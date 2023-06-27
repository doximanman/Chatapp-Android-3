import ChatMessage from "./ChatMessage";
function MessageList({user, messages}) {

    // convert from messages object array to JSX

    if(!messages){
        return (<div id="message-list"></div>)
    }

    const messageList = messages.map((message, key) => {
        return <ChatMessage user={user} message={message} key={key}/>;
    });

    return (
        <div id="message-list">
            {messageList}
        </div>
    );
}

export default MessageList;