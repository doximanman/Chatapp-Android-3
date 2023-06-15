import add from "../Pictures/add.png";
import { useRef } from "react";
import {useNavigate} from "react-router-dom";
import {AddChat} from "../ServerQuery/ChatQuery";

function Profile({ user, setChats,JWT,chats,socket }) {

    const contactInput = useRef(null);

    function updateDismiss() {
        // add button only works when the contact input box isn't empty (ignoring white spaces)
        if (contactInput.current.value !== '') {
            document.getElementById('addchatBTN').setAttribute('data-bs-dismiss', "modal");
        }
        else {
            document.getElementById('addchatBTN').removeAttribute('data-bs-dismiss');
        }
    }
    
    async function newChat() {
        // ignores white spaces
        if (/\S/.test(contactInput.current.value)) {

            // if the chat already exists - go to the chat.
            const exists=chats.filter(chat=>chat.user.username===contactInput.current.value)
            if(exists.length>0) {
                // select the appropriate chat
                chats.forEach(chat=>{
                    chat.classes=""
                })
                exists[0].classes="selected-preview"

                // reset new chat input box
                contactInput.current.value = '';
                updateDismiss();

                // update chat list to reflect the selection change
                setChats(chats => [...chats]);
                return
            }

            // chat doesn't exist - creates a new one.
            let chat=await AddChat(contactInput.current.value,JWT)
            if(!chat){
                contactInput.current.value = '';
                updateDismiss();
                return
            }
            // new chat isn't selected (the chat returned from server doesn't have the 'classes' key)
            chat.classes=""
            // notifies for the other users (user) of the chat to create the chat.
            socket.emit("newChat",[user.username,contactInput.current.value],chat)
            // updates the list on the left
            setChats(chats => [...chats, chat]);
            // resets new chat input box
            contactInput.current.value = '';
            updateDismiss();
        }
    }
    const navigate=useNavigate();

    const handleLogout = () => {
        // disconnects from socket and clears JWT
        socket.disconnect();
        sessionStorage.clear()
        navigate("/Login")
    }
        

    return (
        <div id="profile">
            <img className="profile-pic" src={user.profilePic} alt="Profile" />
            <div id="user-name" className="profile-name">{user.displayName}</div>
            <img id="add-chat" data-bs-toggle="modal" data-bs-target="#addChat" src={add}
                alt="New Chat" />
            <button type="button" onClick={handleLogout} className="btn btn-danger btn-sm" id="logout">Logout</button>
            <div className="modal" id="addChat" aria-labelledby="addChatTitle">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h1 className="modal-title" id="addChatTitle">Add a New Chat</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <input ref={contactInput} onKeyUp={updateDismiss} id="nameInput" type="text" className="col-12" placeholder="Contact Name" />
                        </div>
                        <div className="modal-footer">
                            <button onClick={newChat} id="addchatBTN" type="button" className="btn btn-primary">Add</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;
