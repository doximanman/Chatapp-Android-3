const Chat = require('../models/Chats')
const MessageService = require('./Messages')
const UserService = require('./Users')

// create new chat by the sender's and receiver's username
const createChat = async (sender, receiver) => {
    // ensure the receiver user exist
    if (await UserService.getUserByUsername(receiver) === null) {
        return 0;
    }
    // if he does - create new chat between the sender and receiver
    else {
        const chat = new Chat({ users: [sender, receiver], messages: [] });
        return await chat.save();
    }
};

// get json of username, displayName and profilePic of the given username
const getUserDetailes = async (username) => {
    const user = await UserService.getUserByUsername(username);
    return { username: username, displayName: user.displayName, profilePic: user.profilePic };
};

// get all the chats of the given username
const getChats = async (username) => {
    // extract all the chats from the Chats table
    const allChats = await Chat.find({});
    // initialize array of chats relavent for the given username 
    let chats = [];
    // scan them one by one
    for (const chat of allChats) {
        // define lastMessage detailes, if exist
        let lastMessage = chat.messages.length !== 0 ? await MessageService.getMessageById(chat.messages[chat.messages.length - 1]) : [];
        // insert the second user to the chat detailes item
        if (chat.users[0] === username) {
            chats.push({ id: chat._id, user: await getUserDetailes(chat.users[1]), lastMessage: lastMessage.id  ? { id: lastMessage._id, created: lastMessage.created, content: lastMessage.content } : null });
        }
        else if (chat.users[1] === username) {
            chats.push({ id: chat._id, user: await getUserDetailes(chat.users[0]), lastMessage: lastMessage.id  ? { id: lastMessage._id, created: lastMessage.created, content: lastMessage.content } : null });
        }
    };
    return chats;
};

// get chat by id, 0 if it doesn't exist or 1 if the given username doesn't appear on it
const getChatById = async (username, id) => {
    const chatById = await Chat.findOne({ _id: id });
    // ensure the chat with the given id exist
    if (chatById === null) {
        return 0;
    }
    // ensure the given username appears on the users chat array
    if (chatById.users[0] !== username && chatById.users[1] !== username) {
        return 1;
    }
    // if all the checkes passed successfully - define the messages detailes array with the desired format
    let messages = [];
    for (const messageId of chatById.messages) {
        const message = await MessageService.getMessageById(messageId);
        messages.push({ id: message._id, created: message.created, sender: await getUserDetailes(message.sender), content: message.content });
    }
    // return the detailes of the desired chat
    return { id: id, users: [await getUserDetailes(chatById.users[0]), await getUserDetailes(chatById.users[1])], messages: messages.reverse() };
};

// delete chat by it's id, return 0 if it doesn't exist, 1 if the guven username doesn't appear on it or 2 otherwise
const deleteChatById = async (username, chatId) => {
    // extract the chat with the given chatId
    const chatById = await Chat.findOne({ _id: chatId });
    // ensure the chat exist
    if (chatById === null) {
        return 0;
    }
    // ensure the givenusername appears on the users chat array
    if (chatById.users[0] !== username && chatById.users[1] !== username) {
        return 1;
    }
    await Chat.deleteOne({ _id: chatId });
    return 2;
};

// add message from the given sender to the chat with the given chatId, contains the given content
// and return 0 if the chat doesn't exist, 1 if the sender doesn't appear on it or the new message detailes otherwise
const addMessageToChat = async (sender, chatId, content) => {
    // extract the chat with the given chatId
    const chatById = await Chat.findOne({ _id: chatId });
    // ensure the chat exist
    if (chatById === null) {
        return 0;
    }
    // ensure the given sender's username appears on the users chat array
    if (chatById.users[0] !== sender && chatById.users[1] !== sender) {
        return 1;
    }
    // if all passed successfully - create new message with the given sender and content
    const newMessage = await MessageService.createMessage(sender, content);
    // update the messages array of the desired chat
    await Chat.updateOne(
        { _id: chatId },
        { $set: { messages: [...chatById.messages, newMessage._id] } }
    );
    // define the sender detailes according to the requested foramt
    const senderDetailes = await getUserDetailes(sender);
    // return the detailes of the new message created
    return { id: newMessage._id, created: newMessage.created, sender: senderDetailes, content: content };
};

// get all the messages of the chat with the given chat id, return 0 if not exist
const getMessagesByChatId = async (username, chatId) => {
    // extract the chat with the given chatId
    const chatById = await Chat.findOne({ _id: chatId });
    // ensure the chat exist
    if (chatById === null) {
        return 0;
    }
    // ensure the given sender's username appears on the users chat array
    if (chatById.users[0] !== username && chatById.users[1] !== username) {
        return 1;
    }
    // initialize messages array
    let messages = [];
    // scan all the messages in the chat and insert them to the array according to the requested format 
    for (const messageId of chatById.messages) {
        const message = await MessageService.getMessageById(messageId);
        messages.push({ id: messageId, created: message.created, sender: { username: message.sender }, content: message.content });
    }
    // return the messages array
    return messages.reverse();
};

module.exports = { createChat, getChats, getChatById, deleteChatById, addMessageToChat, getMessagesByChatId };
