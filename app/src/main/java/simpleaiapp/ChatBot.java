package simpleaiapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ChatBot {

    public static int timeSec = (int)1000.0;
    public static int WaitTime = (int)26.5 * timeSec;

    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);

    static List<String> randomWord = new ArrayList<>();
    static List<String> greetB = new ArrayList<>();
    static List<String> greetP = new ArrayList<>();

    static List<String> chainWord0 = new ArrayList<>();
    static List<String> chainTelling = new ArrayList<>();
    static List<String> chainWord2 = new ArrayList<>();
    static List<String> chainQuestions = new ArrayList<>();
    static List<String> replyNT = new ArrayList<>();
    static List<String> replyChainWord1 = new ArrayList<>();
    static List<String> replyChainWord2 = new ArrayList<>();
    static List<String> replyChainWord3 = new ArrayList<>();

    static List<String> questionLists = Arrays.asList("Where", "Why", "How", "What", "When");

    static List<String> replyYN = Arrays.asList("Yes", "No");
    static List<String> botDKWUS = Arrays.asList("What are u saying?", "The fuck?", "Wha?", "Umm", "You mean something?", "???");
    
    public static void main(String[] args) {
        BotCore();
    }
    
    public static void BotCore() {
        randomWord.add("Hmm");
        randomWord.add("*just waiting for ans from user");
        randomWord.add("*waitng");
        randomWord.add("Boring");
        randomWord.add("*still waiting");
    
        Thread sayRandom = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(WaitTime);
                    String saying = randomWord.get(random.nextInt(randomWord.size()));
                    System.out.println("\nBot(?): " + saying);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        sayRandom.start();
        while (true) {
            System.out.print("You: ");
            String userMessage = scanner.nextLine();
                
    
            if (userMessage.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                sayRandom.interrupt();
                break;
            }

            String botReply = getBotReply(userMessage);
            System.out.println("Bot(?): " + botReply);
        }
    }

    public static String getBotReply(String message) {
        StringBuilder botMessage = new StringBuilder();

        greetP.add("hi");
        greetP.add("hello");
        greetP.add("sup");
        greetP.add("hey");

        greetB.add("Sup!");
        greetB.add("Hello");
        greetB.add("Hi");

        chainQuestions.add(" need any help?");
        chainTelling.add(" some shit happen");
        chainQuestions.add(" are u boring?");

        replyNT.add("Thanks for asking there no need to help");
        replyNT.add("No Thank");
        replyNT.add("No need");
        replyChainWord3.add("Is that so, What kind?");
        replyChainWord1.add("Ok");
        replyChainWord1.add("I see");
        replyChainWord2.add("Really boring");
        replyChainWord2.add("Yes Alot");

        String botGreeting = greetB.get(random.nextInt(greetB.size()));
        boolean greeting = greetP.stream().anyMatch(message.toLowerCase()::contains);

        String botReplyNoThank = replyNT.get(random.nextInt(replyNT.size()));
        boolean cQuestions = chainQuestions.stream().anyMatch(message.toLowerCase()::contains);

        String botDK = botDKWUS.get(random.nextInt(botDKWUS.size()));

        if (greeting) botMessage.append(botGreeting);
        if (cQuestions) {
            if (botMessage.length() > 0) botMessage.append(", ");
            botMessage.append(botReplyNoThank);
        }
        if (botMessage.length() == 0) botMessage.append(botDK);
        return botMessage.toString();
    }
}
