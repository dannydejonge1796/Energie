package energie.screens;

import energie.*;
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

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Dashboard {

  private Customer customer;
  private Scene dashboardScene;
  private BorderPane borderSettings;
  private Integer notificationCount;
  private Stage stage;
  private Integer selectedTab;

  public Dashboard(Stage primaryStage, Customer customer, Integer selectedTab, String settingCenterPaneIdx) {
    this.stage = primaryStage;
    this.customer = customer;
    this.selectedTab = selectedTab;

    GridPane settingCenterPane = new GridPane();
    switch (settingCenterPaneIdx) {
      case "usage" -> settingCenterPane = addUsagePane();
      case "elecRate" -> settingCenterPane = addElecRatePane();
      case "gasRate" -> settingCenterPane = addGasRatePane();
      case "advance" -> settingCenterPane = addAdvancePane();
    }

    primaryStage.setTitle("Energie - Welkom " + this.customer.getFirstname() + "!");

    BorderPane borderDash = new BorderPane();
    BorderPane borderSettings = new BorderPane();

    borderDash.setCenter(addDashboardPane());

    this.borderSettings = borderSettings;
    this.borderSettings.setLeft(addVBox());
    this.borderSettings.setCenter(addUsagePane());

    TabPane tabPane = new TabPane();

    Tab tab1 = new Tab();
    tab1.setText("Dashboard");
    tab1.setContent(borderDash);

    Tab tab2 = new Tab();

    this.notificationCount = getNotifications();

    if (this.notificationCount == 0) {
      tab2.setText("Instellingen");
    } else {
      tab2.setText("Instellingen (" + this.notificationCount + ")");
    }

    tab2.setContent(borderSettings);

    tabPane.getTabs().addAll(tab1, tab2);

    SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
    selectionModel.select(this.selectedTab);

    if (this.selectedTab == 1) {
      this.borderSettings.setCenter(settingCenterPane);
    }

    this.dashboardScene = new Scene(tabPane,640, 360);
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

      WeeklyUsage weeklyUsage = new WeeklyUsage(Double.parseDouble(usageElec), Double.parseDouble(usageGas), dateStart, dateEnd);
      customer.addToWeeklyUsages(weeklyUsage);

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw weekelijks verbruik is opgeslagen!");
      stage.setScene(new Dashboard(stage, customer, 1, "usage").getDashboardScene());
    });

    return grid;
  }

  public GridPane addElecRatePane() {
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

    Label lblRate = new Label("Tarief stroom:");
    grid.add(lblRate,0,2);
    TextField tfRate = new TextField();
    tfRate.setPrefHeight(40);
    grid.add(tfRate,1,2);

    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom,0,3);
    DatePicker dpDateFrom = new DatePicker();
    dpDateFrom.setPrefHeight(40);
    grid.add(dpDateFrom,1,3);

    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo,0,4);
    DatePicker dpDateTo = new DatePicker();
    dpDateTo.setPrefHeight(40);
    grid.add(dpDateTo,1,4);

    // Voeg submit knop toe
    Button btnSave = new Button("Opslaan");
    btnSave.setPrefHeight(40);
    btnSave.setPrefWidth(100);
    grid.add(btnSave, 0, 5, 2, 1);
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    GridPane.setMargin(btnSave, new Insets(20, 0,20,0));

    btnSave.setOnAction(e -> {

      String rate = tfRate.getText();
      LocalDate dateFrom = dpDateFrom.getValue();
      LocalDate dateTo = dpDateTo.getValue();

      if (rate.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw huidige stroomtarief in!");
        return;
      }

      if (!rate.matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een geldig bedrag in!");
        return;
      }

      if (dateFrom == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de ingangsdatum van het stroomtarief in!");
        return;
      }

      if (dateTo == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de einddatum van het stroomtarief in!");
        return;
      }

      ElectricityRate electricityRate = new ElectricityRate(Double.parseDouble(rate), dateFrom, dateTo);
      customer.addToElectricityRates(electricityRate);

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw huidige stroomtarief is ingesteld!");
      stage.setScene(new Dashboard(stage, customer, 1, "elecRate").getDashboardScene());
    });

    return grid;
  }

  public GridPane addGasRatePane() {
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

    Label lblRate = new Label("Tarief gas:");
    grid.add(lblRate,0,2);
    TextField tfRate = new TextField();
    tfRate.setPrefHeight(40);
    grid.add(tfRate,1,2);

    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom,0,3);
    DatePicker dpDateFrom = new DatePicker();
    dpDateFrom.setPrefHeight(40);
    grid.add(dpDateFrom,1,3);

    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo,0,4);
    DatePicker dpDateTo = new DatePicker();
    dpDateTo.setPrefHeight(40);
    grid.add(dpDateTo,1,4);

    // Voeg submit knop toe
    Button btnSave = new Button("Opslaan");
    btnSave.setPrefHeight(40);
    btnSave.setPrefWidth(100);
    grid.add(btnSave, 0, 5, 2, 1);
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    GridPane.setMargin(btnSave, new Insets(20, 0,20,0));

    btnSave.setOnAction(e -> {

      String rate = tfRate.getText();
      LocalDate dateFrom = dpDateFrom.getValue();
      LocalDate dateTo = dpDateTo.getValue();

      if (rate.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw huidige gastarief in!");
        return;
      }

      if (!rate.matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een geldig bedrag in!");
        return;
      }

      if (dateFrom == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de ingangsdatum van het gastarief in!");
        return;
      }

      if (dateTo == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de einddatum van het gastarief in!");
        return;
      }

      GasRate gasRate = new GasRate(Double.parseDouble(rate), dateFrom, dateTo);
      customer.addToGasRates(gasRate);

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw huidige gastarief is ingesteld!");
      stage.setScene(new Dashboard(stage, customer, 1, "gasRate").getDashboardScene());
    });

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

    TextField tfAdvance;

    Double txtAdvance = customer.getAdvance();

    if (txtAdvance == null) {
      tfAdvance = new TextField();
    } else {
      DecimalFormat df = new DecimalFormat("#.00");
      tfAdvance = new TextField(df.format(txtAdvance));
    }

    tfAdvance.setPrefHeight(40);
    grid.add(tfAdvance,1,1);

    // Voeg submit knop toe
    Button btnSave = new Button("Opslaan");
    btnSave.setPrefHeight(40);
    btnSave.setPrefWidth(100);
    grid.add(btnSave, 0, 5, 2, 1);
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    GridPane.setMargin(btnSave, new Insets(20, 0,20,0));

    btnSave.setOnAction(e -> {

      String advance = tfAdvance.getText();

      if (advance.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw jaarlijkse voorschot in!");
        return;
      }

      if (!advance.matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een geldig bedrag in!");
        return;
      }

      this.customer.setAdvance(Double.parseDouble(advance));

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw jaarlijkse voorschot is ingesteld!");
      stage.setScene(new Dashboard(stage, customer, 1, "advance").getDashboardScene());
    });

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
    options[1].setOnAction(e -> {this.borderSettings.setCenter(addElecRatePane());});
    options[2].setOnAction(e -> {this.borderSettings.setCenter(addGasRatePane());});
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

  public Integer getNotifications() {

    LocalDate now = LocalDate.now();

    Integer notificationCount = 0;

    if (customer.getAdvance() == null) {
      notificationCount = notificationCount + 1;
    }

    ArrayList<ElectricityRate> electricityRates = customer.getElectricityRates();

    if (electricityRates.isEmpty()) {
      notificationCount = notificationCount + 1;
    } else {
      int lastIdx = electricityRates.size() - 1;
      ElectricityRate latestElecRate = electricityRates.get(lastIdx);
      LocalDate dateTo = latestElecRate.getDateTo();
      if (dateTo.isBefore(now)) {
        notificationCount = notificationCount + 1;
      }
    }

    ArrayList<GasRate> gasRates = customer.getGasRates();

    if (customer.getGasRates().isEmpty()) {
      notificationCount = notificationCount + 1;
    } else {
      int lastIdx = gasRates.size() - 1;
      GasRate latestGasRate = gasRates.get(lastIdx);
      LocalDate dateTo = latestGasRate.getDateTo();
      if (dateTo.isBefore(now)) {
        notificationCount = notificationCount + 1;
      }
    }

    ArrayList<WeeklyUsage> weeklyUsages = customer.getWeeklyUsages();

    if (customer.getWeeklyUsages().isEmpty()) {
      notificationCount = notificationCount + 1;
    } else {
      int lastIdx = weeklyUsages.size() - 1;
      WeeklyUsage weeklyUsage = weeklyUsages.get(lastIdx);
      LocalDate dateEnd = weeklyUsage.getDateEnd();
      if (dateEnd.isBefore(now)) {
        notificationCount = notificationCount + 1;
      }
    }

    return notificationCount;
  }
}
