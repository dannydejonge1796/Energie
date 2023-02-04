package energie.screens;

import energie.models.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;

public class Dashboard {

  private Customer customer;
  private Scene dashboardScene;
  private BorderPane borderSettings;
  private ArrayList<Notification> notifications;
  private Stage stage;

  public Dashboard(Stage primaryStage, Customer customer)
  {
    this.stage = primaryStage;
    this.customer = customer;

    primaryStage.setTitle("Energie - Welkom " + this.customer.getFirstname() + "!");

    this.notifications = new ArrayList<>();

    determineNotifications();

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
    selectionModel.select(0);

    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

    this.dashboardScene = new Scene(tabPane, 760, 480);
  }

  public VBox addDashboardPane()
  {
    VBox vBox = new VBox();
    vBox.setPadding(new Insets(20, 20, 20, 20));
    vBox.setSpacing(15);

    Label headerLabel = new Label("Welkom " + customer.getFirstname() + "!");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    vBox.getChildren().add(headerLabel);

    if (!this.notifications.isEmpty()) {
      VBox vboxNotif = new VBox();
      vboxNotif.setPadding(new Insets(5,5,5,5));
      GridPane.setHgrow(vboxNotif, Priority.ALWAYS);
      vboxNotif.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

      for (Notification notification : this.notifications) {
        Text ntfctnTxt = new Text("- " + notification.getTxtNotification());
        ntfctnTxt.setFont(Font.font("Arial", FontWeight.MEDIUM, 12));
        vboxNotif.getChildren().add(ntfctnTxt);
      }

      vBox.getChildren().add(vboxNotif);
    }

    ComboBox<String> comboPeriod = new ComboBox<>();
    comboPeriod.getItems().add("Wekelijks");
    comboPeriod.getItems().add("Maandelijks");
    comboPeriod.getItems().add("Jaarlijks");

    comboPeriod.getSelectionModel().select(0);

    vBox.getChildren().add(comboPeriod);

    VBox vboxTable = new VBox();
    vboxTable.getChildren().add(this.customer.getOverview(comboPeriod.getValue()));

    vBox.getChildren().add(vboxTable);

    comboPeriod.setOnAction(e -> {
      String comboValue = comboPeriod.getValue();
      vboxTable.getChildren().clear();
      vboxTable.getChildren().add(this.customer.getOverview(comboValue));
    });

    return vBox;
  }

  public GridPane addUsagePane()
  {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);

