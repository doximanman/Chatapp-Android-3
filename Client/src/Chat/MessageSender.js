import sendIcon from "../Pictures/send-icon.png";
import {useRef} from "react";
import {SendMessage} from "../ServerQuery/ChatQuery";

function MessageSender({user,chat,JWT,setMessages,socket}){
    // name is self explanatory...

    const userInput = useRef(null);

    // still loading
    if(!chat){
        return <></>
    }

    const newMessage = async ()=> {
        const input = userInput.current.value;
        // only sends messages with non-whitespace characters.
        if (/\S/.test(input)) {

            // sends it
            const message=await SendMessage(input,chat.id,JWT)


            if(message===null){
                // sent failed
                return
            }

            // adds the message locally and notifies the server to notify other users of the same chat to update their
            // last message on the left, and the message list if this chat is selected!
            setMessages(messages=>[message,...messages]);
            const users = [user.username, chat.user.username]
            socket.emit("newMessage", users, chat.id, message)
        }
        // resets message text box after send
        document.getElementById('message-input').value = '';
    }

    const enterKey = async (e)=> {
        // enter also works for sending messages.
        if (e.key === "Enter")
            await newMessage();
    }


    return(
        <div id="message-send">
            <input ref={userInput} onKeyUp={enterKey} type="text" id="message-input" placeholder="Type your message..."/>
            <button onClick={newMessage} id="send-btn" type="submit"
                    className="button-8">
                <img id="send-icon" src={sendIcon} height="28" width="40" alt="send"/>
            </button>
        </div>
    );
}

export default MessageSender;