const express = require('express');
const router = express.Router();
const FirebaseController=require('../controllers/Firebase')
const UsersController=require("../controllers/Users")

// POST api/Firebase
router.route('/').post(UsersController.isLoggedIn,FirebaseController.newToken)
// DELETE api/Firebase
router.route('/').delete(UsersController.isLoggedIn,FirebaseController.removeToken)

module.exports=router