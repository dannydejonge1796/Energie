package energie;

import java.util.ArrayList;

public class CustomerRegister {

  private ArrayList<Customer> customers = new ArrayList<>();

  public void addCustomer(Customer customer) {
    customers.add(customer);
  }

  public ArrayList<Customer> getCustomers() {
    return customers;
  }

  public Customer getCustomer(String customerNumber) {
    for (Customer customer : this.customers) {
      if (customer.getCustomerNr().equals(customerNumber)) {
        return customer;
      }
    }

    return null;
  }
}
