package energie.screens;

import energie.models.Customer;
import energie.models.CustomerRegister;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Random;

public class Register {

  private final Stage stage;
  private final Scene registerScene;
  private final CustomerRegister cR;

  public Register(Stage primaryStage, CustomerRegister customerRegister)
  {
    this.cR = customerRegister;

    primaryStage.setTitle("Energie - registreren");
    this.stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(createRegFormPane());

    this.registerScene = new Scene(border,640, 360);
  }

  private GridPane createRegFormPane()
  {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(40, 100, 40, 100));
    grid.setHgap(10);
    grid.setVgap(10);

    // Header label toevoegen
    Label headerLabel = new Label("Registreren");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.LEFT);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

    // Voornaam label toevoegen
    Label lblFirstname = new Label("Voornaam:");
    grid.add(lblFirstname,0,2);

    // Voornaam textfield toevoegen
    TextField tfFirstname = new TextField();
    GridPane.setHgrow(tfFirstname, Priority.SOMETIMES);
    grid.add(tfFirstname,1,2);

    // Achternaam label toevoegen
    Label lblLastname = new Label("Achternaam:");
    grid.add(lblLastname,0,3);

    // Achternaam textfield toevoegen
    TextField tfLastname = new TextField();
    grid.add(tfLastname,1,3);

    // Voeg submit knop toe
    Button btnBack = new Button("Terug");
    btnBack.setPrefWidth(100);
    grid.add(btnBack, 0, 5, 2, 1);
    GridPane.setHalignment(btnBack, HPos.LEFT);
    GridPane.setMargin(btnBack, new Insets(20, 0,20,0));

    btnBack.setOnAction(e -> this.stage.setScene(new Home(this.stage, cR).getHomeScene()));

    // Voeg submit knop toe
    Button btnRegister = new Button("Registreer");
    btnRegister.setPrefWidth(100);
    grid.add(btnRegister, 0, 5, 2, 1);
    GridPane.setHalignment(btnRegister, HPos.RIGHT);
    GridPane.setMargin(btnRegister, new Insets(20, 0,20,0));

    // Wanneer op knop wordt gedrukt
    btnRegister.setOnAction(e -> {

      String firstname = tfFirstname.getText();
      String lastname = tfLastname.getText();

      if(firstname.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw voornaam in!");
        return;
      }

      if(!firstname.matches("^[a-zA-Z\\s]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "De ingevoerde voornaam is ongeldig!");
        return;
      }

      if(lastname.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw achternaam in!");
        return;
      }

      if(!lastname.matches("^[a-zA-Z\\s]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "De ingevoerde achternaam is ongeldig!");
        return;
      }

      Random rand = new Random();
      StringBuilder randomCustomerNr = new StringBuilder();
      for (int i = 0; i < 3; i++) {
        int randomNumber = rand.nextInt(10);
        randomCustomerNr.append(randomNumber);
      }

      Customer customer = new Customer(randomCustomerNr.toString(), firstname, lastname, null);
      customer.store();

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "U bent geregistreerd " + customer.getFirstname() + " " + customer.getLastname() + "! Uw klantnummer is: " + customer.getCustomerNr() + ".");
      System.out.println("Uw klantnummer is: " + customer.getCustomerNr());
      stage.setScene(new Home(stage, cR).getHomeScene());
    });

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

  public Scene getRegisterScene() {
    return registerScene;
  }
}
