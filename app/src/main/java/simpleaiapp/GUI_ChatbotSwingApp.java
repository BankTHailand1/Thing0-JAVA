package simpleaiapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GUI_ChatbotSwingApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Chatbot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener((ActionEvent e) -> {
            String userMessage = inputField.getText();
            chatArea.append("You: " + userMessage + "\n");

            // Simple chatbot logic
            String botReply = getChatbotReply(userMessage);
            chatArea.append("Bot: " + botReply + "\n");

            inputField.setText("");
        });

        frame.setVisible(true);
    }

    // Chatbot reply logic
    private static String getChatbotReply(String userMessage) {
        if (userMessage.toLowerCase().contains("hello")) {
            return "Hi there! How can I assist you today?";
        } else if (userMessage.toLowerCase().contains("weather")) {
            return "I can't provide the weather right now, but you can check your local forecast!";
        } else if (userMessage.toLowerCase().contains("bye")) {
            return "Goodbye! Have a nice day!";
        } else {
            return "I'm sorry, I don't understand that. Can you rephrase?";
        }
    }
}

