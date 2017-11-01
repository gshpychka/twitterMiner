
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;

/**
 * Created by glebu on 01-Feb-17.
 * The main class.
 */
public class Main {

    static TelegramBot bot;
    static Logger logger = LogManager.getLogger();
    public static void main (String[] args) throws TwitterException, IOException {
        //new TwitterStreamReceiver("Trump");
        AverageCalculator averageCalculator = new AverageCalculator("impeach");
        averageCalculator.populateDataPoints(60);
        //averageCalculator.correctCorruptTimestamps();
        //ApiContextInitializer.init();
//        TelegramBotsApi botsApi = new TelegramBotsApi();
//        bot = new TelegramBot();
//        try {
//            botsApi.registerBot(bot);
//        } catch (TelegramApiRequestException e) {
//            e.printStackTrace();
//        }
        //new Thread(new KeywordAnalyzer("impeach")).start();
    }
}
