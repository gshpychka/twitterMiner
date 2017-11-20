package telegramBot;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by glebu on 10-Feb-17.
 */
class TelegramBot extends TelegramLongPollingBot {
    public String getBotToken() {
        return "375446890:AAECbxoa97GN8NjihxhXlfYWKNBjiLpsvHA";
    }
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    public void onUpdateReceived(Update update) {
        executor.execute(new TelegramBotThread(update));
    }

    public String getBotUsername() {
        return "TwitterMinerBot";
    }
}
