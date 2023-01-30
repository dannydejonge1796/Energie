package energie.models;

import java.sql.*;

public class Database {

  private Connection conn;

  public Database()
  {
    String user = "root";
    String password= "";
    String port = "3306";
    String db = "dossier";
    String cString = "jdbc:mysql://localhost:" + port + "/" + db + "?user=" + user + "&password=" + password;

    try {
      this.conn = DriverManager.getConnection(cString);
    } catch (SQLException e) {
      //e.printStackTrace();
      System.out.println("Kan geen verbinding maken!");
    }
  }

  public ResultSet getData(String query)
  {
    try {
      Statement stm = this.conn.createStatement();
      return stm.executeQuery(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void storeData(String query)
  {
    try {
      Statement stm = this.conn.createStatement();
      stm.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Connection getConn() {
    return conn;
  }
}
