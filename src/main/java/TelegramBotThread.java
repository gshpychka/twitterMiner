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
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId());
            message.setText("In the last hour, "+KeywordAnalyzer.totalAverage+"% of tweets mention impeachment.");
            try {
                Main.bot.sendMessage(message);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
