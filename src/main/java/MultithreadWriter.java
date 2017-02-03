import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by glebu on 03-Feb-17.
 */
class MultithreadWriter {
    private String keyword;
    private DatabaseWriter databaseWriter;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

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
                    statement.execute();
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("\nDuplicate entry");
                    //System.out.println("Statement was: " + statement.toString() + "\n");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try { statement.close(); } catch (SQLException e) {}
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
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("\nDuplicate entry in Batch task\n");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try { statement.close();} catch (SQLException e) {}
                }
            }
        };
        return task;
    }
}
