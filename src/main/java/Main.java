
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import twitter4j.*;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by glebu on 01-Feb-17.
 * The main class.
 */
public class Main {
    public static void main (String[] args) throws TwitterException, IOException {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        TwitterApiToken token = new TwitterApiToken();
        new TwitterStreamReceiver(token, new DatabaseWriter(),"Trump");
        new TwitterStreamReceiver(token, new DatabaseWriter(), "Bannon");
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
