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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;

public class Dashboard {

  private final Customer customer;
  private final Scene dashboardScene;
  private final BorderPane borderSettings;
  private final ArrayList<Notification> notifications;
  private final Stage stage;

  public Dashboard(Stage primaryStage, Customer customer)
  {
    //Koppel stage aan class
    this.stage = primaryStage;
    //Koppel de aangemelde customer aan class
    this.customer = customer;
    //Titel van de stage instellen
    primaryStage.setTitle("Energie - Welkom " + this.customer.getFirstname() + "!");
    //Lijst met notificaties gekoppeld aan class
    this.notifications = new ArrayList<>();
    //Functie om notificaties te bepalen
    determineNotifications();
    //Borderpanes aanmaken
    BorderPane borderDash = new BorderPane();
    BorderPane borderSettings = new BorderPane();
    //Dashboard pane aan center toevoegen
    borderDash.setCenter(addDashboardPane());
    //Borderpane aan class koppelen
    this.borderSettings = borderSettings;
    //Linkermenu aan borderpane toevoegen
    this.borderSettings.setLeft(addVBox());
    //Instelling pane aan center toevoegen
    this.borderSettings.setCenter(addUsagePane());
    //Tabpane aanmaken
    TabPane tabPane = new TabPane();
    //Nieuwe tab maken en dashboard borderpane erin stoppen
    Tab tab1 = new Tab();
    tab1.setText("Dashboard");
    tab1.setContent(borderDash);
    //Nieuwe tab aanmaken
    Tab tab2 = new Tab();
    //Als er geen notificaties zijn
    if (this.notifications.size() == 0) {
      //Titel van tab is "Instellingen"
      tab2.setText("Instellingen");
    } else {
      //Anders "Instellingen" + notificatie count
      tab2.setText("Instellingen (" + this.notifications.size() + ")");
    }
    //Borderpane instellingen toevoegen aan tab
    tab2.setContent(borderSettings);
    //Alle tabs toevoegen an tabPanw
    tabPane.getTabs().addAll(tab1, tab2);
    //Eerste tab als selected instellen
    SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
    selectionModel.select(0);
    //Tabs kunnen niet gesloten worden
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

    tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
      //Als naar tab 1 wordt geschakeld
      if (newTab.equals(tab1)) {
        //Ververs het dashboard
        stage.setScene(new Dashboard(stage, customer).getDashboardScene());
      }
    });

    //Koppel dashboard scene aan class
    this.dashboardScene = new Scene(tabPane, 760, 480);
  }

  private VBox addDashboardPane()
  {
    //VBox aanmaken met padding en spacing
    VBox vBox = new VBox();
    vBox.setPadding(new Insets(20, 20, 20, 20));
    vBox.setSpacing(15);
    //Welkomst header toevoegen met naam van customer
    Label headerLabel = new Label("Welkom " + customer.getFirstname() + " " + customer.getLastname() + "!");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    vBox.getChildren().add(headerLabel);
    //Als er notificaties zijn
    if (!this.notifications.isEmpty()) {
      //Maak vbox aan
      VBox vboxNotif = new VBox();
      vboxNotif.setPadding(new Insets(5,5,5,5));
      //vbox groeit naar maximale h lengte
      GridPane.setHgrow(vboxNotif, Priority.ALWAYS);
      //Rode border
      vboxNotif.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
      //loop alle notificaties uit
      for (Notification notification : this.notifications) {
        //Voef notificatie toe aan vbox
        Text ntfctnTxt = new Text("- " + notification.txtNotification());
        ntfctnTxt.setFont(Font.font("Arial", FontWeight.MEDIUM, 12));
        vboxNotif.getChildren().add(ntfctnTxt);
      }
      //Notificatie box aan dashboard vbox toevoegen
      vBox.getChildren().add(vboxNotif);
    }
    //Combobox om periode te selecteren
    ComboBox<String> comboPeriod = new ComboBox<>();
    comboPeriod.getItems().add("Wekelijks");
    comboPeriod.getItems().add("Maandelijks");
    comboPeriod.getItems().add("Jaarlijks");
    //Standaard selectie instellen
    comboPeriod.getSelectionModel().select(0);
    //Toevoegen aan vbox
    vBox.getChildren().add(comboPeriod);
    //Vbox voor overview tabel aanmaken
    VBox vboxTable = new VBox();
    //Overview tabel toevoegen door method the roepen en period mee te geven
    vboxTable.getChildren().add(this.customer.getOverview(comboPeriod.getValue()));
    //Tabel toevoegen aan vbox
    vBox.getChildren().add(vboxTable);
    //Als een periode wordt geselecteerd
    comboPeriod.setOnAction(e -> {
      //Geef de value van de box
      String comboValue = comboPeriod.getValue();
      //Verwijder de tabel uit de vbox
      vboxTable.getChildren().clear();
      //Nieuwe tabel in de vbox zetten
      vboxTable.getChildren().add(this.customer.getOverview(comboValue));
    });
    //vbox dashboard terug geven
    return vBox;
  }

  private GridPane addUsagePane()
  {
    //Gridpane aanmaken met padding en spacing
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);
    //Header tekst aanmaken en toevoegen
    Label headerLabel = new Label("Voer uw gebruik van de week in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);
    //Label stroomverbruik aanmaken en toevoegen
    Label lblUsageElec = new Label("Stroomverbruik:");
    grid.add(lblUsageElec, 0, 1);
    //Tekstveld voor stroomverbruik aanmaken en toevoegen
    TextField tfUsageElec = new TextField();
    GridPane.setHgrow(tfUsageElec, Priority.SOMETIMES);
    grid.add(tfUsageElec, 1, 1);
    //Label gasverbruik aanmaken en toevoegen
    Label lblUsageGas = new Label("Gasverbruik:");
    grid.add(lblUsageGas, 0, 2);
    //Tekstveld gasverbruik aanmaken en toevoegen
    TextField tfUsageGas = new TextField();
    GridPane.setHgrow(tfUsageGas, Priority.SOMETIMES);
    grid.add(tfUsageGas, 1, 2);
    //Label start datum aanmaken en toevoegen
    Label lblDateStart = new Label("Startdatum:");
    grid.add(lblDateStart, 0, 3);
    //Datepicker start datum aanmaken en toevoegen
    DatePicker dpDateStart = new DatePicker();
    //Standaard waarde is nu min een week
    dpDateStart.setValue(LocalDate.now().minusWeeks(1));
    grid.add(dpDateStart, 1, 3);
    //Alle wekelijkse gebruiken ophalen uit de customer
    ArrayList<WeeklyUsage> weeklyUsages = customer.getWeeklyUsages();
    //Onbeschikbare datums moeten niet gesleecteerd kunnen worden
    dpDateStart.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        //Er kan alleen een datum gekozen worden die minimaal 7 dagen geleden is.
        setDisable(empty || date.isAfter(LocalDate.now().minusWeeks(1)));
        //Loop door al het wekelijks gebruik
        for (WeeklyUsage weeklyUsage : weeklyUsages) {
          //Een periode van een wekelijks gebruik kan niet opnieuw worden geselecteerd
          if (!date.isBefore(weeklyUsage.getDateStart()) && !date.isAfter(weeklyUsage.getDateEnd())) {
            setDisable(true);
            break;
          }
        }
      }
    });
    //Knop voor opslaan aanmaken en toevoegen
    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 4);
    //Tabel met al het wekelijks gebruik ophalen uit customer en toevoegen
    grid.add(customer.getTableUsage(), 0, 5, 2, 1);
    //Als er op opslaan gedrukt wordt
    btnSave.setOnAction(e -> {
      //Values uit de velden halen
      String usageElec = tfUsageElec.getText();
      String usageGas = tfUsageGas.getText();
      LocalDate dateStart = dpDateStart.getValue();
      //Foutmelding als veld leeg is
      if(usageElec.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw stroomverbruik in!");
        return;
      }
      //Foutmelding als veld niet voldoet aan regex, alleen nummers
      if(!usageElec.matches("^[0-9]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde aantal is ongeldig!");
        return;
      }
      //Foutmelding als veld leeg is
      if(usageGas.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw gasverbruik in!");
        return;
      }
      //Foutmelding als veld niet voldoet aan regex, alleen nummers
      if(!usageGas.matches("^[0-9]*$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Het ingevoerde aantal is ongeldig!");
        return;
      }
      //Als er geen datum is ingevoerd, of iets anders dan een datum geef foutmelding
      if (dateStart == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de start datum in!");
        return;
      }
      //Als datum na vandaag min een week is, foutmelding
      if (dateStart.isAfter(LocalDate.now().minusWeeks(1))) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Deze datum is nog niet beschikbaar!");
        return;
      }
      //Loop door alle weekly usage
      for (WeeklyUsage weeklyUsage : weeklyUsages) {
        //Als de ingevoerde startdatum valt in een al bestaande periode weekly usages, foutmelding
        if (!dateStart.isBefore(weeklyUsage.getDateStart()) && !dateStart.isAfter(weeklyUsage.getDateEnd())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een wekelijks gebruik ingevoerd op deze datum!");
          return;
        }
      }
      //Eind datum is de start datum plus een week
      LocalDate dateEnd = dateStart.plusWeeks(1);
      //Nieuwe weekly usage instantie aanmaken
      WeeklyUsage weeklyUsage = new WeeklyUsage(this.customer.getCustomerNr(), Integer.parseInt(usageElec), Integer.parseInt(usageGas), dateStart, dateEnd);
      //Roep functie om de weekly usage in de database op te slaan
      weeklyUsage.store();
      //Weekly usage aan arraylist in customer toevoegen
      customer.addToWeeklyUsages(weeklyUsage);
      //Succes melding
      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw weekelijks verbruik is opgeslagen!");
      //Ververs dashboard
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });
    //Geef de grid terug
    return grid;
  }

  private GridPane addElecRatePane()
  {
    //Grid aanmaken met spacing en padding
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);
    //Header tekst aanmaken en toevoegen
    Label headerLabel = new Label("Voer het huidige stroomtarief in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);
    //Label stroomtarief aanmaken en toevoegen
    Label lblRate = new Label("Tarief stroom:");
    grid.add(lblRate, 0, 1);
    //Tekstveld stroomtarief aanmaken en toevoegen
    TextField tfRate = new TextField();
    GridPane.setHgrow(tfRate, Priority.SOMETIMES);
    grid.add(tfRate, 1, 1);
    //Label datum vanaf aanmaken en toevoegen
    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom, 0, 2);
    //Datepicker datum vanaf aanmaken een toevoegen, standaard waarde vandaag instellen
    DatePicker dpDateFrom = new DatePicker(LocalDate.now());
    grid.add(dpDateFrom, 1, 2);
    //Alle stroomtarieven uit customer halen
    ArrayList<ElectricityRate> electricityRates = customer.getElectricityRates();
    //Onbeschikbare datums moeten niet gesleecteerd kunnen worden
    dpDateFrom.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        //Loop door alle stroomtarieven
        for (ElectricityRate electricityRate : electricityRates) {
          //Als een datum binnen de periode van een stroomtarief valt
          if (!date.isBefore(electricityRate.getDateFrom()) && !date.isAfter(electricityRate.getDateTo())) {
            //Een periode van een elek tarief kan niet opnieuw worden geselecteerd
            setDisable(true);
            break;
          }
        }
      }
    });
    //Label datum tot aanmaken en toevoegen
    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo, 0, 3);
    //Datepicker datum tot aanmaken en toevoegen, standaard waarde is vandaag plus 6 maanden
    DatePicker dpDateTo =  new DatePicker(LocalDate.now().plusMonths(6));
    grid.add(dpDateTo, 1, 3);
    //Onbeschikbare datums moeten niet geselecteerd kunnen worden
    dpDateTo.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        //Door alle stroomtarieven loop en
        for (ElectricityRate electricityRate : electricityRates) {
          //Als een datum binnen de periode van een stroomtarief valt
          if (!date.isBefore(electricityRate.getDateFrom()) && !date.isAfter(electricityRate.getDateTo())) {
            //Een periode van een elek tarief kan niet opnieuw worden geselecteerd
            setDisable(true);
            break;
          }
        }
      }
    });
    //Button opslaan aanmaken en toevoegen
    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 4);
    //Tabel van stroomtarieven ophalen uit customer en toevoegen
    grid.add(customer.getTableElec(), 0, 5, 2, 1);
    //Als op opslaan wordt gedrukt
    btnSave.setOnAction(e -> {
      //Haal values op uit velden
      String rate = tfRate.getText();
      LocalDate dateFrom = dpDateFrom.getValue();
      LocalDate dateTo = dpDateTo.getValue();
      //Als tekstveld leeg is, foutmelding
      if (rate.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw huidige stroomtarief in!");
        return;
      }
      //Als ingevoerde waarde niet voldoet aan regex voor bedrag, foutmelding
      if (!rate.matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een geldig bedrag in!");
        return;
      }
      //Als er geen datum is ingevoerd of iets anders dan een datum, foutmelding
      if (dateFrom == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de ingangsdatum van het stroomtarief in!");
        return;
      }
      //Loop door alle stroomtarieven
      for (ElectricityRate electricityRate : electricityRates) {
        //Als er een datum is ingevoerd die in de periode van een al bestaand stroomtarief valt, foutmelding
        if (!dateFrom.isBefore(electricityRate.getDateFrom()) && !dateFrom.isAfter(electricityRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een elek tarief ingevoerd op deze datum!");
          return;
        }
      }
      //Als er geen datum is ingevoerd of iets anders dan een datum, foutmelding
      if (dateTo == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de einddatum van het stroomtarief in!");
        return;
      }
      //Loop door alle stroomtarieven
      for (ElectricityRate electricityRate : electricityRates) {
        //Als er een datum is ingevoerd die in de periode van een al bestaand stroomtarief valt, foutmelding
        if (!dateTo.isBefore(electricityRate.getDateFrom()) && !dateTo.isAfter(electricityRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een elek tarief ingevoerd op deze datum!");
          return;
        }
      }
      //Als ingevoerde datum eerder is of gelijk is aan startdatum, foutmelding
      if (dateTo.isBefore(dateFrom) || dateTo.isEqual(dateFrom)) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Datum tot is eerder of gelijk aan startdatum!");
        return;
      }
      //Nieuwe instantie van stroomtarief aanmaken
      ElectricityRate electricityRate = new ElectricityRate(this.customer.getCustomerNr(), Double.parseDouble(rate), dateFrom, dateTo);
      //Stroomtarief opslaan in database
      electricityRate.store();
      //Stroomtarief toevoegen aan customer arraylist
      customer.addToElectricityRates(electricityRate);
      //Succes melding
      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw huidige stroomtarief is ingesteld!");
      //Ververs dashboard
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });
    //Geef grid terug
    return grid;
  }

  private GridPane addGasRatePane()
  {
    //Grid aanmaken met spacing en padding
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);
    //Header tekst aanmaken en toevoegen
    Label headerLabel = new Label("Voer het huidige gas tarief in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);
    //Label gastarief aanmaken en toevoegen
    Label lblRate = new Label("Tarief gas:");
    grid.add(lblRate, 0, 1);
    //Tekstveld gastarief aanmaken en toevoegen
    TextField tfRate = new TextField();
    GridPane.setHgrow(tfRate, Priority.SOMETIMES);
    grid.add(tfRate, 1, 1);
    //Label datum vanaf aanmaken en toevoegen
    Label lblDateFrom = new Label("Datum vanaf:");
    grid.add(lblDateFrom,0,2);
    //Datepicker datum vanaf aanmaken een toevoegen, standaard waarde vandaag instellen
    DatePicker dpDateFrom = new DatePicker(LocalDate.now());
    grid.add(dpDateFrom,1,2);
    //Alle gastarieven uit customer halen
    ArrayList<GasRate> gasRates = customer.getGasRates();
    //Onbeschikbare datums moeten niet gesleecteerd kunnen worden
    dpDateFrom.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        //Loop door alle gastarieven
        for (GasRate gasRate : gasRates) {
          //Als een datum binnen de periode van een gastarief valt
          if (!date.isBefore(gasRate.getDateFrom()) && !date.isAfter(gasRate.getDateTo())) {
            //Een periode van een gas tarief kan niet opnieuw worden geselecteerd
            setDisable(true);
            break;
          }
        }
      }
    });
    //Label datum tot aanmaken en toevoegen
    Label lblDateTo = new Label("Datum tot:");
    grid.add(lblDateTo,0,3);
    //Datepicker datum tot aanmaken en toevoegen, standaard waarde is vandaag plus 6 maanden
    DatePicker dpDateTo = new DatePicker(LocalDate.now().plusMonths(6));
    grid.add(dpDateTo,1,3);
    //Onbeschikbare datums moeten niet gesleecteerd kunnen worden
    dpDateTo.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        //Loop door alle gastarieven
        for (GasRate gasRate : gasRates) {
          //Als een datum binnen de periode van een gastarief valt
          if (!date.isBefore(gasRate.getDateFrom()) && !date.isAfter(gasRate.getDateTo())) {
            //Een periode van een gas tarief kan niet opnieuw worden geselecteerd
            setDisable(true);
            break;
          }
        }
      }
    });
    //Button opslaan aanmaken en toevoegen
    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 4);
    //Tabel van gastarieven ophalen uit customer en toevoegen
    grid.add(customer.getTableGas(), 0, 5, 2, 1);
    //Als op opslaan wordt gedrukt
    btnSave.setOnAction(e -> {
      //Values uit de velden ophalen
      String rate = tfRate.getText();
      LocalDate dateFrom = dpDateFrom.getValue();
      LocalDate dateTo = dpDateTo.getValue();
      //Als er geen tarief is ingevoerd, foutmelding
      if (rate.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw huidige gastarief in!");
        return;
      }
      //Als het ingevoerde bedrag niet voldoet aan regex bedrag, foutmelding
      if (!rate.matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een geldig bedrag in!");
        return;
      }
      //Als er geen datum is ingevoerd of iets anders dan een datum, foutmelding
      if (dateFrom == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de ingangsdatum van het gastarief in!");
        return;
      }
      //Loop door alle gastarieven
      for (GasRate gasRate : gasRates) {
        //Als er een datum is ingevoerd die in de periode van een al bestaand gastarief valt, foutmelding
        if (!dateFrom.isBefore(gasRate.getDateFrom()) && !dateFrom.isAfter(gasRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een gas tarief ingevoerd op deze datum!");
          return;
        }
      }
      //Als er geen datum is ingevoerd of iets anders dan een datum, foutmelding
      if (dateTo == null) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer de einddatum van het gastarief in!");
        return;
      }
      //Loop door alle gastarieven
      for (GasRate gasRate : gasRates) {
        //Als er een datum is ingevoerd die in de periode van een al bestaand gastarief valt, foutmelding
        if (!dateTo.isBefore(gasRate.getDateFrom()) && !dateTo.isAfter(gasRate.getDateTo())) {
          showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Er is al een gas tarief ingevoerd op deze datum!");
          return;
        }
      }
      //Als ingevoerde datum eerder is of gelijk is aan startdatum, foutmelding
      if (dateTo.isBefore(dateFrom) || dateTo.isEqual(dateFrom)) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Datum tot is eerder of gelijk aan startdatum!");
        return;
      }
      //Nieuwe instantie van gasTarief aanmaken
      GasRate gasRate = new GasRate(this.customer.getCustomerNr(), Double.parseDouble(rate), dateFrom, dateTo);
      //Gastarief opslaan in database
      gasRate.store();
      //Gastarief toevoegen aan arraylist in customer
      customer.addToGasRates(gasRate);
      //Succes melding
      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw huidige gastarief is ingesteld!");
      //Dashboard verversen
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });
    //Gridpane teruggeven
    return grid;
  }

  private GridPane addAdvancePane()
  {
    //Gridpane aanmaken met padding en spacing
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(15);
    grid.setVgap(25);
    //Header tekst aanmaken en toevoegen
    Label headerLabel = new Label("Voer uw jaarlijkse voorschot in.");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    GridPane.setMargin(headerLabel, new Insets(0,0,10,0));
    grid.add(headerLabel, 0, 0, 2, 1);
    //Label jaarlijks voorschot aanmaken en toevoegen
    Label lblAdvance = new Label("Jaarlijks voorschot:");
    grid.add(lblAdvance, 0, 1);
    //Decimalformat #,## ophalen
    DecimalFormat df = getDecimalFormat();
    //Huidige voorschot ophalen
    Double txtAdvance = customer.getAdvance();
    //Voorschot tekstveld aanmaken en toevoegen, als er een voorschot bestaat als standaard instellen
    TextField tfAdvance = new TextField(txtAdvance != null ? df.format(txtAdvance) : "");
    GridPane.setHgrow(tfAdvance, Priority.SOMETIMES);
    grid.add(tfAdvance, 1, 1);
    //Opslaan knop aanmaken en toevoegen
    Button btnSave = new Button("Opslaan");
    GridPane.setHalignment(btnSave, HPos.RIGHT);
    grid.add(btnSave, 1, 2);
    //Als er op opslaan wordt gedrukt
    btnSave.setOnAction(e -> {
      //Value uit het veld ophalen
      String strAdvance = tfAdvance.getText();
      //Als veld leeg is, foutmelding
      if (strAdvance.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer uw jaarlijkse voorschot in!");
        return;
      }
      //Als ingevoerde bedrag niet voldoet aan regex voor bedrag, foutmelding
      if (!strAdvance.matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Error!", "Voer een geldig bedrag in!");
        return;
      }
      //Voorschot instellen in customer
      this.customer.setAdvance(Double.parseDouble(strAdvance));
      //Update customer in database
      this.customer.update();
      //Succes melding
      showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Success!", "Uw jaarlijkse voorschot is ingesteld!");
      //Dashboard verversen
      stage.setScene(new Dashboard(stage, customer).getDashboardScene());
    });
    //Gridpane teruggeven
    return grid;
  }

  private VBox addVBox()
  {
    //Vbox voor sidemenu aanmaken en toevoegen
    VBox vbox = new VBox();
    vbox.setPadding(new Insets(20,20,20,10));
    vbox.setSpacing(12);
    vbox.setStyle("-fx-border-color: lightgray");
    //Tekst "Instellingen" aanmaken en toevoegen
    Text title = new Text("Instellingen");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    vbox.getChildren().add(title);
    //Hyperlink aanmaken
    Hyperlink hyperOne = new Hyperlink("Wekelijks gebruik");
    VBox.setMargin(hyperOne, new Insets(0, 0, 0, 8));
    //Als op de hyperlink wordt geklikt, stel borderpane center in met gewenste pane
    hyperOne.setOnAction(e -> this.borderSettings.setCenter(addUsagePane()));
    //Hyperlink aanmaken
    Hyperlink hyperTwo = new Hyperlink("Stroom tarief");
    VBox.setMargin(hyperTwo, new Insets(0, 0, 0, 8));
    //Als op de hyperlink wordt geklikt, stel borderpane center in met gewenste pane
    hyperTwo.setOnAction(e -> this.borderSettings.setCenter(addElecRatePane()));
    //Hyperlink aanmaken
    Hyperlink hyperThree = new Hyperlink("Gas tarief");
    VBox.setMargin(hyperThree, new Insets(0, 0, 0, 8));
    //Als op de hyperlink wordt geklikt, stel borderpane center in met gewenste pane
    hyperThree.setOnAction(e -> this.borderSettings.setCenter(addGasRatePane()));
    //Hyperlink aanmaken
    Hyperlink hyperFour = new Hyperlink("Jaarlijks voorschot");
    VBox.setMargin(hyperFour, new Insets(0, 0, 0, 8));
    //Als op de hyperlink wordt geklikt, stel borderpane center in met gewenste pane
    hyperFour.setOnAction(e -> this.borderSettings.setCenter(addAdvancePane()));
    //Alle hyperlinks toevoegen aan het linker menu
    vbox.getChildren().addAll(hyperOne, hyperTwo, hyperThree, hyperFour);
    //vbox teruggeven
    return vbox;
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

  private DecimalFormat getDecimalFormat()
  {
    //Decimalformat twee decimalen aanmaken
    DecimalFormat df = new DecimalFormat("#.00");
    //Gebruik een punt i.p.v komma
    DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
    sym.setDecimalSeparator('.');
    df.setDecimalFormatSymbols(sym);
    //Decimalformat teruggeven
    return df;
  }

  private void determineNotifications()
  {
    //Voorschot ophalen uit customer
    Double advance = customer.getAdvance();
    //Als voorschot kleiner of gelijk is aan 0
    if (advance <= 0) {
      //Notificatie aanmaken en toevoegen aan notificaties
      String txtNotification = "U heeft nog geen jaarlijks voorschot ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    }
    //Als een klant geen stroomtarieven heeft
    if (customer.getElectricityRates().isEmpty()) {
      //Notificatie aanmaken en toevoegen aan notificaties
      String txtNotification = "U heeft nog geen stroomtarief ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    } else {
      //Als een klant wel stroomtarieven heeft, maar deze verlopen zijn
      ElectricityRate current = customer.getCurrentElecRate();
      if (current == null) {
        //Notificatie aanmaken en toevoegen aan notificaties
        String txtNotification = "Uw ingevoerde stroomtarief is verlopen!";
        Notification notification = new Notification(txtNotification);
        notifications.add(notification);
      }
    }
    //Als een klant geen gastarieven heeft
    if (customer.getGasRates().isEmpty()) {
      //Notificatie aanmaken en toevoegen aan notificaties
      String txtNotification = "U heeft nog geen gastarief ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    } else {
      //Als een klant wel gastarieven heeft, maar deze verlopen zijn
      GasRate current = customer.getCurrentGasRate();
      if (current == null) {
        //Notificatie aanmaken en toevoegen aan notificaties
        String txtNotification = "Uw ingevoerde gastarief is verlopen!";
        Notification notification = new Notification(txtNotification);
        notifications.add(notification);
      }
    }
    //Als een klant geen wekelijkse gebruiken heeft
    if (customer.getWeeklyUsages().isEmpty()) {
      //Notificatie aanmaken en toevoegen aan notificaties
      String txtNotification = "U heeft deze week nog geen wekelijks verbruik ingevoerd!";
      Notification notification = new Notification(txtNotification);
      notifications.add(notification);
    } else {
      //Als een klant wel wekelijkse gebruiken heeft, maar deze zijn verlopen
      WeeklyUsage current = customer.getCurrentWeeklyUsage();
      if (current == null) {
        //Notificatie aanmaken en toevoegen aan notificaties
        String txtNotification = "U heeft deze week nog geen wekelijks verbruik ingevoerd!";
        Notification notification = new Notification(txtNotification);
        notifications.add(notification);
      }
    }
  }

  public Scene getDashboardScene() {
    return dashboardScene;
  }
}
