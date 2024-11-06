import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatGptApi {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    // Use of Environment Variable
    private static final String API_KEY = System.getenv("CHAT_GPT_API_KEY");

/**
 * Sends a text input to the OpenAI GPT-4 model via an API request and returns a JSON response
 * containing the city and date extracted from the input text.
 *
 * @param freeText The input text from which the city, day, and month are to be extracted. This text
 *                 should contain the relevant information for identifying a city and an exact date.
 * @return A JSONObject containing the extracted data.
 * @throws IOException If there is an error in making the request or receiving the response.
 */
    public static JSONObject chatGpt(String freeText) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject data = new JSONObject();

        data.put("model", "gpt-4");
        JSONArray messagesArray = new JSONArray();

        // Add system message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        messagesArray.put(systemMessage);

        // Add user message
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "Extract the city and exact date (day and month as Strings) from the following text." +
                "Return the result as a JSONObject with keys: city, day, and month, where 'day' and 'month' are numerical values for the exact calendar date." +
                "Text: " + freeText);
        messagesArray.put(userMessage);

        data.put("messages", messagesArray);

        // Create the request body
        RequestBody body = RequestBody.create(
                data.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        // Send the request and get the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Return the response body
            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse;
        }
    }
}
