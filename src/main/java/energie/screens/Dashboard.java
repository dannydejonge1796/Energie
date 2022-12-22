package energie.screens;

import energie.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Dashboard {

  private Customer customer;
  private Scene dashboardScene;
  private BorderPane borderSettings;
  private ArrayList<Notification> notifications;
  private Stage stage;
  private Integer selectedTab;

  public Dashboard(Stage primaryStage, Customer customer, Integer selectedTab, String settingCenterPaneIdx) {

    this.stage = primaryStage;
    this.customer = customer;

    primaryStage.setTitle("Energie - Welkom " + this.customer.getFirstname() + "!");

    this.selectedTab = selectedTab;
    this.notifications = new ArrayList<>();

    determineNotifications();

    GridPane settingCenterPane = new GridPane();
    switch (settingCenterPaneIdx) {
      case "usage" -> settingCenterPane = addUsagePane();
      case "elecRate" -> settingCenterPane = addElecRatePane();
      case "gasRate" -> settingCenterPane = addGasRatePane();
      case "advance" -> settingCenterPane = addAdvancePane();
    }

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

    if (this.notifications.size() == 0) {
      tab2.setText("Instellingen");
    } else {
      tab2.setText("Instellingen (" + this.notifications.size() + ")");
    }

    tab2.setContent(borderSettings);

    tabPane.getTabs().addAll(tab1, tab2);

    SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
    selectionModel.select(this.selectedTab);

    if (this.selectedTab == 1) {
      this.borderSettings.setCenter(settingCenterPane);
    }

    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

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

    Label ntfctnHdrLabel = new Label("Notificaties");
    ntfctnHdrLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));
    grid.add(ntfctnHdrLabel, 0,1,2,1);
    GridPane.setHalignment(ntfctnHdrLabel, HPos.LEFT);

    int count = 2;
    for (Notification notification : this.notifications) {
      Text ntfctnTxt = new Text(notification.getTxtNotification());

      ntfctnTxt.setFont(Font.font("Arial", FontWeight.MEDIUM, 12));
      ntfctnTxt.setStyle("-fx-text-fill: red");
      grid.add(ntfctnTxt, 0, count,2,1);
      GridPane.setHalignment(ntfctnTxt, HPos.LEFT);
      count++;
    }

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

    ArrayList<WeeklyUsage> weeklyUsages = customer.getWeeklyUsages();
    int latestIdx = weeklyUsages.size() - 1;
    WeeklyUsage latestWeeklyUsage = latestIdx >= 0 ? weeklyUsages.get(latestIdx) : null;

    Label lblUsageElec = new Label("Stroomverbruik:");
    grid.add(lblUsageElec,0,1);
    TextField tfUsageElec = new TextField(latestWeeklyUsage != null ? latestWeeklyUsage.getUsageElec().toString() : "");
    tfUsageElec.setPrefHeight(40);
    grid.add(tfUsageElec,1,1);

    Label lblUsageGas = new Label("Gasverbruik:");
    grid.add(lblUsageGas,0,2);
    TextField tfUsageGas = new TextField(latestWeeklyUsage != null ? latestWeeklyUsage.getUsageGas().toString() : "");
    tfUsageGas.setPrefHeight(40);
    grid.add(tfUsageGas,1,2);

    Label lblDateStart = new Label("Startdatum:");
    grid.add(lblDateStart,0,3);
    DatePicker dpDateStart;
    if (latestWeeklyUsage != null) {
      dpDateStart = new DatePicker(latestWeeklyUsage.getDateStart());
    } else {
      dpDateStart = new DatePicker();
    }
    dpDateStart.setPrefHeight(40);
    grid.add(dpDateStart,1,3);

    Label lblDateEnd = new Label("Einddatum:");
    grid.add(lblDateEnd,0,4);
    DatePicker dpDateEnd;
    if (latestWeeklyUsage != null) {
      dpDateEnd = new DatePicker(latestWeeklyUsage.getDateEnd());
    } else {
      dpDateEnd = new DatePicker();
    }
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

      WeeklyUsage weeklyUsage = new WeeklyUsage(Integer.parseInt(usageElec), Integer.parseInt(usageGas), dateStart, dateEnd);
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

    ArrayList<ElectricityRate> electricityRates = customer.getElectricityRates();
    int latestIdx = electricityRates.size() - 1;
    ElectricityRate latestElecRate = latestIdx >= 0 ? electricityRates.get(latestIdx) : null;

    Label lblRate = new Label("Tarief stroom:");
    grid.add(lblRate,0,2);

    DecimalFormat df = new DecimalFormat("#.00");
    DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
    sym.setDecimalSeparator('.');
    df.setDecimalFormatSymbols(sym);

    TextField tfRate = new TextField(latestElecRate != null ? df.format(latestElecRate.getRate()) : "");
    tfRate.setPrefHeight(40);
    grid.add(tfRate,1,2);

    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom,0,3);
    DatePicker dpDateFrom;
    if (latestElecRate != null) {
      dpDateFrom = new DatePicker(latestElecRate.getDateFrom());
    } else {
      dpDateFrom = new DatePicker();
    }
    dpDateFrom.setPrefHeight(40);
    grid.add(dpDateFrom,1,3);

    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo,0,4);
    DatePicker dpDateTo;
    if (latestElecRate != null) {
      dpDateTo = new DatePicker(latestElecRate.getDateTo());
    } else {
      dpDateTo = new DatePicker();
    }
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

    ArrayList<GasRate> gasRates = customer.getGasRates();
    int latestIdx = gasRates.size() - 1;
    GasRate latestGasRate = latestIdx >= 0 ? gasRates.get(latestIdx) : null;

    Label lblRate = new Label("Tarief gas:");
    grid.add(lblRate,0,2);

    DecimalFormat df = new DecimalFormat("#.00");
    DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
    sym.setDecimalSeparator('.');
    df.setDecimalFormatSymbols(sym);

    TextField tfRate = new TextField(latestGasRate != null ? df.format(latestGasRate.getRate()) : "");
    tfRate.setPrefHeight(40);
    grid.add(tfRate,1,2);

    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom,0,3);
    DatePicker dpDateFrom;
    if (latestGasRate != null) {
      dpDateFrom = new DatePicker(latestGasRate.getDateFrom());
    } else {
      dpDateFrom = new DatePicker();
    }
    dpDateFrom.setPrefHeight(40);
    grid.add(dpDateFrom,1,3);

    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo,0,4);
    DatePicker dpDateTo;
    if (latestGasRate != null) {
      dpDateTo = new DatePicker(latestGasRate.getDateTo());
    } else {
      dpDateTo = new DatePicker();
    }
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
    grid.add(lblAdvance,0,2);

    // Jaarlijks voorschot textfield toevoegen

    TextField tfAdvance;

    Double txtAdvance = customer.getAdvance();

    if (txtAdvance == null) {
      tfAdvance = new TextField();
    } else {
      DecimalFormat df = new DecimalFormat("#.00");
      DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
      sym.setDecimalSeparator('.');
      df.setDecimalFormatSymbols(sym);

      tfAdvance = new TextField(df.format(txtAdvance));
    }

    tfAdvance.setPrefHeight(40);
    grid.add(tfAdvance,1,2);

    // Voeg submit knop toe
    Button btnSave = new Button("Opslaan");
    btnSave.setPrefHeight(40);
    btnSave.setPrefWidth(100);
    grid.add(btnSave, 0, 5, 2, 1);
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    GridPane.setMargin(btnSave, new Insets(20, 0,20,0));

    btnSave.setOnAction(e -> {

      String strAdvance = tfAdvance.getText();

      if (strAdvance.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw jaarlijkse voorschot in!");
        return;
      }

      if (!strAdvance.matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een geldig bedrag in!");
        return;
      }

      this.customer.setAdvance(Double.parseDouble(strAdvance));

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

  public void determineNotifications() {

    LocalDate now = LocalDate.now();

    Double advance = customer.getAdvance();
    if (advance == null) {
      String txtNotification = "U heeft nog geen jaarlijks voorschot ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    }

    ArrayList<ElectricityRate> electricityRates = customer.getElectricityRates();
    if (electricityRates.isEmpty()) {
      String txtNotification = "U heeft nog geen stroomtarief ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    } else {
      int lastIdx = electricityRates.size() - 1;
      ElectricityRate latestElecRate = electricityRates.get(lastIdx);
      LocalDate dateTo = latestElecRate.getDateTo();
      if (dateTo.isBefore(now)) {
        String txtNotification = "De einddatum van uw stroomtarief is verlopen!";
        Notification notification = new Notification(txtNotification);
        notifications.add(notification);
      }
    }

    ArrayList<GasRate> gasRates = customer.getGasRates();
    if (customer.getGasRates().isEmpty()) {
      String txtNotification = "U heeft nog geen gastarief ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    } else {
      int lastIdx = gasRates.size() - 1;
      GasRate latestGasRate = gasRates.get(lastIdx);
      LocalDate dateTo = latestGasRate.getDateTo();
      if (dateTo.isBefore(now)) {
        String txtNotification = "De einddatum van uw gastarief is verlopen!";
        Notification notification = new Notification(txtNotification);
        notifications.add(notification);
      }
    }

    ArrayList<WeeklyUsage> weeklyUsages = customer.getWeeklyUsages();
    if (customer.getWeeklyUsages().isEmpty()) {
      String txtNotification = "U heeft deze week nog geen wekelijks verbruik ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    } else {
      int lastIdx = weeklyUsages.size() - 1;
      WeeklyUsage weeklyUsage = weeklyUsages.get(lastIdx);
      LocalDate dateEnd = weeklyUsage.getDateEnd();
      if (dateEnd.isBefore(now)) {
        String txtNotification = "U heeft deze week nog geen wekelijks verbruik ingevoerd!";
        Notification notification = new Notification(txtNotification);
        notifications.add(notification);
      }
    }
  }
}
