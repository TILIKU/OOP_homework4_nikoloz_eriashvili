import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExchangeRateService {
    private static final String API_KEY = "2d58e15efe12911f05637271"; // Replace with your API key

    public String getExchangeRateData(String currency) throws Exception {
        String urlString = String.format("https://v6.exchangerate-api.com/v6/%s/latest/GEL", API_KEY);
        String jsonResponse = fetchDataFromAPI(urlString);

        // Manually parse the JSON data
        if(currency.equalsIgnoreCase("all")){
            String exchangeRate = allExchangeRate(jsonResponse);
            return exchangeRate;
        }else{
            String exchangeRate = extractRate(jsonResponse, currency.toUpperCase());
            return exchangeRate;
        }
    }

    private String fetchDataFromAPI(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            return content.toString();
        } else {
            throw new Exception("Failed to fetch data from API. HTTP response code: " + responseCode);
        }
    }

    private static String allExchangeRate(String json){
        // Find the "conversion_rates" section
        int startIndex =json.indexOf("\"conversion_rates\":") + "\"conversion_rates\":".length();
        int endIndex = json.indexOf("}", startIndex);
        String conversionRatesString = json.substring(startIndex, endIndex);

        // Extract specific conversion rates for USD, EUR, and GBP
        String usdRate = extractRate(conversionRatesString, "USD");
        String eurRate = extractRate(conversionRatesString, "EUR");
        String gbpRate = extractRate(conversionRatesString, "GBP");

        // Print the extracted rates
        StringBuilder sb = new StringBuilder();
        sb.append("USD Conversion Rate: ").append(usdRate).append("\n");
        sb.append("EUR Conversion Rate: ").append(eurRate).append("\n");
        sb.append("GBP Conversion Rate: ").append(gbpRate);

        // Assign the combined string to a variable
        String combinedRates = sb.toString();

        return combinedRates;
    }
    // Method to extract the rate for a given currency code from the conversion rates string
    private static String extractRate(String conversionRatesString, String currencyCode) {
        // Find the start index of the currency code
        String searchKey = "\"" + currencyCode + "\":";
        int startIndex = conversionRatesString.indexOf(searchKey) + searchKey.length();

        // Find the end index of the rate value (comma or end of string)
        int endIndex = conversionRatesString.indexOf(",", startIndex);
        if (endIndex == -1) { // If comma is not found, this is the last element
            endIndex = conversionRatesString.length();
        }

        // Extract the rate value as a string
        String rateString = conversionRatesString.substring(startIndex, endIndex).trim();

        // Convert the extracted string to a double
        return rateString;
    }
}
