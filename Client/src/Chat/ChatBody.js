import MessageList from "./MessageList";
import MessageSender from "./MessageSender";
import {useEffect, useRef, useState} from "react";
import {GetMessages} from "../ServerQuery/ChatQuery";

function ChatBody({user, chat, JWT, setChats,socket}) {

    // messages stay between renders
    const [messages, setMessages] = useState(null)

    // to determine when messages change whether to load new messages (a different chat was selected)
    // or to add one message to the message list (a message was sent)
    const oldChatId = useRef(null)

    useEffect(()=>{
        // updates messages when a new message arrives from the server
        const updateMessages=(chatID,msg)=>{
            // chat must be selected and the new messages from the server only matter
            // if the chat id of the chat with the new messages is equal to the selected chat id
            // (we only see the messages from the selected chat)
            if(chat&&chat.id===chatID) {
                // add the new message at the start of the message list
                if(!messages||messages.length===0)
                    setMessages([msg])
                else if(messages[0].id!==msg.id)
                    setMessages(messages=>[msg,...messages])
            }
        }

        socket.on('newMessage',updateMessages)

        return (()=>{socket.off('newMessage',updateMessages)})
    },[socket,chat,messages])

    useEffect(() => {
        // messages updated

        if (!chat)
            return

        const updateMessages = async () => {
            // render the change in messages
            if (chat.id === oldChatId.current) {
                // chat selected didn't change - means messages changed.
                // updates the last message.
                if(messages.length>0) {
                    if(!chat.lastMessage||chat.lastMessage.id!==messages[0].id) {
                        chat.lastMessage = messages[0]
                    }
                }
            } else {
                // chat selection changed - gets the messages of the new chat.
                oldChatId.current = chat.id
                setMessages(await GetMessages(chat.id, JWT))
            }
            // updates the last message on the left with the new message
            setChats(chats => [...chats])
        }
        updateMessages()
    }, [JWT, chat, setChats, messages,socket,user])


    return (
        <>
            <MessageList user={user} messages={messages} JWT={JWT}/>
            <MessageSender user={user} chat={chat} JWT={JWT} setMessages={setMessages} socket={socket}/>
        </>
    )
}

export default ChatBody