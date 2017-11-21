import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.servlet.ServletHolder;
//import telegramBot.TelegramBot;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Created by glebu on 01-Feb-17.
 * The main class.
 */
public class Main {


  static Logger logger = LogManager.getLogger();

  public static void main(String[] args) throws Exception{

    new TwitterStreamReceiver("Trump");
    logger.trace("Receiver created");

    MyGetServlet getServlet = new MyGetServlet();
    ServletContextHandler context = new ServletContextHandler(1);
    context.addServlet(new ServletHolder(getServlet), "/*");

    Server server = new Server(8080);
    server.setHandler(context);
    server.start();
    logger.trace("server started");

    //AverageCalculator averageCalculator = new AverageCalculator("impeach");
    //averageCalculator.populateDataPoints(60);
    //averageCalculator.correctCorruptTimestamps();
    //ApiContextInitializer.init();
    //        TelegramBotsApi botsApi = new TelegramBotsApi();
    //        bot = new telegramBot.TelegramBot();
    //        try {
    //            botsApi.registerBot(bot);
    //        } catch (TelegramApiRequestException e) {
    //            e.printStackTrace();
    //        }
    //new Thread(new KeywordAnalyzer("impeach")).start();
  }
}
