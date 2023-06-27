const express = require('express');
const router = express.Router();
const FirebaseController=require('../controllers/Firebase')
const UsersController=require("../controllers/Users")

router.route('/').post(UsersController.isLoggedIn,FirebaseController.newToken)

module.exports=router