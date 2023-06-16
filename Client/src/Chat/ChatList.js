import ChatPreview from "./ChatPreview";

let selected = false;

export function setSelected({bool}) {
    // determines when the screen is small if the chat list will be shown or the chat body (content)
    selected = bool;
}



function ChatList({chats, setChats}) {



    function changeSelection(chat) {
        // selection changes

        let allow = false;
        // if the screen is small enough only show the chat list, or, if a chat is selected, show the chat.
        if (window.innerWidth <= 632 && !selected) {
            document.getElementById('chat').classList.add('fullscreen');
            document.getElementById('chat-list').classList.add('hidden');
            allow = true;
        }
        // only changes if the new selected chat is not already selected
        if (allow || !chat.classes.includes("selected-preview")) {
            // changes the selected chat
            const selectedChat = chats.filter((chat) => {
                return chat.classes.includes("selected-preview");
            });
            if(selectedChat.length>0){
                selectedChat[0].classes=selectedChat[0].classes.replace("selected-preview", " ");
            }
            chat.classes+=" selected-preview";
            selected = true;
            // updates chat list on the left
            setChats([...chats]);
        }
    }

    window.addEventListener('resize', function () {
        // if the screen is small enough only show the chat list, or, if a chat is selected, show the chat.
        if (selected && document.getElementById('chat') != null) {
            if (window.innerWidth <= 632) {
                document.getElementById('chat').classList.add('fullscreen');
                document.getElementById('chat-list').classList.add('hidden');

            } else {
                document.getElementById('chat').classList.remove('fullscreen');
                document.getElementById('chat-list').classList.remove('hidden');
            }

        }
    })

    const chatList = chats.map((chat, key) => {
        return <ChatPreview chat={chat} changeSelection={changeSelection} key={key}/>
    });

    return (
        <div id="chat-list">
            {chatList}
        </div>
    );
}

export default ChatList;