package energie.screens;

import energie.Customer;
import energie.CustomerRegister;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Dashboard {

  private Customer customer;
  private Scene dashboardScene;
  private BorderPane borderSettings;

  public Dashboard(Stage primaryStage, Customer customer) {
    this.customer = customer;
    primaryStage.setTitle("Energie - Welkom " + this.customer.getFirstname() + "!");

    BorderPane borderDash = new BorderPane();
    borderDash.setCenter(addDashboardPane());

    BorderPane borderSettings = new BorderPane();
    this.borderSettings = borderSettings;
    this.borderSettings.setLeft(addVBox());
    this.borderSettings.setCenter(addUsagePane());

    TabPane tabPane = new TabPane();

    Tab tab1 = new Tab();
    tab1.setText("Dashboard");
    tab1.setContent(borderDash);

    Tab tab2 = new Tab();
    tab2.setText("Instellingen");
    tab2.setContent(borderSettings);

    tabPane.getTabs().addAll(tab1, tab2);

    this.dashboardScene = new Scene(tabPane,1280, 720);
  }

  public GridPane addDashboardPane() {
    // Nieuwe gridpane aanmaken
    GridPane grid = new GridPane();
    // Gridpane in het midden van het scherm positioneren
    grid.setAlignment(Pos.CENTER);
    // Padding van 20px aan elke kant
    grid.setPadding(new Insets(40, 40, 40, 40));
    // Horizontale witregel tussen columns
    grid.setHgap(10);
    // Verticale witregel tussen rows
    grid.setVgap(10);

    // Header label toevoegen
    Label headerLabel = new Label("Welkom " + customer.getFirstname() + "!");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));



    return grid;
  }

  public GridPane addUsagePane() {
    // Nieuwe gridpane aanmaken
    GridPane grid = new GridPane();
    // Gridpane in het midden van het scherm positioneren
    grid.setAlignment(Pos.CENTER);
    // Padding van 20px aan elke kant
    grid.setPadding(new Insets(40, 40, 40, 40));
    // Horizontale witregel tussen columns
    grid.setHgap(10);
    // Verticale witregel tussen rows
    grid.setVgap(10);

    // Header label toevoegen
    Label headerLabel = new Label("Voer uw gebruik van de week in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

    Label lblUsageElec = new Label("Stroomverbruik:");
    grid.add(lblUsageElec,0,1);
    TextField tfUsageElec = new TextField();
    tfUsageElec.setPrefHeight(40);
    grid.add(tfUsageElec,1,1);

    Label lblUsageGas = new Label("Gasverbruik:");
    grid.add(lblUsageGas,0,2);
    TextField tfUsageGas = new TextField();
    tfUsageGas.setPrefHeight(40);
    grid.add(tfUsageGas,1,2);

    Label lblDateStart = new Label("Startdatum:");
    grid.add(lblDateStart,0,3);
    DatePicker dpDateStart = new DatePicker();
    dpDateStart.setPrefHeight(40);
    grid.add(dpDateStart,1,3);

    Label lblDateEnd = new Label("Einddatum:");
    grid.add(lblDateEnd,0,4);
    DatePicker dpDateEnd = new DatePicker();
    dpDateEnd.setPrefHeight(40);
    grid.add(dpDateEnd,1,4);

    // Voeg submit knop toe
    Button btnSave = new Button("Opslaan");
    btnSave.setPrefHeight(40);
    btnSave.setPrefWidth(100);
    grid.add(btnSave, 0, 5, 2, 1);
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    GridPane.setMargin(btnSave, new Insets(20, 0,20,0));

    btnSave.setOnAction(e -> {

      String usageElec = tfUsageElec.getText();
      String usageGas = tfUsageGas.getText();
      LocalDate dateStart = dpDateStart.getValue();
      LocalDate dateEnd = dpDateEnd.getValue();

      if(usageElec.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw stroomverbruik in!");
        return;
      }
      if(!usageElec.matches("^[0-9]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde aantal is ongeldig!");
        return;
      }

      if(usageGas.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw gasverbruik in!");
        return;
      }
      if(!usageGas.matches("^[0-9]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde aantal is ongeldig!");
        return;
      }

      if (dateStart == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de start datum in!");
        return;
      }

      if(dateEnd == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de eind datum in!");
        return;
      }

    });

    return grid;
  }

  public GridPane addCostElecPane() {
// Nieuwe gridpane aanmaken
    GridPane grid = new GridPane();
    // Gridpane in het midden van het scherm positioneren
    grid.setAlignment(Pos.CENTER);
    // Padding van 20px aan elke kant
    grid.setPadding(new Insets(40, 40, 40, 40));
    // Horizontale witregel tussen columns
    grid.setHgap(10);
    // Verticale witregel tussen rows
    grid.setVgap(10);

    // Header label toevoegen
    Label headerLabel = new Label("Voer het huidige stroomtarief in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

    Label lblCostKwh = new Label("Tarief stroom:");
    grid.add(lblCostKwh,0,2);
    TextField tfCostKwh = new TextField();
    tfCostKwh.setPrefHeight(40);
    grid.add(tfCostKwh,1,2);

    Label lblKwhDateFrom = new Label("Datum vanaf:");
    grid.add(lblKwhDateFrom,0,3);
    DatePicker dpKwhDateFrom = new DatePicker();
    dpKwhDateFrom.setPrefHeight(40);
    grid.add(dpKwhDateFrom,1,3);

    Label lblKwhDateTo = new Label("Datum tot:");
    grid.add(lblKwhDateTo,0,4);
    DatePicker dpKwhDateTo = new DatePicker();
    dpKwhDateTo.setPrefHeight(40);
    grid.add(dpKwhDateTo,1,4);

    // Voeg submit knop toe
    Button btnRegister = new Button("Opslaan");
    btnRegister.setPrefHeight(40);
    btnRegister.setPrefWidth(100);
    grid.add(btnRegister, 0, 5, 2, 1);
    GridPane.setHalignment(btnRegister, HPos.RIGHT);
    GridPane.setMargin(btnRegister, new Insets(20, 0,20,0));

    return grid;
  }

  public GridPane addCostGasPane() {
// Nieuwe gridpane aanmaken
    GridPane grid = new GridPane();
    // Gridpane in het midden van het scherm positioneren
    grid.setAlignment(Pos.CENTER);
    // Padding van 20px aan elke kant
    grid.setPadding(new Insets(40, 40, 40, 40));
    // Horizontale witregel tussen columns
    grid.setHgap(10);
    // Verticale witregel tussen rows
    grid.setVgap(10);

    // Header label toevoegen
    Label headerLabel = new Label("Voer het huidige gas tarief in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

    Label lblCostGas = new Label("Tarief gas:");
    grid.add(lblCostGas,0,2);
    TextField tfCostGas = new TextField();
    tfCostGas.setPrefHeight(40);
    grid.add(tfCostGas,1,2);

    Label lblGasDateFrom = new Label("Datum vanaf:");
    grid.add(lblGasDateFrom,0,3);
    DatePicker dpGasDateFrom = new DatePicker();
    dpGasDateFrom.setPrefHeight(40);
    grid.add(dpGasDateFrom,1,3);

    Label lblGasDateTo = new Label("Datum tot:");
    grid.add(lblGasDateTo,0,4);
    DatePicker dpGasDateTo = new DatePicker();
    dpGasDateTo.setPrefHeight(40);
    grid.add(dpGasDateTo,1,4);

    // Voeg submit knop toe
    Button btnRegister = new Button("Opslaan");
    btnRegister.setPrefHeight(40);
    btnRegister.setPrefWidth(100);
    grid.add(btnRegister, 0, 5, 2, 1);
    GridPane.setHalignment(btnRegister, HPos.RIGHT);
    GridPane.setMargin(btnRegister, new Insets(20, 0,20,0));

    return grid;
  }

  public GridPane addAdvancePane() {
// Nieuwe gridpane aanmaken
    GridPane grid = new GridPane();
    // Gridpane in het midden van het scherm positioneren
    grid.setAlignment(Pos.CENTER);
    // Padding van 20px aan elke kant
    grid.setPadding(new Insets(40, 40, 40, 40));
    // Horizontale witregel tussen columns
    grid.setHgap(10);
    // Verticale witregel tussen rows
    grid.setVgap(10);

    // Header label toevoegen
    Label headerLabel = new Label("Voer uw jaarlijkse voorschot in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

    // Jaarlijks voorschot label toevoegen
    Label lblAdvance = new Label("Jaarlijks voorschot:");
    grid.add(lblAdvance,0,1);

    // Jaarlijks voorschot textfield toevoegen
    TextField tfAdvance = new TextField();
    tfAdvance.setPrefHeight(40);
    grid.add(tfAdvance,1,1);

    // Voeg submit knop toe
    Button btnRegister = new Button("Opslaan");
    btnRegister.setPrefHeight(40);
    btnRegister.setPrefWidth(100);
    grid.add(btnRegister, 0, 5, 2, 1);
    GridPane.setHalignment(btnRegister, HPos.RIGHT);
    GridPane.setMargin(btnRegister, new Insets(20, 0,20,0));

    return grid;
  }

  public VBox addVBox() {
    VBox vbox = new VBox();
    vbox.setPadding(new Insets(10));
    vbox.setSpacing(8);

    Text title = new Text("Instellingen");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    vbox.getChildren().add(title);

    Hyperlink options[] = new Hyperlink[] {
            new Hyperlink("Wekelijks gebruik"),
            new Hyperlink("Stroom tarief"),
            new Hyperlink("Gas tarief"),
            new Hyperlink("Jaarlijks voorschot")};

    for (int i=0; i<4; i++) {
      VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
      vbox.getChildren().add(options[i]);
    }

    options[0].setOnAction(e -> {this.borderSettings.setCenter(addUsagePane());});
    options[1].setOnAction(e -> {this.borderSettings.setCenter(addCostElecPane());});
    options[2].setOnAction(e -> {this.borderSettings.setCenter(addCostGasPane());});
    options[3].setOnAction(e -> {this.borderSettings.setCenter(addAdvancePane());});

    return vbox;
  }

  public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initOwner(owner);
    alert.show();
  }

  public Scene getDashboardScene() {
    return dashboardScene;
  }
}
