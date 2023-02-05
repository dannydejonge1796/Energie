package energie.models;

import energie.Application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRegister {

  public Customer getCustomer(String customerNumber)
  {
    //Query om een customer op te halen op basis van het customer nummer
    String query =
            "SELECT * " +
            "FROM customer " +
            "WHERE number = '" + customerNumber + "' " +
            "LIMIT 1";
    //Haal de resultaten uit de database
    ResultSet result = Application.db.getData(query);

    try {
      //Als er een customer is gevonden, return een nieuwe instantie van customer
      if (result.next()) {
        return new Customer(
                result.getString("number"),
                result.getString("firstname"),
                result.getString("lastname"),
                (double) result.getFloat("advance")
        );
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return null;
  }
}