const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Users = new Schema({
    username: {
        type: String
    },
    password: {
        type: String
    },
    displayName: {
        type: String
    },
    profilePic: {
        type: String
    }
});

module.exports = mongoose.model('Users', Users);