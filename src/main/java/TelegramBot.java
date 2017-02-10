import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by glebu on 10-Feb-17.
 */
public class TelegramBot extends TelegramLongPollingBot {
    public String getBotToken() {
        return "375446890:AAECbxoa97GN8NjihxhXlfYWKNBjiLpsvHA";
    }

    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            KeywordAnalyzer keywordAnalyzer;
            Thread thread = new Thread(keywordAnalyzer = new KeywordAnalyzer(update.getMessage().getText()));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = keywordAnalyzer.getResult();
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(result);
            try {
                sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return "TwitterMinerBot";
    }
}
