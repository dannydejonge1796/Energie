package energie.screens;

import energie.Customer;
import energie.CustomerRegister;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Home {

  private Stage stage;
  private Scene homeScene;
  private CustomerRegister cR;

  public Home(Stage primaryStage, CustomerRegister customerRegister) {

    this.cR = customerRegister;

    primaryStage.setResizable(false);
    primaryStage.setTitle("Energie - klantnummer invoeren");

    this.stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(addHomePane());

    this.homeScene = new Scene(border,1280, 720);
  }

  public GridPane addHomePane() {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text txtCustomerNr = new Text("Voer uw klantnummer in:");
    txtCustomerNr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(txtCustomerNr, 0, 0);

    TextField tfCustomerNr = new TextField();
    grid.add(tfCustomerNr, 0, 2);

    Button btnCustomerNr = new Button("Ok");

    btnCustomerNr.setOnAction(e -> {
      String customerNr = tfCustomerNr.getText();

      if(customerNr.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een klantnummer in!");
        return;
      }

      if(!customerNr.matches("^[0-9]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde klantnummer is ongeldig!");
        return;
      }

      if (cR.getCustomer(customerNr) != null) {
        Customer customer = cR.getCustomer(customerNr);




        showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "TEMPGelukt");
      } else {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde klantnummer bestaat niet!");
      }
    });

    grid.add(btnCustomerNr, 1, 2);

    Text txtExistingCustomer = new Text("Nog geen klant?");
    txtExistingCustomer.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    grid.add(txtExistingCustomer, 0, 4);

    Button btnRegister = new Button("Registreren");
    btnRegister.setOnAction(e -> stage.setScene(new Register(this.stage, this.cR).getRegisterScene()));
    grid.add(btnRegister, 0, 6);

    return grid;
  }

  public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initOwner(owner);
    alert.show();
  }

  public Scene getHomeScene() {
    return homeScene;
  }
}
