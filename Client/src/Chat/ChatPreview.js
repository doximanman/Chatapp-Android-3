function convertFormat(dateTime) {
    let date = dateTime.split("T")[0]
    let time = (dateTime.split("T")[1]).split(".")[0]
    return date + " " + time
}

function ChatPreview({chat, changeSelection}) {

    // displays the chat's details (pfp, displayname,last message)
    const className = "chat-preview " + chat.classes;

    // converts from 'message' object to { date: HH:MM , message: message_text }
    const lastMSG = chat.lastMessage
    const lastMessage = lastMSG ? {
        date: convertFormat(lastMSG.created),
        message: lastMSG.content
    } : {
        date: '',
        message: ''
    };


    return (
        <div onClick={() => changeSelection(chat)} className={className}>
            <img className="profile-pic" src={chat.user.profilePic} alt="Profile"/>
            <div className="profile-name">{chat.user.displayName}</div>
            <div className="preview-date">{lastMessage.date}</div>
            <p className="last-message">{lastMessage.message}</p>
        </div>
    );

}

export default ChatPreview;