    Label headerLabel = new Label("Voer uw gebruik van de week in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);

    Label lblUsageElec = new Label("Stroomverbruik:");
    grid.add(lblUsageElec, 0, 1);

    TextField tfUsageElec = new TextField();
    GridPane.setHgrow(tfUsageElec, Priority.SOMETIMES);
    grid.add(tfUsageElec, 1, 1);

    Label lblUsageGas = new Label("Gasverbruik:");
    grid.add(lblUsageGas, 0, 2);

    TextField tfUsageGas = new TextField();
    GridPane.setHgrow(tfUsageGas, Priority.SOMETIMES);
    grid.add(tfUsageGas, 1, 2);

    Label lblDateStart = new Label("Startdatum:");
    grid.add(lblDateStart, 0, 3);

    DatePicker dpDateStart = new DatePicker();
    dpDateStart.setValue(LocalDate.now().minusWeeks(1));
    grid.add(dpDateStart, 1, 3);

    ArrayList<WeeklyUsage> weeklyUsages = customer.getWeeklyUsages();

    dpDateStart.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        //Er kan alleen een datum gekozen worden die minimaal 7 dagen geleden is.
        setDisable(empty || date.isAfter(LocalDate.now().minusWeeks(1)));

        for (WeeklyUsage weeklyUsage : weeklyUsages) {
          //Een periode van een wekelijks gebruik kan niet opnieuw worden geselecteerd
          if (!date.isBefore(weeklyUsage.getDateStart()) && !date.isAfter(weeklyUsage.getDateEnd())) {
            setDisable(true);
            break;
          }
        }
      }
    });

    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 4);

    btnSave.setOnAction(e -> {

      String usageElec = tfUsageElec.getText();
      String usageGas = tfUsageGas.getText();
      LocalDate dateStart = dpDateStart.getValue();

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

      if (dateStart.isAfter(LocalDate.now().minusWeeks(1))) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Deze datum is nog niet beschikbaar!");
        return;
      }

      for (WeeklyUsage weeklyUsage : weeklyUsages) {
        if (!dateStart.isBefore(weeklyUsage.getDateStart()) && !dateStart.isAfter(weeklyUsage.getDateEnd())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een wekelijks gebruik ingevoerd op deze datum!");
          return;
        }
      }

      LocalDate dateEnd = dateStart.plusWeeks(1);

      WeeklyUsage weeklyUsage = new WeeklyUsage(this.customer.getCustomerNr(), Integer.parseInt(usageElec), Integer.parseInt(usageGas), dateStart, dateEnd);
      weeklyUsage.store();
      customer.addToWeeklyUsages(weeklyUsage);

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw weekelijks verbruik is opgeslagen!");
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });

    return grid;
  }

  public GridPane addElecRatePane()
  {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);

    Label headerLabel = new Label("Voer het huidige stroomtarief in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);

    Label lblRate = new Label("Tarief stroom:");
    grid.add(lblRate, 0, 1);

    TextField tfRate = new TextField();
    GridPane.setHgrow(tfRate, Priority.SOMETIMES);
    grid.add(tfRate, 1, 1);

    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom, 0, 2);

    DatePicker dpDateFrom = new DatePicker();
    grid.add(dpDateFrom, 1, 2);

    ArrayList<ElectricityRate> electricityRates = customer.getElectricityRates();

    dpDateFrom.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        for (ElectricityRate electricityRate : electricityRates) {
          //Een periode van een elek tarief kan niet opnieuw worden geselecteerd
          if (!date.isBefore(electricityRate.getDateFrom()) && !date.isAfter(electricityRate.getDateTo())) {
            setDisable(true);
            break;
          }
        }
      }
    });

    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo, 0, 3);

    DatePicker dpDateTo =  new DatePicker();
    grid.add(dpDateTo, 1, 3);

    dpDateTo.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        for (ElectricityRate electricityRate : electricityRates) {
          //Een periode van een elek tarief kan niet opnieuw worden geselecteerd
          if (!date.isBefore(electricityRate.getDateFrom()) && !date.isAfter(electricityRate.getDateTo())) {
            setDisable(true);
            break;
          }
        }
      }
    });

    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 4);

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

      for (ElectricityRate electricityRate : electricityRates) {
        if (!dateFrom.isBefore(electricityRate.getDateFrom()) && !dateFrom.isAfter(electricityRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een elek tarief ingevoerd op deze datum!");
          return;
        }
      }

      if (dateTo == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de einddatum van het stroomtarief in!");
        return;
      }

      for (ElectricityRate electricityRate : electricityRates) {
        if (!dateTo.isBefore(electricityRate.getDateFrom()) && !dateTo.isAfter(electricityRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een elek tarief ingevoerd op deze datum!");
          return;
        }
      }

      ElectricityRate electricityRate = new ElectricityRate(this.customer.getCustomerNr(), Double.parseDouble(rate), dateFrom, dateTo);
      electricityRate.store();
      customer.addToElectricityRates(electricityRate);

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw huidige stroomtarief is ingesteld!");
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });

    return grid;
  }

  public GridPane addGasRatePane()
  {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);

    Label headerLabel = new Label("Voer het huidige gas tarief in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);

    Label lblRate = new Label("Tarief gas:");
    grid.add(lblRate, 0, 1);

    TextField tfRate = new TextField();
    GridPane.setHgrow(tfRate, Priority.SOMETIMES);
    grid.add(tfRate, 1, 1);

    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom,0,2);

    DatePicker dpDateFrom = new DatePicker();
    grid.add(dpDateFrom,1,2);

    ArrayList<GasRate> gasRates = customer.getGasRates();

    dpDateFrom.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        for (GasRate gasRate : gasRates) {
          //Een periode van een gas tarief kan niet opnieuw worden geselecteerd
          if (!date.isBefore(gasRate.getDateFrom()) && !date.isAfter(gasRate.getDateTo())) {
            setDisable(true);
            break;
          }
        }
      }
    });

    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo,0,3);

    DatePicker dpDateTo = new DatePicker();
    grid.add(dpDateTo,1,3);

    dpDateTo.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        for (GasRate gasRate : gasRates) {
          //Een periode van een gas tarief kan niet opnieuw worden geselecteerd
          if (!date.isBefore(gasRate.getDateFrom()) && !date.isAfter(gasRate.getDateTo())) {
            setDisable(true);
            break;
          }
        }
      }
    });

    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 4);

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

      for (GasRate gasRate : gasRates) {
        if (!dateFrom.isBefore(gasRate.getDateFrom()) && !dateFrom.isAfter(gasRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een gas tarief ingevoerd op deze datum!");
          return;
        }
      }

      if (dateTo == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de einddatum van het gastarief in!");
        return;
      }

      for (GasRate gasRate : gasRates) {
        if (!dateTo.isBefore(gasRate.getDateFrom()) && !dateTo.isAfter(gasRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een gas tarief ingevoerd op deze datum!");
          return;
        }
      }

      GasRate gasRate = new GasRate(this.customer.getCustomerNr(), Double.parseDouble(rate), dateFrom, dateTo);
      gasRate.store();
      customer.addToGasRates(gasRate);

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw huidige gastarief is ingesteld!");
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });

    return grid;
  }

  public GridPane addAdvancePane()
  {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);

    Label headerLabel = new Label("Voer uw jaarlijkse voorschot in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);

    Label lblAdvance = new Label("Jaarlijks voorschot:");
    grid.add(lblAdvance, 0, 1);

    DecimalFormat df = getDecimalFormat();
    Double txtAdvance = customer.getAdvance();

    TextField tfAdvance = new TextField(txtAdvance != null ? df.format(txtAdvance) : "");
    GridPane.setHgrow(tfAdvance, Priority.SOMETIMES);

    grid.add(tfAdvance, 1, 1);

    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 2);

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
      this.customer.update();

      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw jaarlijkse voorschot is ingesteld!");
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });

    return grid;
  }

  public VBox addVBox()
  {
    VBox vbox = new VBox();
    vbox.setPadding(new Insets(20,20,20,10));
    vbox.setSpacing(12);
    vbox.setStyle("-fx-border-color: lightgray");

    Text title = new Text("Instellingen");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    vbox.getChildren().add(title);

    Hyperlink hyperOne = new Hyperlink("Wekelijks gebruik");
    VBox.setMargin(hyperOne, new Insets(0, 0, 0, 8));
    hyperOne.setOnAction(e -> this.borderSettings.setCenter(addUsagePane()));

    Hyperlink hyperTwo = new Hyperlink("Stroom tarief");
    VBox.setMargin(hyperTwo, new Insets(0, 0, 0, 8));
    hyperTwo.setOnAction(e -> this.borderSettings.setCenter(addElecRatePane()));

    Hyperlink hyperThree = new Hyperlink("Gas tarief");
    VBox.setMargin(hyperThree, new Insets(0, 0, 0, 8));
    hyperThree.setOnAction(e -> this.borderSettings.setCenter(addGasRatePane()));

    Hyperlink hyperFour = new Hyperlink("Jaarlijks voorschot");
    VBox.setMargin(hyperFour, new Insets(0, 0, 0, 8));
    hyperFour.setOnAction(e -> this.borderSettings.setCenter(addAdvancePane()));

    vbox.getChildren().addAll(hyperOne, hyperTwo, hyperThree, hyperFour);

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

  private DecimalFormat getDecimalFormat()
  {
    DecimalFormat df = new DecimalFormat("#.00");
    DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
    sym.setDecimalSeparator('.');
    df.setDecimalFormatSymbols(sym);
    return df;
  }

  public Scene getDashboardScene() {
    return dashboardScene;
  }

  public void determineNotifications() {

    LocalDate now = LocalDate.now();

    Double advance = customer.getAdvance();
    if (advance <= 0) {
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

    if (customer.getWeeklyUsages().isEmpty()) {
      String txtNotification = "U heeft deze week nog geen wekelijks verbruik ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    } else {
      WeeklyUsage current = customer.getCurrentWeeklyUsage();
      if (current == null) {
        String txtNotification = "U heeft deze week nog geen wekelijks verbruik ingevoerd!";
        Notification notification = new Notification(txtNotification);
        notifications.add(notification);
      }
    }
  }
}
