import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by glebu on 03-Feb-17.
 */
class MultithreadWriter {
    private DatabaseWriter databaseWriter;
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    MultithreadWriter(DatabaseWriter databaseWriter) {
        this.databaseWriter = databaseWriter;
    }

    void executeBatchStatement(PreparedStatement statement) throws SQLException {
        executorService.execute(createBatchTask(statement));
    }

    void executeStatement(PreparedStatement statement) throws SQLException {
        executorService.execute(createSingleTask(statement));
    }

    private Runnable createSingleTask(PreparedStatement statement) {
        Runnable task = new Runnable() {
            public void run(){
                try {
                    statement.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e) {
                    //System.out.println("\nUser already exists. Count : " + ++DatabaseWriter.USER_EXISTS_COUNT);
                    //DatabaseWriter.USER_EXISTS_COUNT++;
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

    private Runnable createBatchTask(PreparedStatement statement) {
        Runnable task = new Runnable(){
            public void run(){
                try {
                    statement.executeBatch();
                    //DatabaseWriter.BATCH_COUNT++;
                } catch (SQLException e) {
                    //System.out.println("========================On writing:===================================\n"+ e.getMessage());
                } finally {
                    try {
                        statement.close();
                        //System.out.println("Statement closed. Counter: " + DatabaseWriter.CONNECTION_CLOSED_COUNT++ + " Total counter: " + DatabaseWriter.TOTAL_COUNT + " Same user count: " + DatabaseWriter.USER_EXISTS_COUNT);
                    } catch (SQLException e) {
                        System.out.println("==========================On closing:===================================\n"+ e.getMessage());
                    }
                }
            }
        };
        return task;
    }
}
