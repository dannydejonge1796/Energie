package energie;

public class Customer {

  private Database database;
  private String customerNr;
  private String firstname;
  private String lastname;
  private Double advance;

  public Customer(String customerNr, String firstname, String lastname, Double advance) {
    this.database = new Database("test");
    this.customerNr = customerNr;
    this.firstname = firstname;
    this.lastname = lastname;
    this.advance = advance;
  }
}
