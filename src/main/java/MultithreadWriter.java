import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by glebu on 03-Feb-17.
 */
class MultithreadWriter {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    MultithreadWriter(){}

    void executeStatement(PreparedStatement statement) throws SQLException {
        executorService.execute(createSingleTask(statement));
    }

    private Runnable createSingleTask(PreparedStatement statement) {
        Runnable task = new Runnable() {
            public void run(){
                try {
                    statement.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e) {
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        statement.close();
                    } catch (SQLException e) {}
                }
            }
        };
        return task;
    }

}
