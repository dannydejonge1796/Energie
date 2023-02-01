package energie.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRegister {

  private Database db;

  public CustomerRegister(Database db) {
    this.db = db;
  }

  public Customer getCustomer(String customerNumber)
  {
    String query =
            "SELECT * " +
            "FROM customer " +
            "WHERE number = '" + customerNumber + "' " +
            "LIMIT 1";

    ResultSet result = db.getData(query);

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

  public void addCustomer(Customer customer)
  {
    String number = customer.getCustomerNr();
    String firstname = customer.getFirstname();
    String lastname = customer.getLastname();

    String query =
            "INSERT INTO customer (number, firstname, lastname) " +
            "VALUES ('"+number+"', '"+firstname+"', '"+lastname+"')";

    db.storeData(query);
  }
}