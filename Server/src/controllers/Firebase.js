const {getMessaging}=require('firebase-admin/messaging')

let tokenUsers=[]

const getUsers=(usernames)=>{
    let users=[]
    tokenUsers.forEach(item=>{
        if(usernames.includes(item.username))
            users.push(item)
    })
    return users
}

const newToken=async (req,res)=>{
    try {
        const username = req.body.username
        const token = req.body.token
        tokenUsers.push({username, token})
        console.log(req.body.username+" connected")
        res.status(200)
    }
    catch (e) {
        res.status(404).send("Must provide username and token")
    }

}

const newChat=(usernames)=>{
    const users=getUsers(usernames)
    if(users.length>0){
        // send the "NewChat" message to every user
        users.forEach(user=>{
            const message={
                data:{type:"NewChat"},
                token:user.token
            }
            getMessaging().send(message).then(response=>{
                console.log("Notified "+user.username+" of a new chat")
            })
        })
    }
}

const newMessage=(usernames,chatID,msg)=>{
    let users=[]
    const senderUsername=msg.sender.username
    tokenUsers.forEach((item)=>{
        if(usernames.includes(item.username))
            users.push(item)
    })
    if(users.length>0){
        users.forEach(item=>{
            const message={
                data:{

                }
            }
        })
    }
}

module.exports={newToken,newChat,newMessage}