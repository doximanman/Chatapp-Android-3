
const UsersService=require("../services/Users")

// list of {name,socket} jsons for each user that logs in.
let socketUsers= [];

// helper function to find user from username
const getUser=username=>{
    let user=null;
    socketUsers.forEach(item=>{
        if(item.user.username===username)
            user=item.user
    })
    return user
}

const connect=(socket)=>{
    // the real connection starts at newUser
}

const newUser=(socket, user)=>{
    // add name-socket json to the array
    socketUsers.push({user,socket})
    console.log(user.username+" connected")
}

const disconnect=(socket)=>{
    // find the socket in the socketUsers list
    let index=-1;
    socketUsers.forEach((item,pos)=>{
        if(item.socket===socket)
            index=pos
    })

    // if found - remove from the socketUsers list.
    if(index>=0) {
        console.log(socketUsers[index].user.username+ " disconnected");
        socketUsers.splice(index,1);
    }

    // if not found - a socket disconnected without being connected (without going through the newUser function). weird!
    else{
        console.log("A socket tried to disconnect that isn't registered!");
    }
}

const newMessage=(usernames,chatID,msg)=>{
    // find all relevant users
    let users=[];
    const senderUsername=msg.sender.username;
    socketUsers.forEach((item)=>{
        if(usernames.includes(item.user.username))
            users.push(item)
    })
    if(users.length>0){
        // tell the relevant users of the new message
        users.forEach(item=>{
            item.socket.emit("newMessage",chatID,msg)
            console.log("sending a new message to ",item.user.username)
        })
    }
}

const newChat=async (usernames,chat)=>{
    // find relevant users
    let users=[];
    socketUsers.forEach((item)=>{
        if(usernames.includes(item.user.username))
            users.push(item)
    })
    if(users.length>0){
        // tell the new users of the new chat
        for (const item of users) {
            if(item.user.username===chat.user.username) {
                // chat user has to change for the user that didn't make the chat ('tho shall not talk with thy self')
                const otherUsername = usernames.filter(username => username !== item.user.username)[0]
                chat.user=await UsersService.getUserByUsername(otherUsername)
            }
            item.socket.emit("newChat",chat)
            console.log("new chat created with ",item.user.username)
        }
    }
}

module.exports={newUser,disconnect,newMessage,connect,newChat};