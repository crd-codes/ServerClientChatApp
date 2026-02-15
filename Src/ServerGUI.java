/*
Server Features (Current Version)

1. GUI-based chat server using Java Swing.
2. JTextArea with JScrollPane to display full chat history.
3. Auto-scroll enabled to always show latest messages.
4. Send message using:
   - SEND button
   - Enter key (ActionListener on text field)
5. Background thread used for:
   - Starting server
   - Accepting client connection
   - Receiving messages (prevents GUI freezing)
6. Displays connection status (waiting, connected, disconnected, errors).
7. Server can send messages to connected client.
8. Handles client disconnection safely.
9. Closes socket and server when "end" message is sent.
*/


import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

class ServerLogic implements ActionListener
{
    // GUI Components
    JFrame fobj;
    JButton bobj;
    JLabel messagelabel;
    JTextField tobj;
    JTextArea chatArea;
    JScrollPane scroll;

    // Networking
    ServerSocket ssobj;
    Socket sobj;
    PrintStream pobj;
    BufferedReader bobj1;

    public ServerLogic(String title, int width, int height)
    {
        // Build GUI
        fobj = new JFrame(title);

        messagelabel = new JLabel("Message");
        messagelabel.setBounds(30, 20, 80, 30);

        tobj = new JTextField();
        tobj.setBounds(110, 20, 200, 30);
        tobj.addActionListener(this);

        bobj = new JButton("SEND");
        bobj.setBounds(150, 60, 80, 30);

        // Chat history area
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        scroll = new JScrollPane(chatArea);
        scroll.setBounds(30, 110, 320, 150);

        fobj.add(messagelabel);
        fobj.add(tobj);
        fobj.add(bobj);
        fobj.add(scroll);

        bobj.addActionListener(this);

        fobj.setLayout(null);
        fobj.setSize(width, height);
        fobj.setVisible(true);
        fobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background thread for server
        new Thread(() -> {
            try {
                ssobj = new ServerSocket(5200);
                addMessage("Server started. Waiting for client...");

                sobj = ssobj.accept();
                addMessage("Client connected!");

                pobj = new PrintStream(sobj.getOutputStream());
                bobj1 = new BufferedReader(new InputStreamReader(sobj.getInputStream()));

                new Thread(() -> receiveMessages()).start();

            } catch (IOException e) {
                addMessage("Server error: " + e.getMessage());
            }
        }).start();
    }

    // Method to add message to history
    private void addMessage(String text)
    {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(text + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength()); // auto-scroll
        });
    }

    // Receive messages continuously
    private void receiveMessages()
    {
        try {
            String msg;
            while (bobj1 != null && (msg = bobj1.readLine()) != null) {
                addMessage("Client: " + msg);
            }
            addMessage("Client disconnected.");
        } catch (IOException e) {
            addMessage("Connection closed.");
        } finally {
            try { if (sobj != null && !sobj.isClosed()) sobj.close(); } catch (IOException ex) {}
            try { if (ssobj != null && !ssobj.isClosed()) ssobj.close(); } catch (IOException ex) {}
        }
    }

    // Send message
    public void actionPerformed(ActionEvent aobj)
    {
        try {
            if (pobj != null && sobj != null && !sobj.isClosed()) {
                String msg = tobj.getText();
                pobj.println(msg);
                addMessage("You: " + msg);
                tobj.setText("");

                if (msg.equalsIgnoreCase("end")) {
                    addMessage("Chat ended.");
                    sobj.close();
                    ssobj.close();
                    SwingUtilities.invokeLater(() -> fobj.dispose());
                }
            } else {
                addMessage("No client connected.");
            }
        } catch (Exception e) {
            addMessage("Error sending message.");
        }
    }
}

class ServerGUI
{
    public static void main(String A[])
    {
        new ServerLogic("Server", 400, 320);
    }
}
