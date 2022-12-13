package energie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  private Connection conn;

  public Database(String dbName){
    String user = "root";
    String passwd="root";
    String cString = "jdbc:mysql://localhost:3306/" + dbName + "?user=" + user + "&password="+ passwd;
    try {
      this.conn = DriverManager.getConnection(cString);
    } catch (SQLException e) {
      //e.printStackTrace();
      System.out.println("Kan geen verbinding maken!");
    }
  }

  public Connection getConnection() {
    return conn;
  }
}
