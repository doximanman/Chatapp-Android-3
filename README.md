# Chat App
## The Aim of the App
  This app aims to allow people to chat with each other from anywhere in the world easily and intuitively, from one app.

## Client functionality (front-end)
* ### Register page:
  You can register to the website. Registration requires a username, password, a display name and a profile picture.
  
  Once you provide valid credentials (specified by the requirements that show when you start typing), you can hit the "register" button and
  you will be fully registered and sent to the login page to log in.
  
  Your credentials are saved in a database by the server. Once you've registered, it is saved forever!
  
  If you've already registered, you can click on the bottom of the page to go to the login page.
    
* ### Login page:
  On this page you can login to the website using the credentials you've provided in the registration form.
  
  Once you type in your username and password, they will be checked and validated according to past registrations.
  If the username-password combo match an existing registered user you will be logged in and sent to the chat page.
  
  If you haven't logged in yet, you can click on the bottom of the page to go the register page.
    
* ### Chat page:
  This is the main page of the app. On the left you can see your display name and your profile picture at the top,
  along with a button to add new chats to your chat list, which you can see right underneath. You can also logout
  using the red button that says "logout".
  
  The chat list contains the list of all the chats that you have added in the past. Every chat in the list
  has a profile picture, a display name, and the last message sent or recieved, with its date.
  
  From the chat list you can select a chat to display on the right side. If a chat is selected, it will be highlighted
  in blue.
  
  On the right side you can see the selected chat. At the top you have the other person's profile picture and display
  name. Below that you have the chat itself containing all the messages, and below that you have a message box along
  with a send button.
  
  You can change the selected chat, add a new contact, send and recieve messages, and logout.
  Chats created with you are added on your end automatically.
  
  Notice that you cannot browse to the chat page unless you are logged in. After logging in once, you stay logged in until
  you explicitly log out.
  
## Server functionality (back-end)
  The server of the app is designed using the MVC (Model-View-Controller) pattern. This architectural pattern helps organize the codebase into distinct components, making it easier to maintain and extend the application in the future.
  * MVC Architecture: The server follows the MVC pattern, which separates the concerns of data management, presentation, and user interaction. This promotes code reusability and maintainability.
  * HTTP Requests: User actions within the app are translated into HTTP requests, which are sent directly to the server. The server then handles these requests, performing the necessary operations and providing appropriate responses.
  * MongoDB Database: We have chosen to use the MongoDB NoSQL database management system to store and manage the app's data. MongoDB's flexibility and scalability make it a suitable choice for handling varying data structures and accommodating future growth.
  * Database Integration: Each incoming request to the server involves communication with the MongoDB database. This allows for seamless data import and export, ensuring that the desired information is efficiently processed and delivered.
  
## How to launch the project
  In order to run the project you'll need to have NodeJS installed (preferably version 18 and above), then you can choose to run the server
  or the client:
  1. save the files in a folder (unzipped)
  ### To run the server:
  2. open a terminal in the Server folder
  3. run
  ```
  npm i
  npm start
  ```
  ### To run the client:
  2. open a terminal in the Client folder
  3. run
  ```
  npm i
  npm start
  ```
  
  Once the server is running, you can browse to ```localhost:5000``` and the client will be a build of the client that runs solely on the server.
  
  If the client is running seperately, you can browse to ```localhost:3000``` and the client will be served by the react server. It will communicate with the server at ```localhost:5000``` (if a different server is used make sure to set the port to 5000)
