import logo from "../Pictures/logo.png";

import {setSelected} from "./ChatList";



function ChatTitle({chat}){

    // chat not selected yet
    if(!chat){
        return (<div id="chat-title"></div>)
    }

    function goBack(){
        // a click on the top of the chat screen when the screen is small and a chat is selected
        // will deselect the chat and go back to the chat list.
        document.getElementById('chat').classList.remove('fullscreen');
        document.getElementById('chat-list').classList.remove('hidden');
        // that variable from ChatList that determines if the chat list or the chat body will be shown
        setSelected(false);
    }



    return(
        <div id="chat-title">
            <img className="profile-pic" src={chat.user.profilePic} alt="Profile"/>
            <div id="chat-name" className="profile-name">{chat.user.displayName}</div>
            <img id="side-logo" src={logo} alt="Chatapp"></img>
            <button id="go-back" onClick={goBack} type="button"></button>
        </div>
    );

}


export default ChatTitle;