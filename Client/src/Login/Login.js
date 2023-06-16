import "./Login.css"
import Input from '../FormsItems/Input';
import Button from '../FormsItems/Button';
import Title from '../FormsItems/Title';
import BottomMessage from '../FormsItems/BottomMessage';
import React, { useState } from 'react';
import { useNavigate } from "react-router-dom"
import { GetUser, ValidateUser } from "../ServerQuery/UserQuery";

function Login({ setUser }) {
    const [input, setInput] = useState({
        Username: '',
        Password: ''
    });

    const [error, setError] = useState({
        Username: '',
        Password: ''
    });

    const onInputChange = e => {
        const { name, value } = e.target;
        setInput(prev => ({
            ...prev,
            [name]: value
        }));
        setError(prev => ({
            ...prev,
            Username: "",
            Password: ""
        }));

    }

    const navigate = useNavigate();
    async function handleLogin(e) {
        e.preventDefault();
        const username = input.Username;
        const password = input.Password;
        // validate
        const JWT=await ValidateUser(username, password)
        if (JWT) {
            // valid

            // save JWT and username
            sessionStorage.setItem('JWT',JWT)
            sessionStorage.setItem('username',username)

            // get user and move to chat
            const user = await GetUser(username, sessionStorage.getItem('JWT'));
            setUser(user)
            navigate("/Chat");
        }
        else {
            // invalid
            setError(prev => ({
                ...prev,
                Username: "User doesn't exist, please register first."
            }));
        }
    }

    return (
        <form>
            <div id="login-form">
                <Title title="Login to Message-Manager!"></Title>
                <Input description={{ labelClass: "col-sm-2 col-form-label name", ins: "Username", name: "Username", divClass: "col-sm-10", type: "text", id: "Username", value: input.Username, onChange: onInputChange, className: error.Username ? "is-invalid form-control" : "form-control" }}></Input>
                {error.Username && <span className='err invalid-feedback small'>{error.Username}</span>}
                <Input description={{ labelClass: "col-sm-2 col-form-label name", ins: "Password", name: "Password", divClass: "col-sm-10", type: "password", id: "Password", value: input.Password, onChange: onInputChange, className: error.Password ? "is-invalid form-control" : "form-control" }}></Input>
                {error.Password && <span className='err invalid-feedback small'>{error.Password}</span>}
                <Button description={{ id: "login-button", name: "Login", onClick: handleLogin }}></Button>
                <BottomMessage description={{ id: "not-registered", question: "Not regitered? ", link: "/Register", click: "Click here", goal: " to register" }}></BottomMessage>
            </div>
        </form >
    );
}

export default Login;
