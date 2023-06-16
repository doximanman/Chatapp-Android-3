import {serverAddress} from "./ServerInfo";

export async function GetChats(JWT){
    let res;
    try {
        res = await fetch(serverAddress + "/api/Chats", {
            'method': 'get',
            'headers': {
                'Accept': 'application/json',
                'authorization': 'bearer ' + JWT
            },
        })
    }catch(e){
        alert(e)
        return null
    }
    if(!res.ok){
        return null;
    }
    return await res.json()
}

export async function AddChat(contactName,JWT){
    const data={username: contactName}
    let res;
    try {
        res = await fetch(serverAddress + "/api/Chats", {
            'method': 'post',
            'headers': {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization': 'bearer ' + JWT,
            },
            'body': JSON.stringify(data)
        })
    }catch(e){
        alert(e)
        return null
    }
    if(!res.ok){
        alert(await res.text())
        return null
    }
    return await res.json()
}

export async function GetChat(id,JWT){
    let res
    try {
        res = await fetch(serverAddress + "/api/Chats/" + id, {
            'method': 'get',
            'headers': {
                'authorization': 'bearer ' + JWT,
                'Accept': 'application/json',
            },
        })
    }catch(e){
        alert(e)
        return null
    }
    if(!res.ok){
        return null;
    }
    return await res.json()
}

export async function GetMessages(id,JWT){
    let res
    try{
        res = await fetch(serverAddress + "/api/Chats/" + id+"/Messages", {
            'method': 'get',
            'headers': {
                'authorization': 'bearer ' + JWT,
                'Accept': 'application/json',
            },
        })
    }catch(e){
        alert(e)
        return null
    }
    if(!res.ok){
        alert(await res.text())
        return null;
    }
    return await res.json();
}

export async function SendMessage(msg,id,JWT){
    const data={msg}
    let res
    try{
        res = await fetch(serverAddress + "/api/Chats/" + id+"/Messages", {
            'method': 'post',
            'headers': {
                'authorization': 'bearer ' + JWT,
                'Content-Type':'application/json',
                'Accept': 'application/json',
            },
            'body':JSON.stringify(data)
        })
    }catch(e){
        alert(e)
        return null
    }
    if(!res.ok){
        return null
    }
    return await res.json()
}