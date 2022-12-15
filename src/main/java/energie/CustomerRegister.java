package energie;

import energie.screens.Register;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CustomerRegister {

  private ArrayList<Customer> customers = new ArrayList<>();

  public void addCustomer(Customer customer) {
    customers.add(customer);
  }
}
