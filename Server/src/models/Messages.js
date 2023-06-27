const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Messages = new Schema({
    created: {
        type: String
    },
    sender: {
        type: String
    },
    content: {
        type: String
    }
});

module.exports = mongoose.model('Messages', Messages);