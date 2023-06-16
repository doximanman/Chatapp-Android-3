const User = require('../models/Users')

// get user detailes by username (or null if it doesn't exist)
const getUserByUsername = async (username) => {
    return await User.findOne({ username: username });
};

// get user detailes by username and password (or null if it doesn't exist)
const getUserByUsernameAndPassword = async (username, password) => {
    return await User.findOne({ username: username, password: password });
};

// create user by his personal detailes - username, password,displayName and profilePic
const createUser = async (username, password, displayName, profilePic) => {
    // define regex expressions
    const passwordRegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
    const usernameRegExp = /^[a-zA-Z0-9-_!.]{4,20}$/;
    
    // check for user with the same username to prevent duplicates
    const userWithUsername = await getUserByUsername(username);
    if (userWithUsername !== null) {
        return "Username already exist, please choose another one.";
    }

    // ensure the username fulfills the regex conditions
    if (!usernameRegExp.test(username)) {
        return "Username must contain 4-20 characters, can include letters, digits and _-!.";
    }
    // ensure the password fulfills the regex conditions
    if (!passwordRegExp.test(password)) {
        return "The passwors must contain 8-20 characters, at least one uppercase and lowercase leters, special character and digit.";
    }
    // ensure the displayName isn't empty
    if (displayName === "") {
        return "Please enter display name.";
    // ensure the displayName isn't identical to the username
    } else if (displayName === username) {
        return "Please don't use your real name for the displayed name.";
    }
    // ensure the profilePic isn't empty
    if (profilePic === "") {
        return "Please upload profile picture.";
    }
    // if all the validation checks passed successfully - create the user
    const user = new User({ username: username, password: password, displayName: displayName, profilePic: profilePic });
    return await user.save();
};

module.exports = { getUserByUsername, createUser, getUserByUsernameAndPassword };
