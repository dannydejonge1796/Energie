package energie.models;

import java.sql.*;

public class Database {

  private final Connection conn;

  public Database(String dbName)
  {
    String user = "root";
    String password= "";
    String port = "3306";
    String cString = "jdbc:mysql://localhost:" + port + "/" + dbName + "?user=" + user + "&password=" + password;

    try {
      this.conn = DriverManager.getConnection(cString);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ResultSet getData(String query)
  {
    try {
      Statement stm = this.conn.createStatement();
      return stm.executeQuery(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void storeData(String query)
  {
    try {
      Statement stm = this.conn.createStatement();
      stm.execute(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Connection getConn() {
    return conn;
  }
}
