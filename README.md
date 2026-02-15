# Server-Client Chat Application (Java Swing)

## 📌 Project Overview

Java Swing Socket Chat is a simple messaging system that allows a client and server to exchange messages through a graphical interface.  
The application uses **TCP sockets** for reliable communication and **background threads** to ensure the GUI remains responsive.

This project demonstrates:
- Client–Server architecture
- TCP communication using Sockets
- Multithreading in Java
- GUI development using Swing
- Event handling

---

## 🚀 Features

### Server
- GUI-based chat interface
- Displays complete chat history using `JTextArea`
- Auto-scroll to latest messages
- Send messages using:
  - SEND button
  - Enter key
- Background threads for:
  - Server startup
  - Client connection
  - Receiving messages
- Shows connection status:
  - Waiting for client
  - Connected
  - Disconnected
- Safe handling of client disconnection
- Closes server when **"end"** message is sent

---

### Client
- GUI-based chat window using Swing
- Connects to server on **localhost:5200**
- Displays full chat history with auto-scroll
- Send messages using:
  - SEND button
  - Enter key
- Background thread for receiving messages
- Displays connection status
- Closes connection when **"end"** message is sent

---

## 🧠 Concepts Used

- Java Networking (`ServerSocket`, `Socket`)
- TCP Communication
- Multithreading
- Event Handling (`ActionListener`)
- Swing Components (`JFrame`, `JTextArea`, `JTextField`, `JButton`, `JScrollPane`)
- Stream Handling (`BufferedReader`, `PrintStream`)

---

## 🖥️ Technologies

- Java
- Swing (GUI)
- Socket Programming
- Multithreading
---

## ⚙️ How to Run
```bash
# Step 1: Compile
javac ServerUpdate1.java
javac ClientUpdate1.java

# Step 2: Run Server
java ServerUpdate1

# Step 3: Run Client (in another terminal)
java ClientUpdate1
```
## 🖥️ Usage

- Start the **Server** first.
- Then start the **Client**.
- Type a message and press **Enter** or click **SEND**.
- To end the chat, type: end

- Both server and client will close the connection.

## 🔮 Future Improvements

- Multiple client support
- Chat timestamps
- Usernames for participants
- File sharing feature

## 👨‍💻 Author

**CR Dugade**
Aspiring Software Developer

* GitHub: https://github.com/crd-codes
* LinkedIn: https://linkedin.com/in/chakradhar-dugade

