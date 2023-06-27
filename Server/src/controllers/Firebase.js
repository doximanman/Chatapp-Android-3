const {getMessaging}=require('firebase-admin/messaging')

let tokenUsers=[]

const newToken=async (req,res)=>{
    try {
        const username = req.body.username
        const token = req.body.token
        tokenUsers.push({username, token})
        console.log(req.body.username+" connected")
        res.status(200)
    }
    catch (e) {
        res.status(400).send("Must provide username and token")
    }

}

const removeToken=async(req,res)=>{
    try {
        const username = req.body.username
        const token = req.body.token
        const currentUser=tokenUsers.findIndex(user=>user.username===username&&user.token===token)
        if(currentUser>=0){
            tokenUsers.splice(currentUser,1)
            console.log(req.body.username+" disconnected")
            res.status(200)
        }
        else{
            console.log(req.body.username+" tried to disconnect but isn't connected")
            res.status(403).send("Not connected")
        }

    }
    catch (e) {
        res.status(400).send("Must provide username and token")
    }
}

const newChat=(usernames)=>{
    const users=tokenUsers.filter(user=>usernames.includes(user.username))
    if(users.length>0){
        // send the "NewChat" message to every user
        users.forEach(user=>{
            const message={
                data:{type:"NewChat",username:usernames},
                token:user.token
            }
            getMessaging().send(message).then(response=>{
                console.log("Notified "+user.username+" of a new chat")
            })
        })
    }
}

const newMessage=(senderUsername,senderDisplayName,usernames,chatID,msg)=>{
    const users=tokenUsers.filter(user=>usernames.includes(user.username))
    if(users.length>0){
        users.forEach(user=>{
            const message={
                data:{
                    type:"NewMessage",
                    message:msg.content,
                    messageId:msg.id.toString(),
                    chatId:chatID,
                    username:senderUsername,
                    displayName:senderDisplayName,
                    created:msg.created
                },
                token:user.token
            }
            getMessaging().send(message).then(response=>{
                console.log("sending a new message to "+user.username)
            })
        })
    }
}

module.exports={newToken,removeToken,newChat,newMessage}