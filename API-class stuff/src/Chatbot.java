import java.util.Scanner;

public class Chatbot {
    private WeatherService weatherService;
    private ExchangeRateService exchangeRateService;

    public Chatbot(WeatherService weatherService, ExchangeRateService exchangeRateService) {
        this.weatherService = weatherService;
        this.exchangeRateService = exchangeRateService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! I'm your API chatbot. What information would you like?");
        System.out.println("You can ask me about the current weather or exchange rates.");

        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().toLowerCase();

            try {
                if (userInput.contains("weather")) {
                    System.out.println("Chatbot: Please enter the latitude and longitude (comma-separated, e.g., 40.7128, -74.0060):");
                    String[] coordinates = scanner.nextLine().split(",");
                    double latitude = Double.parseDouble(coordinates[0].trim());
                    double longitude = Double.parseDouble(coordinates[1].trim());
                    String weatherData = weatherService.getWeatherData(latitude, longitude);
                    System.out.println("Chatbot: " + weatherData);
                } else if (userInput.contains("exchange rate")) {
                    System.out.println("Chatbot:type ONE for one currency rate or ALL to display full information:");
                    String response = scanner.nextLine().toLowerCase();
                    if(response.equalsIgnoreCase("all")){
                        String exchangeRateData = exchangeRateService.getExchangeRateData(response);
                        System.out.println("Chatbot: " + exchangeRateData);
                    }else if(response.equalsIgnoreCase("one")){
                        System.out.println("Chatbot: Please enter the currency code (USD, EUR,GBP):");
                        String currency = scanner.nextLine().toUpperCase();
                        String exchangeRateData = exchangeRateService.getExchangeRateData(currency);
                        System.out.println("Chatbot: " + exchangeRateData);
                    }else{
                        System.out.println("Chatbot:Wrong request, please try again");
                    }
                } else if (userInput.equals("exit")) {
                    System.out.println("Chatbot: Goodbye!");
                    break;
                } else {
                    System.out.println("Chatbot: I'm sorry, I can only provide information on the weather and exchange rates.");
                    System.out.println("If you want to exit, type 'exit'.");
                }
            } catch (Exception e) {
                System.out.println("Chatbot: An error occurred: " + e.getMessage());
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        // Create instances of WeatherService and ExchangeRateService
        WeatherService weatherService = new WeatherService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();

        // Create an instance of Chatbot
        Chatbot chatbot = new Chatbot(weatherService, exchangeRateService);

        // Start the chatbot
        chatbot.start();
    }
}
