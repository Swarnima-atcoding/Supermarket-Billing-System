
import database.DBConnection;
import java.sql.Connection;

public class TestDBConnection {

    public static void main(String[] args) {

        Connection connection = DBConnection.getConnection();

        if (connection != null) {
            System.out.println("JDBC Connection Test Passed!");

            try {
                connection.close();
                System.out.println("Database Connection Closed!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("JDBC Connection Test Failed!");
        }
    }
}
