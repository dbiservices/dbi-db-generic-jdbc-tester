// generic test utility for jdbc connections;
// cec@dbi-services;
// works with any jdbc driver since its name is passed as parameter;
// to compile:
//   javac jdbc_tester_generic.java
// to execute, specify the JDBC jar to use to -cp at run-time; see example below;
import java.sql.DriverManager;
import java.sql.*;
 
public class jdbc_tester_generic {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("The command-line parameters are:");
        for (int i = 0; i < args.length; i++) {
            System.out.println("arg " + i + " = " + args[i]);
        }
 
        if (args.length != 5) {
           System.out.println("Invalid number of arguments: 5 arguments with the following format are required: driver user_name password url sqlquery");
           System.out.println("e.g.:");
           System.out.println("   java -cp postgresql-42.5.1.jar:. jdbc_tester_generic org.postgresql.Driver dmadmin xxx jdbc:postgresql://localhost:5432/mydb \"select 'now in postgresql db: ' || to_char(current_timestamp(0), 'DD/MM/YYYY HH24:MI:SS') as now;\"");
           System.out.println("   java -cp ojdbc.jar:. jdbc_tester_generic oracle.jdbc.driver.OracleDriver dmadmin dmadmin jdbc:oracle:thin:@//db:1521/pdb1 \"select 'now in Oracle db: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') as now from dual\"");
           return;
        }
 
        String driver   = args[0];
        String user     = args[1];
        String password = args[2];
        String url      = args[3];
        String sqlStmt = args[4];
 
        System.out.println("\nLoading driver [" + driver + "]:");
        try {
           Class.forName(driver);
        } catch (ClassNotFoundException e) {
           System.out.println("JDBC Driver [" + driver + " is not found. Please, append it to -cp or CLASSPATH");
           e.printStackTrace();
           System.out.println("Example of driver's syntax: org.postgresql.Driver or oracle.jdbc.driver.OracleDriver");
           return;
        }
        System.out.println("JDBC Driver " + driver + " successfully loaded");
 
        try {
           System.out.println("\nConnecting to url [" + url + "] as user [" + user + "]:");
           Connection conn = DriverManager.getConnection(url, user, password);
           if (conn != null) {
              System.out.println("Successful connection to database");
           }
 
           System.out.println("\nExecuting SQL query [" + sqlStmt + "]:");
           Statement statement = conn.createStatement();
           ResultSet resultSet = statement.executeQuery(sqlStmt);
 
           System.out.println("\nRetrieving the result set:");
           while (resultSet.next()) {
              System.out.println(resultSet.getString(1));
           }
 
           statement.close();
           conn.close();
 
           System.out.println("\nJDBC connection successfully tested, quitting ...");
        } catch (SQLException e) {
           System.out.println("Exception occurred connecting to database: " + e.getMessage());
           e.printStackTrace();
        }
    }
}
