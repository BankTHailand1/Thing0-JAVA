package advApp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatbotOutsideData {
//    private static final String MAIN = "src/main/resource";
    private static final String BOT_MEMERY = "botMemery.json";
    public static InputStream getFile() {
        return ChatbotOutsideData.class.getResourceAsStream(BOT_MEMERY);
    }

    private static JSONObject chatbotData = new JSONObject();
    
    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);
    
    public ChatbotOutsideData() {
        chatbotData = loadChatbotData();
    }

    public static void main(String[] args) {
        botCore();
    }
    
    public static void botCore() {
        while (true) { 
            System.out.print("You: ");
            String userMessage = scanner.nextLine();
    
            String getBotReply = getBotReply(userMessage);
            System.out.println("Bot(?): " + getBotReply);
        }
    }
                
    public static String getBotReply(String message) {
        StringBuilder bot = new StringBuilder();
    
        List<String> greet = chatbotData.has("greeting") ? IntStream.range(0, chatbotData.getJSONArray("greeting").length()).mapToObj(i -> chatbotData.getJSONArray("greeting").getString(i)).collect(Collectors.toList()) : Arrays.asList("hi", "hello");
    
        boolean greeting = greet.stream().anyMatch(message.toLowerCase()::contains);
    
        if (greeting) bot.append(greet.get(random.nextInt(greet.size())));
    
        if (bot.length() == 0) {
            saveNewData(message);
            return "IDK";
        }
                
        return bot.toString();
    }
                
    private JSONObject loadChatbotData() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(BOT_MEMERY)));
            return new JSONObject(content);
        } catch (IOException | JSONException e) {
            return new JSONObject();
        }
    }
                
    private static void saveNewData(String message) {
        try {
            JSONArray unknownMessage = chatbotData.has("unknown") ? chatbotData.getJSONArray("unknown") : new JSONArray();
            unknownMessage.put(message);
            chatbotData.put("unknown", unknownMessage);
            Files.write(Paths.get(BOT_MEMERY), chatbotData.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}