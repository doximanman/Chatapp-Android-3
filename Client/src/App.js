import {BrowserRouter, Routes, Route} from "react-router-dom";
import Chat from "./Chat/Chat"
import Login from "./Login/Login";
import Register from "./Register/Register"
import React, { useState} from 'react';

function App() {

    // user will be set by /login and /chat
    const [user, setUser] = useState(null)

    return (
        <BrowserRouter>
            <Routes onL>
                <Route path="/" element={<Login setUser={setUser}/>}></Route>
                <Route path="/Login" element={<Login setUser={setUser}/>}></Route>
                <Route path="/Chat" element={<Chat user={user} setUser={setUser}/>}></Route>
                <Route path="/Register" element={<Register/>}></Route>
            </Routes>
        </BrowserRouter>
    );

}


export default App;
