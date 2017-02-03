import twitter4j.Status;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by glebu on 03-Feb-17.
 */
public class RetweetWriter {
    Queue<Status> retweetsQueue = new LinkedList<>();

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private String keyword;
    private DatabaseWriter databaseWriter;
    ExecutorService executorService = Executors.newFixedThreadPool(8);

    void write() {
        executorService.execute(new Runnable() {
            public void run() {
                    int i=0;
                while(!retweetsQueue.isEmpty()){
                    i++;
                    databaseWriter.writeRetweet(retweetsQueue.remove(), keyword);
                    System.out.println("======================================================================\nFLUSHING OUT RETWEETS, i=" + i);
                }
//                try {
//                    this.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }


    public RetweetWriter(DatabaseWriter databaseWriter) {
        this.databaseWriter = databaseWriter;
    }
}
