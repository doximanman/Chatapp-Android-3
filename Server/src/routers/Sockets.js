
const SocketController=require('../controllers/Sockets');


const newSocket=(io,socket)=>{
    socket.on('connect',()=>SocketController.connect(socket));
    socket.on('user',(msg)=>SocketController.newUser(socket,msg));
    socket.on('disconnect',()=>SocketController.disconnect(socket));

    // now happens automatically
    //socket.on('newMessage',(usernames,chatID,msg)=>SocketController.newMessage(usernames,chatID,msg));
    //socket.on('newChat',(usernames,chat)=>SocketController.newChat(usernames,chat));
}

module.exports={newSocket};