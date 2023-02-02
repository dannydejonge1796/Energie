package energie.models;

import energie.Application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRegister {

  public Customer getCustomer(String customerNumber)
  {
    String query =
            "SELECT * " +
            "FROM customer " +
            "WHERE number = '" + customerNumber + "' " +
            "LIMIT 1";

    ResultSet result = Application.db.getData(query);

    try {
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