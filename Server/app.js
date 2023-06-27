const express = require('express');
var app = express();

app.use(express.static('public'));

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended: true}));
app.use(express.json({limit:'50mb'}));

const cors = require('cors');
app.use(cors());

const customEnv = require('custom-env')
customEnv.env(process.env.NODE_ENV, './config');
console.log(process.env.CONNECTION_STRING);
console.log(process.env.PORT);

const mongoose = require('mongoose').default;
mongoose.connect(process.env.CONNECTION_STRING,
    {
        useNewUrlParser: true,
        useUnifiedTopology: true
    }
);

const users = require('./src/routers/Users');
app.use('/api/Users', users);

const tokens = require('./src/routers/Tokens');
app.use('/api/Tokens', tokens);

const firebase=require('./src/routers/Firebase');
app.use('/api/Firebase',firebase);

const chats = require('./src/routers/Chats');
app.use('/api/Chats', chats);

// anything not defined goes to react
const path=require('path')
app.get('*',(req,res)=>{
    res.sendFile(path.join(__dirname,'public','index.html'))
})

const http = require('http');
const server = http.createServer(app);
const io = require('socket.io')(server,{
    cors:{
        origin:'*',
    }
});
const sockets = require("./src/routers/Sockets")
io.on('connection', (socket) => sockets.newSocket(io,socket));

// firebase
const admin=require('firebase-admin')
const serviceAccount=require('./config/chatapp-ba0bd-firebase-adminsdk-vzq5s-24a0314578.json')
admin.initializeApp({
    credential:admin.credential.cert(serviceAccount)
})




server.listen(process.env.PORT);