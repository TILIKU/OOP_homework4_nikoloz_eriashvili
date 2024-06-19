import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WeatherService {

    private static final String API_KEY = "bd26ada2e35853054179289226f0a0b8\n"; // Replace with your actual API key
    private String latitude = "41.542894744908224";
    private String longtitue = "45.002636525603805";

    // Method to fetch weather data using latitude and longitude
    public String getWeatherData(double latitude, double longitude) throws Exception {
        // Construct the API request URL with latitude, longitude, and API key
        String urlString = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                latitude, longitude, API_KEY);
        // Fetch the JSON data from the API
        String jsonResponse = fetchDataFromAPI(urlString);

        // Manually parse the JSON data
        String cityName = extractValue(jsonResponse, "\"name\":\"", "\"");
        String temperature = extractValue(jsonResponse, "\"temp\":", ",");
        String weatherDescription = extractValue(jsonResponse, "\"description\":\"", "\"");
        String humidity = extractValue(jsonResponse, "\"humidity\":", "},");
        String windSpeed = extractValue(jsonResponse, "\"speed\":", ",");

        // Format and return the weather data
        return String.format("City: %s\nTemperature: %.2f°C\nDescription: %s\nHumidity: %s%%\nWind Speed: %s m/s",
                cityName, (Double.parseDouble(temperature) - 273.15), weatherDescription, humidity, windSpeed);
    }

    // Helper method to fetch data from the API
    private String fetchDataFromAPI(String urlString) throws Exception {
        // Create a URL object from the URL string
        URL url = new URL(urlString);
        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set the request method to GET
        connection.setRequestMethod("GET");

        // Get the response code from the server
        int responseCode = connection.getResponseCode();
        // If the response code is HTTP_OK (200), read the response
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Create a BufferedReader to read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            // Read each line of the response and append to the StringBuilder
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Close the BufferedReader and disconnect the connection
            in.close();
            connection.disconnect();

            // Return the content as a string
            return content.toString();
        } else {
            // If the response code is not HTTP_OK, throw an exception
            throw new Exception("Failed to fetch data from API. HTTP response code: " + responseCode);
        }
    }

    // Utility method to extract values between specific markers
    private String extractValue(String json, String key, String delimiter) {
        int startIndex = json.indexOf(key) + key.length();
        int endIndex = json.indexOf(delimiter, startIndex);
        return json.substring(startIndex, endIndex);
    }

    // Main method to test the WeatherService class
    public static void main(String[] args) {
        try {
            // Create an instance of WeatherService
            WeatherService weatherService = new WeatherService();
            // Fetch and display the weather data for specific coordinates (e.g., Rustavi, Georgia)
            String weatherData = weatherService.getWeatherData(41.5428, 45.0025); // Example coordinates for Rustavi, Georgia
            System.out.println(weatherData);
        } catch (Exception e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }
}
