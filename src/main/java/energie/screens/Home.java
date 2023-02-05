package energie.screens;

import energie.models.Customer;
import energie.models.CustomerRegister;
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

  private final Stage stage;
  private final Scene homeScene;
  private final CustomerRegister cR;

  public Home(Stage primaryStage, CustomerRegister customerRegister)
  {
    //Koppel customer register aan c;ass
    this.cR = customerRegister;
    //Stage mag niet in grootte worden veranderd
    primaryStage.setResizable(false);
    //Titel van stage instellen
    primaryStage.setTitle("Energie - klantnummer invoeren");
    //Koppel stage aan class
    this.stage = primaryStage;
    //Borderpane aanmaken
    BorderPane border = new BorderPane();
    //Homepane in de center zetten
    border.setCenter(addHomePane());
    //Home scene koppelen aan de class
    this.homeScene = new Scene(border,640, 360);
  }

  private GridPane addHomePane()
  {
    //Gridpane aanmaken met padding en spacing, content centreren
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 100, 25, 100));
    //Tekst invoeren klantnummer aanmaken en toevoegen
    Text txtCustomerNr = new Text("Voer uw klantnummer in:");
    txtCustomerNr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(txtCustomerNr, 0, 0);
    //Tekstveld voor invoeren van klantnummer aanmaken en toevoegen
    TextField tfCustomerNr = new TextField();
    GridPane.setHgrow(tfCustomerNr, Priority.SOMETIMES);
    grid.add(tfCustomerNr, 0, 2);
    //Button ok aanmaken
    Button btnCustomerNr = new Button("Ok");
    //Als op knop ok wordt geklikt
    btnCustomerNr.setOnAction(e -> {
      //Haal nummer op uit veld
      String customerNr = tfCustomerNr.getText();
      //Als veld leeg is, foutmelding
      if(customerNr.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een klantnummer in!");
        return;
      }
      //Als veld geen nummer is, foutmelding
      if(!customerNr.matches("^[0-9]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde klantnummer is ongeldig!");
        return;
      }
      //Haal customer op uit het customer register
      Customer customer = cR.getCustomer(customerNr);
      //Als er een customer is met het ingevoerde nummer
      if (customer != null) {
        //Ga naar het dashboard van de customer
        stage.setScene(new Dashboard(stage, customer).getDashboardScene());
      } else {
        //Als er geen customer is gevonden, foutmelding
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde klantnummer bestaat niet!");
      }
    });
    //Ok knop toevoegen
    grid.add(btnCustomerNr, 1, 2);
    //tekst "nog geen klant?" aanmaken en toevoegen
    Text txtExistingCustomer = new Text("Nog geen klant?");
    txtExistingCustomer.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    grid.add(txtExistingCustomer, 0, 4);
    //Registreerknop aanmaken
    Button btnRegister = new Button("Registreren");
    //Als er op de registreerknop wordt gedrukt, ga naar het registratieformulier
    btnRegister.setOnAction(e -> stage.setScene(new Register(this.stage, this.cR).getRegisterScene()));
    //Button toevoegen
    grid.add(btnRegister, 0, 6);
    //Gridpane home teruggeven
    return grid;
  }

  private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
    //Nieuwe alert aanmaken
    Alert alert = new Alert(alertType);
    //Titel instellen
    alert.setTitle(title);
    //Header instellen
    alert.setHeaderText(null);
    //Gewenste bericht instellen
    alert.setContentText(message);
    //Instellen bij welke window de alert hoort
    alert.initOwner(owner);
    //Weergeef functie
    alert.show();
  }

  public Scene getHomeScene() {
    return homeScene;
  }
}
