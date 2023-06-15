const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Chats = new Schema({
    users: {
        type: Array
    },
    messages: {
        type: Array
    }
});

module.exports = mongoose.model('Chats', Chats);