package simpleaiapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ChatbotWithLWJGL {

    public static int timerSec = 1000;
    public static int waitingTime = 20 * timerSec;
    
    public static void main(String[] args) {
        // Start LWJGL in a new thread
        Thread graphicsThread = new Thread(() -> new LWJGLApp().run());
        graphicsThread.start();
    
        // Start chatbot logic
        startChatbot();
    }
    
        private static void startChatbot() {
            List<String> randomSayings = new ArrayList<>();
            randomSayings.add("Thinking about the meaning of life...");
            randomSayings.add("Did you know? The earth is spinning at 1,600 km/h!");
            randomSayings.add("I'm just here waiting for your next question.");
            randomSayings.add("Sometimes I wonder if robots dream of electric sheep.");
            randomSayings.add("What should we talk about next? You decide!");
            randomSayings.add("Why do programmers prefer dark mode? Because light attracts bugs!");
    
            Random random = new Random();
            Scanner scanner = new Scanner(System.in);
    
            System.out.println("Welcome to your chatbot with 3D graphics!");
            System.out.println("Type 'exit' to quit.\n");
    
            Thread randomSayingsThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(waitingTime); // Wait 5 seconds between random sayings
                    String saying = randomSayings.get(random.nextInt(randomSayings.size()));
                    System.out.println("\nBot (random thought): " + saying);
                } catch (InterruptedException e) {
                    break; // Stop the thread if interrupted
                }
            }
        });
        randomSayingsThread.start();

        while (true) {
            System.out.print("You: ");
            String userMessage = scanner.nextLine();

            if (userMessage.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                randomSayingsThread.interrupt(); // Stop random sayings
                break;
            }

            String botReply = getChatbotReply(userMessage);
            System.out.println("Bot: " + botReply);
        }
    }

    private static String getChatbotReply(String message) {
        // Basic chatbot logic
        if (message.toLowerCase().contains("hello")) {
            return "Hi there! How can I assist you today?";
        } else if (message.toLowerCase().contains("3d")) {
            return "The 3D graphics window is up and running!";
        } else if (message.toLowerCase().contains("how are you")) {
            return "I'm just a bot, but thanks for asking! How are you?";
        }
        return "I'm not sure how to respond to that, but I'm here to help!";
    }
}

