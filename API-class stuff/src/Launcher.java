public class Launcher {
    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        Chatbot chatbot = new Chatbot(weatherService, exchangeRateService);

        chatbot.start();
    }
}
