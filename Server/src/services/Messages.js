const Message = require('../models/Messages')

// get message by id (or null if not exist)
const getMessageById = async (id) => {
    return await Message.findById(id);
};

// create message by the sender username and content with the current date time
const createMessage = async (sender, content) => {
    const msg = new Message({ created: new Date().toISOString(), sender: sender, content: content });
    return await msg.save();
};

module.exports = { getMessageById, createMessage };
