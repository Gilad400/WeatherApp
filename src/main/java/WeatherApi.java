
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class WeatherApi {
    private static final String API_KEY = System.getenv("WEATHER_API_KEY");
/**
 * Retrieves hourly temperature data for a specified date and location using the Tomorrow.io API.
 *
 * @param city  The name of the city for which to retrieve weather data.
 * @param year  The year as a string (e.g., "2023").
 * @param month The month as a string (e.g., "08" for August).
 * @param day   The day as a string (e.g., "15" for the 15th).
 * @return A JSONObject containing hourly temperature data.
 * @throws IOException If there is an error in making the request or receiving the response.
 */
    public static JSONObject get_Specific_Weather(String city, String  year, String month, String  day) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject data = new JSONObject();
        data.put("location", city);
        JSONArray fields = new JSONArray();
        fields.put("temperature");
        fields.put("uvIndex");
        data.put("fields", fields);
        data.put("units", "metric");
        JSONArray timesteps = new JSONArray();
        timesteps.put("1h");
        data.put("startTime", year + "-" + month + "-" + day + "T00:00:01Z");
        data.put("endTime", year + "-" + month + "-" + day + "T23:59:59Z");
        data.put("timezone", "Asia/Tel_Aviv");

        // Create the request body
        RequestBody body = RequestBody.create(mediaType, data.toString());
        Request request = new Request.Builder()
                .url("https://api.tomorrow.io/v4/timelines?apikey=" + API_KEY)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        // Send the request and get the response
        Response response = client.newCall(request).execute();
        String responseAsString = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseAsString);

        // Extract the temperature information
        JSONArray intervals = jsonResponse.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals");

        // Create a new JSONObject to hold the temperatures with hours as keys
        JSONObject temperaturesJson = new JSONObject();

        // Iterate over each interval to get the temperature data
        for (int i = 0; i < intervals.length(); i++) {
            JSONObject interval = intervals.getJSONObject(i);
            String time = interval.getString("startTime");

            // Extract only the hour from the time (format: HH:mm)
            String hour = time.substring(11, 16);

            // Get the temperature value
            double temperature = interval.getJSONObject("values").getDouble("temperature");

            // Add to the new JSON object
            temperaturesJson.put(hour, temperature);
        }

        return temperaturesJson;
    }
/**
 * Retrieves the current temperature for a specified location using the Tomorrow.io API.
 *
 * @param city The name of the city for which to retrieve real-time temperature data.
 * @return A string representing the current temperature in Celsius.
 * @throws IOException If there is an error in making the request or receiving the response.
 */
    public static String get_Real_Time_Weather(String city) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject data = new JSONObject();
        data.put("location", city);
        JSONArray fields = new JSONArray();
        fields.put("temperature");
        fields.put("uvIndex");
        data.put("fields", fields);
        data.put("units", "metric");
        JSONArray timesteps = new JSONArray();
        timesteps.put("1h");
        data.put("startTime", "now");
        data.put("endTime", "nowPlus6h");
        data.put("timezone", "Asia/Tel_Aviv");

        // Create the request body
        RequestBody body = RequestBody.create(mediaType, data.toString());
        Request request = new Request.Builder()
                .url("https://api.tomorrow.io/v4/weather/realtime?location=" + data.get("location") + "&apikey=" + API_KEY)
                .get()
                .addHeader("accept", "application/json")
                .build();

        // Send the request and get the response
        Response response = client.newCall(request).execute();
        String responseAsString = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseAsString);
        //Extract the temperature information
        double temperature = jsonResponse.getJSONObject("data").getJSONObject("values").getDouble("temperature");
        return String.valueOf(temperature);
    }
}
