package energie.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRegister {

  private Database db;

  public CustomerRegister(Database db) {
    this.db = db;
  }

  public Customer getCustomer(Integer customerNumber)
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
                result.getInt("number"),
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
    Integer number = customer.getCustomerNr();
    String firstname = customer.getFirstname();
    String lastname = customer.getLastname();
    float advance = customer.getAdvance().floatValue();

    String query =
            "INSERT INTRO customer ('number', 'firstname', 'lastname', 'advance') " +
            "VALUES ('"+number+"', '"+firstname+"', '"+lastname+"', '"+advance+"')";
  }
}