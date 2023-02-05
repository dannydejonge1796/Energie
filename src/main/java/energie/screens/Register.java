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
    //Koppel customer register aan class
    this.cR = customerRegister;
    //Stel titel van de stage in
    primaryStage.setTitle("Energie - registreren");
    //Koppel stage aan class
    this.stage = primaryStage;
    //Nieuwe borderpane aanmaken
    BorderPane border = new BorderPane();
    //Register form als center instellen
    border.setCenter(createRegFormPane());
    //Koppel register scene aan class
    this.registerScene = new Scene(border,640, 360);
  }

  private GridPane createRegFormPane()
  {
    //Gridpane aanmaken met padding en spacing, content centreren
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(40, 100, 40, 100));
    grid.setHgap(10);
    grid.setVgap(10);
    // Header label aanmaken en toevoegen
    Label headerLabel = new Label("Registreren");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.LEFT);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
    // Voornaam label aanmaken en toevoegen
    Label lblFirstname = new Label("Voornaam:");
    grid.add(lblFirstname,0,2);
    // Voornaam tekstveld aanmaken en toevoegen
    TextField tfFirstname = new TextField();
    GridPane.setHgrow(tfFirstname, Priority.SOMETIMES);
    grid.add(tfFirstname,1,2);
    // Achternaam label aanmaken en toevoegen
    Label lblLastname = new Label("Achternaam:");
    grid.add(lblLastname,0,3);
    // Achternaam tekstveld aanmaken en toevoegen
    TextField tfLastname = new TextField();
    grid.add(tfLastname,1,3);
    //Terug knop aanmaken en toevoegen
    Button btnBack = new Button("Terug");
    btnBack.setPrefWidth(100);
    grid.add(btnBack, 0, 5, 2, 1);
    GridPane.setHalignment(btnBack, HPos.LEFT);
    GridPane.setMargin(btnBack, new Insets(20, 0,20,0));
    //Als er op knop terug wordt geklikt, herlaad de home scene
    btnBack.setOnAction(e -> this.stage.setScene(new Home(this.stage, cR).getHomeScene()));
    //Registreer knop aanmaken en toevoegen
    Button btnRegister = new Button("Registreer");
    btnRegister.setPrefWidth(100);
    grid.add(btnRegister, 0, 5, 2, 1);
    GridPane.setHalignment(btnRegister, HPos.RIGHT);
    GridPane.setMargin(btnRegister, new Insets(20, 0,20,0));
    //Wanneer op registreren wordt geklikt
    btnRegister.setOnAction(e -> {
      //Haal values uit velden op
      String firstname = tfFirstname.getText();
      String lastname = tfLastname.getText();
      //Als veld leeg is, foutmelding
      if(firstname.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw voornaam in!");
        return;
      }
      //Als veld niet voldoet aan regex letters en spaces, foutmelding
      if(!firstname.matches("^[a-zA-Z\\s]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "De ingevoerde voornaam is ongeldig!");
        return;
      }
      //Als veld leeg is, foutmelding
      if(lastname.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw achternaam in!");
        return;
      }
      //Als veld niet voldoet aan regex letters en spaces, foutmelding
      if(!lastname.matches("^[a-zA-Z\\s]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "De ingevoerde achternaam is ongeldig!");
        return;
      }
      //Genereer een random customer nummer
      Random rand = new Random();
      StringBuilder randomCustomerNr = new StringBuilder();
      //Loop 3 keer
      for (int i = 0; i < 3; i++) {
        //Genereer een random nummer 0-9
        int randomNumber = rand.nextInt(10);
        //Nummer toevoegen aan string
        randomCustomerNr.append(randomNumber);
      }
      //Nieuwe customer instantie aanmaken
      Customer customer = new Customer(randomCustomerNr.toString(), firstname, lastname, null);
      //Customer opslaan in database
      customer.store();
      //Succes melding, geeft belangrijk klantnummer weer
      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "U bent geregistreerd " + customer.getFirstname() + " " + customer.getLastname() + "! Uw klantnummer is: " + customer.getCustomerNr() + ".");
      //Klantnummer wordt nogmaals weergegeven in de console.
      System.out.println("Uw klantnummer is: " + customer.getCustomerNr());
      //Herlaad home scene
      stage.setScene(new Home(stage, cR).getHomeScene());
    });
    //Geef gridpane terug
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
