/*
Client Features (Current Version)

1. GUI-based chat client using Java Swing.
2. JTextArea with JScrollPane to maintain full chat history.
3. Auto-scroll enabled to always display latest messages.
4. Send message using:
   - SEND button
   - Enter key (ActionListener on text field)
5. Connects to server using Socket (localhost:5200).
6. Uses background threads for:
   - Server connection
   - Receiving messages (prevents GUI freezing)
7. Displays connection status (connected, failed, disconnected, errors).
8. Client can send messages to server.
9. Closes connection and exits when "end" message is sent.
*/


import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

class ClientLogic implements ActionListener
{
    // GUI Components
    JFrame fobj;
    JButton bobj;
    JLabel messagelabel;
    JTextField tobj;
    JTextArea chatArea;
    JScrollPane scroll;

    // Networking
    Socket sobj;
    PrintStream pobj;
    BufferedReader bobj1;

    public ClientLogic(String title, int width, int height)
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

        // Connect to server in background
        new Thread(() -> {
            try {
                sobj = new Socket("localhost", 5200);
                addMessage("Connected to Server!");

                pobj = new PrintStream(sobj.getOutputStream());
                bobj1 = new BufferedReader(new InputStreamReader(sobj.getInputStream()));

                new Thread(() -> receiveMessages()).start();

            } catch (IOException e) {
                addMessage("Connection failed: " + e.getMessage());
            }
        }).start();
    }

    // Add message to chat history
    private void addMessage(String text)
    {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(text + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    // Receive messages
    private void receiveMessages()
    {
        try {
            String msg;
            while (bobj1 != null && (msg = bobj1.readLine()) != null) {
                addMessage("Server: " + msg);
            }
            addMessage("Server disconnected.");
        } catch (IOException e) {
            addMessage("Connection closed.");
        } finally {
            try { if (sobj != null && !sobj.isClosed()) sobj.close(); } catch (IOException ex) {}
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
                    SwingUtilities.invokeLater(() -> fobj.dispose());
                }
            } else {
                addMessage("Not connected to server.");
            }
        } catch (Exception e) {
            addMessage("Error sending message.");
        }
    }
}

class ClientGUI
{
    public static void main(String A[])
    {
        new ClientLogic("Client", 400, 320);
    }
}
