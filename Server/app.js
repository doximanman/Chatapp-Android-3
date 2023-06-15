const express = require('express');
var app = express();

app.use(express.static('public'));

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended: true, limit: '50mb'}));
app.use(express.json());

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


server.listen(process.env.PORT);