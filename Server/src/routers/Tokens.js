const express = require('express');
var router = express.Router();
const TokensController = require('../controllers/Tokens');

// POST api/TOKENS
router.route('/')
    .post(TokensController.createToken)

module.exports = router;
