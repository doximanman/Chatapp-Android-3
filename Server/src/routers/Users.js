const express = require('express');
var router = express.Router();
const UsersController = require('../controllers/Users');

// POST api/Users
router.route('/')
    .post(UsersController.createUser)
// GET api/Users/{username}
router.route('/:username')
    .get(UsersController.isLoggedIn, UsersController.getUserByUsername)
module.exports = router;
