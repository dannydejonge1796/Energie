package energie.models;

import java.sql.*;

public class Database {

  private final Connection conn;

  public Database(String dbName)
  {
    //Database string aanmaken
    String user = "root";
    String password= "";
    String port = "3306";
    String cString = "jdbc:mysql://localhost:" + port + "/" + dbName + "?user=" + user + "&password=" + password;

    try {
      //Koppel connectie aan class
      this.conn = DriverManager.getConnection(cString);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  //Functie om data op te halen uit de database, met deze functie kan een result set worden verkregen
  public ResultSet getData(String query)
  {
    try {
      Statement stm = this.conn.createStatement();
      return stm.executeQuery(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  //Functie voor het opslaan van data, voert alleen een query uit en returned verder niets
  public void storeData(String query)
  {
    try {
      Statement stm = this.conn.createStatement();
      stm.execute(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
