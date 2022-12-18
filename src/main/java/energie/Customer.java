package energie;

import java.util.Random;

public class Customer {

//  private Database database;
  private String customerNr;
  private String firstname;
  private String lastname;
  private Double advance;

  public Customer(String firstname, String lastname) {
//    this.database = new Database("test");

    Random rand = new Random();
    StringBuilder randomCustomerNr = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      int randomNumber = rand.nextInt(10);
      randomCustomerNr.append(randomNumber);
    }

    System.out.println(randomCustomerNr);

    this.customerNr = randomCustomerNr.toString();
    this.firstname = firstname;
    this.lastname = lastname;
    this.advance = null;
  }

  public String getCustomerNr() {
    return customerNr;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public Double getAdvance() {
    return advance;
  }

  public void setAdvance(Double advance) {
    this.advance = advance;
  }
}
