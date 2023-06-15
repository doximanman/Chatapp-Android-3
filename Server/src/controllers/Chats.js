const ChatService = require('../services/Chats');
const UserService = require('../services/Users');
// Use a library to perform the cryptographic operations
const jwt = require("jsonwebtoken")
const key = process.env.KEY;


// Ensure that the user sent a valid token
const isLoggedIn = (req, res, next) => {
    // If the request has an authorization header
    if (req.headers.authorization) {
        // Extract the token from that header
        const token = req.headers.authorization.split(" ")[1];
        // const tokenObject = JSON.parse(token);
        // const extractedToken = tokenObject.token;
        try {
            // Verify the token is valid
            const data = jwt.verify(token, key, { algorithm: 'HS256' });
            // Token validation was successful. Continue to the actual function (index)
            res.locals.username = data.username;
            return next()
        } catch (err) {
            return res.status(401).send("Invalid Token");
        }
    }
    else
        return res.status(403).send('Token required');
}

// POST (create) new chat for the connected user
const createChat = async (req, res) => {
    // return error if the user trys to create chat with himself
    if (res.locals.username === req.body.username) {
        return res.status(400).send("Thou shalt not talk with theyself");
    }
    // otherwise create the new chat or return appropriate error if exist
    const newChat = await ChatService.createChat(res.locals.username, req.body.username);
    if (newChat === 0) {
        return res.status(404).send('No such user');
    }
    const user = await UserService.getUserByUsername(req.body.username);
    res.json({ id: newChat._id, user: { username: user.username, displayName: user.displayName, profilePic: user.profilePic } });
}

// GET all the chats of the connected user
const getChats = async (req, res) => {
    const chats = await ChatService.getChats(res.locals.username);
    res.json(chats);
};

// GET chat by id for the connected username, return error if exist
const getChatById = async (req, res) => {
    const chat = await ChatService.getChatById(res.locals.username, req.params.id);
    if (chat === 0) {
        return res.status(404).send("Chat not found");
    }
    // the user trys to get chat detailes of a chat he isn't participate in
    if (chat === 1) {
        return res.status(401).send("Unauthorized");
    }
    res.json(chat);
};

// DELETE chat by its id or retun error if exist
const deleteChatById = async (req, res) => {
    const deleteCode = await ChatService.deleteChatById(res.locals.username, req.params.id);
    if (deleteCode === 0) {
        return res.status(404).send("Chat not found");
    }
    // the user trys to delete chat he isn't participate in
    if (deleteCode === 1) {
        return res.status(401).send("Unauthorized");
    }
    return res.status(204).json();
};

// POST (add) new message to a chat by its id or return error if exist
const addMessageToChat = async (req, res) => {
    const newMessage = await ChatService.addMessageToChat(res.locals.username, req.params.id, req.body.msg);
    if (newMessage === 0) {
        return res.status(404).send("Chat not found");
    }
    // the user trys to add message to a chat he isn't participate in
    if (newMessage === 1) {
        return res.status(401).send("Unauthorized");
    }
    res.json(newMessage);
};

// GET all the messages of a chat by its id or return error if exist
const getMessagesByChatId = async (req, res) => {
    const messages = await ChatService.getMessagesByChatId(res.locals.username, req.params.id);
    if (messages === 0) {
        return res.status(404).send("Chat not found");
    }
    // the user trys to get the messages of a chat he isn't participate in
    if (messages === 1) {
        return res.status(401).send("Unauthorized");
    }
    res.json(messages);
};

module.exports = { createChat, isLoggedIn, getChats, getChatById, deleteChatById, addMessageToChat, getMessagesByChatId };
