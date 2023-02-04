package energie.screens;

import energie.models.Customer;
import energie.models.CustomerRegister;
import energie.models.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Home {

  private Stage stage;
  private Scene homeScene;
  private CustomerRegister cR;

  public Home(Stage primaryStage, CustomerRegister customerRegister)
  {
    this.cR = customerRegister;

    primaryStage.setResizable(false);
    primaryStage.setTitle("Energie - klantnummer invoeren");

    this.stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(addHomePane());

    this.homeScene = new Scene(border,640, 360);
  }

  public GridPane addHomePane()
  {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 100, 25, 100));

    Text txtCustomerNr = new Text("Voer uw klantnummer in:");
    txtCustomerNr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(txtCustomerNr, 0, 0);

    TextField tfCustomerNr = new TextField();
    GridPane.setHgrow(tfCustomerNr, Priority.SOMETIMES);
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

      Customer customer = cR.getCustomer(customerNr);
      if (customer != null) {
        stage.setScene(new Dashboard(stage, customer).getDashboardScene());
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
