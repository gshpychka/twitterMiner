import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by glebu on 11-Feb-17.
 */
class TelegramBotThread implements Runnable {
    private Update update;

    TelegramBotThread(Update update) {
        this.update = update;
    }
    public void run() {
        if(update.hasMessage() && update.getMessage().hasText() && !update.getMessage().getText().equals("/start")){
            KeywordAnalyzer keywordAnalyzer;
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId());

            keywordAnalyzer = new KeywordAnalyzer(update.getMessage().getText());
            message.setText("Hold on, working on your query. This may take some time.");
            try {
                Main.bot.sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(keywordAnalyzer);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            message.setText(keywordAnalyzer.getResult());
            try {
                Main.bot.sendMessage(message);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
