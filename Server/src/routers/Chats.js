const express = require('express');
var router = express.Router();
const ChatController = require('../controllers/Chats');

router.route('/')
    // POST api/Chats
    .post(ChatController.isLoggedIn, ChatController.createChat)
    // GET api/Chats
    .get(ChatController.isLoggedIn, ChatController.getChats)
router.route('/:id')
    // GET api/Chats/{id}
    .get(ChatController.isLoggedIn, ChatController.getChatById)
    // DELETE api/Chats/{id}
    .delete(ChatController.isLoggedIn, ChatController.deleteChatById)
router.route('/:id/Messages')
    // POST api/Chats/{id}/Messages
    .post(ChatController.isLoggedIn, ChatController.addMessageToChat)
    // GET api/Chats/{id}/Messages
    .get(ChatController.isLoggedIn, ChatController.getMessagesByChatId)

module.exports = router;
