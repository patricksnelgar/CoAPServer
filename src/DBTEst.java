import java.sql.*;

/**
 * Created by Patrick on 24/08/2015.
 */
public class DBTEst {
    static Connection connection;

    public static void main(String[] args) throws Exception{
        Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/wsn_db", "PATRICK", "TAMIYA");
        Statement statement;
        ResultSet res;

        statement = connection.createStatement();
        res = statement.executeQuery("SELECT * FROM NODE_LIST");
        while(res.next()){
            System.out.println(res.getString("N_ID"));
        }
    }
}
