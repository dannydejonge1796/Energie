package energie.models;

import energie.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Customer {

  private final String customerNr;
  private final String firstname;
  private final String lastname;
  private Double advance;
  private final ArrayList<Object> rates;
  private ArrayList<WeeklyUsage> weeklyUsages;

  public Customer(String customerNr, String firstname, String lastname, Double advance)
  {
    this.customerNr = customerNr;
    this.firstname = firstname;
    this.lastname = lastname;
    this.advance = advance;
    this.rates = new ArrayList<>();

    //Haal alle stroomtarieven van de klant op en stop ze in de arraylist
    initElectricityRates();
    //Haal alle gastarieven van de klant op en stop ze in de arraylist
    initGasRates();
    //Haal alle wekelijkse gebruiken van de klant op en stop ze in de arraylist
    initWeeklyUsage();
  }

  public TableView<WeeklyUsage> getTableUsage()
  {
    //Maak een nieuwe tabel voor wekelijks energieverbruik
    TableView<WeeklyUsage> table = new TableView<>();
    //Vul de tabel met items uit de wekelijkse energieverbruik lijst
    table.setItems(FXCollections.observableArrayList(this.weeklyUsages));

    //Maak kolommen aan voor alle properties
    //Vul de kolommen met de waarden uit de properties
    TableColumn<WeeklyUsage, String> colUsageElec = new TableColumn<>("Stroom gebruik");
    colUsageElec.setCellValueFactory(new PropertyValueFactory<>("usageElec"));

    TableColumn<WeeklyUsage, String> colUsageGas = new TableColumn<>("Gas gebruik");
    colUsageGas.setCellValueFactory(new PropertyValueFactory<>("usageGas"));

    TableColumn<WeeklyUsage, String> colDateStart = new TableColumn<>("Start datum");
    colDateStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));

    TableColumn<WeeklyUsage, String> colDateEnd = new TableColumn<>("Eind datum");
    colDateEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));

    //Maak een kolom voor deleteknoppen
    TableColumn<WeeklyUsage, Void> deleteCol = new TableColumn<>("Delete");
    //Maak een knop in de cel van de kolom
    deleteCol.setCellFactory(new Callback<>() {
      @Override
      public TableCell<WeeklyUsage, Void> call(final TableColumn<WeeklyUsage, Void> param) {
        return new TableCell<>() {
          private final Button deleteButton = new Button("Delete");
          //Als op de verwijderknop wordt geklikt
          {
            deleteButton.setOnAction((ActionEvent event) -> {
              //Haal het wekelijkse energieverbruik op uit de tabel
              WeeklyUsage weeklyUsage = getTableView().getItems().get(getIndex());
              //Verwijder wekelijks verbruik uit database
              weeklyUsage.destroy();
              //Verwijder wekelijks gebruik uit arraylist van customer
              weeklyUsages.remove(weeklyUsage);
              //Verwijder de row uit de tabel
              table.getItems().remove(weeklyUsage);
            });
          }

          @Override
          //Deze functie houdt de deleteknop kolom up-to-date
          public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              //Als de rij leeg is, verwijder de deleteknop
              setGraphic(null);
            } else {
              //Als de rij niet leeg is, zet dan de deleteknop als het grafische element
              setGraphic(deleteButton);
            }
          }
        };
      }
    });
    //Voeg alle kolommen toe aan de tabel
    table.getColumns().add(colUsageElec);
    table.getColumns().add(colUsageGas);
    table.getColumns().add(colDateStart);
    table.getColumns().add(colDateEnd);
    table.getColumns().add(deleteCol);
    //De kolommen hebben een statische breedte en krijgen dze automatisch
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    table.autosize();
    //Geef de tabel terug
    return table;
  }

  public <T extends Rate> TableView<T> getRateTable(List<T> rates)
  {
    //Maak een nieuwe tabel voor stroomtarief
    TableView<T> table = new TableView<>();
    //Vul de tabel met items uit de lijst met stroomtarieven
    table.setItems(FXCollections.observableArrayList(rates));

    //Maak kolommen aan voor alle properties
    //Vul de kolommen met de waarden uit de properties
    TableColumn<T, String> colRate = new TableColumn<>("Rate");
    colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

    TableColumn<T, String> colDateFrom = new TableColumn<>("Date From");
    colDateFrom.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));

    TableColumn<T, String> colDateTo = new TableColumn<>("Date To");
    colDateTo.setCellValueFactory(new PropertyValueFactory<>("dateTo"));

    //Maak een kolom voor deleteknoppen
    TableColumn<T, Void> deleteCol = new TableColumn<>("Delete");
    //Maak een knop in de cel van de kolom
    deleteCol.setCellFactory(new Callback<>() {
      @Override
      public TableCell<T, Void> call(final TableColumn<T, Void> param) {
        return new TableCell<>() {
          private final Button deleteButton = new Button("Delete");
          //Als op de verwijderknop wordt geklikt
          {
            deleteButton.setOnAction((ActionEvent event) -> {
              //Haal het tarief op uit de tabel
              T rate = getTableView().getItems().get(getIndex());
              //Verwijder het tarief uit de database
              rate.destroy();
              //Verwijder het tarief uit customer
              rates.remove(rate);
              //Verwijder het tarief uit de tabel
              table.getItems().remove(rate);
            });
          }

          @Override
          //Deze functie houdt de deleteknop kolom up-to-date
          public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              //Als de rij leeg is, verwijder de deleteknop
              setGraphic(null);
            } else {
              //Als de rij niet leeg is, zet dan de deleteknop als het grafische element
              setGraphic(deleteButton);
            }
          }
        };
      }
    });
    //Voeg alle kolommen toe aan de tabel
    table.getColumns().add(colRate);
    table.getColumns().add(colDateFrom);
    table.getColumns().add(colDateTo);
    table.getColumns().add(deleteCol);
    //De kolommen hebben een statische breedte en krijgen dze automatisch
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    table.autosize();
    //Geef de tabel terug
    return table;
  }

  public TableView<ObservableList<String>> getOverview(String selectedPeriod)
  {
    int divideAdvance = 0;
    String groupString = "";
    if (selectedPeriod.equals("Wekelijks")) {
      //Haal het resultaat op per week
      divideAdvance = 52;
      //Group by string voor wekelijks
      groupString = "GROUP BY WEEK(weekly_usage.date_start) ";
    }

    if (selectedPeriod.equals("Maandelijks")) {
      //Haal het resultaat op per maand
      divideAdvance = 12;
      //Group by string voor maandelijks
      groupString = "GROUP BY MONTH(weekly_usage.date_start) ";
    }

    if (selectedPeriod.equals("Jaarlijks")) {
      //Haal het resultaat op per jaar
      divideAdvance = 1;
      //Group by string voor jaarlijks
      groupString = "GROUP BY YEAR(weekly_usage.date_start) ";
    }

    String query =
    "SELECT " +
    //Kolom periode
    "CONCAT(MIN(weekly_usage.date_start), ' - ', MAX(weekly_usage.date_end)) as Period, " +
    //Kolom elek verbruik
    "SUM(weekly_usage.usage_elec) as Total_elec_usage, " +
    //Kolom gas verbruik
    "SUM(weekly_usage.usage_gas) as Total_gas_usage, " +
    //Kolom gemiddelde elek tarief
    "ROUND(AVG(electricity_rate.rate), 2) as Average_elec_rate, " +
    //Kolom gemiddelde gas tarief
    "ROUND(AVG(gas_rate.rate), 2) as Average_gas_rate, " +
    //Kolom totale elek kosten, de som van verbruik * tarief
    "ROUND(SUM(weekly_usage.usage_elec * electricity_rate.rate), 2) as Total_elec_cost, " +
    //Kolom totale gas kosten, de som van verbruik * tarief
    "ROUND(SUM(weekly_usage.usage_gas * gas_rate.rate), 2) as Total_gas_cost, " +
    //Kolom totale kosten
    "ROUND((SUM(weekly_usage.usage_elec * electricity_rate.rate) + SUM(weekly_usage.usage_gas * gas_rate.rate)), 2) as Total_cost, " +
    //Kolom voorschot / bepaalde periode
    "ROUND(customer.advance / "+divideAdvance+", 2) as Average_advance, " +
    //Kolom overschrijding, voorschot - totale kosten
    "ROUND((SUM(weekly_usage.usage_elec * electricity_rate.rate) + SUM(weekly_usage.usage_gas * gas_rate.rate)) - (customer.advance / "+divideAdvance+"), 2) as Exceed " +
    //Van tabel (wekelijks)gebruik met joins (rates tabellen en customer tabel)
    //Gebruik left join zodat de weekly usage wordt weergegeven als rate of advance niet bestaat
    "FROM weekly_usage " +
    //Join stroomtarief als de periode van het (wekelijks)gebruik binnen de periode van het tarief valt, als er meerdere zijn geef de laatst toegevoegde
    "LEFT JOIN electricity_rate " +
    "ON electricity_rate.id = (SELECT MAX(id) " +
    "FROM electricity_rate er " +
    "WHERE er.date_from <= weekly_usage.date_start " +
    "AND er.date_to >= weekly_usage.date_end " +
    "AND er.customer_number = '" + customerNr + "'" +
    //Als het tarief veranderd midden in de periode van een wekelijks gebruik dan geldt het oude tarief voor die periode
    "OR (er.date_to BETWEEN weekly_usage.date_start AND weekly_usage.date_end) " +
    "AND er.date_from <= weekly_usage.date_start) " +
    //Join gastarief als de periode van het (wekelijks)gebruik binnen de periode van het tarief valt, als er meerdere zijn geef de laatst toegevoegde
    "LEFT JOIN gas_rate " +
    "ON gas_rate.id = (SELECT MAX(id) " +
    "FROM gas_rate gr " +
    "WHERE (gr.date_from <= weekly_usage.date_start " +
    "AND gr.date_to >= weekly_usage.date_end) " +
    "AND gr.customer_number = '" + customerNr + "'" +
    //Als het tarief veranderd midden in de periode van een wekelijks gebruik dan geldt het oude tarief voor die periode
    "OR (gr.date_to BETWEEN weekly_usage.date_start AND weekly_usage.date_end) " +
    "AND gr.date_from <= weekly_usage.date_start) " +
    //Join customer als het customer nummer gelijk is aan het customer nummer van het (wekelijks)gebruik
    "LEFT JOIN customer " +
    "ON weekly_usage.customer_number = customer.number " +
    //Alleen resultaten ophalen van deze customer
    "WHERE weekly_usage.customer_number = '" + customerNr + "' " +
    //Geen resultaten ophalen die in de toekomst liggen
    "AND weekly_usage.date_start <= CURRENT_DATE " +
    //Haal het resultaat op per bepaalde periode
    groupString +
    //Sort by most recent start date
    "ORDER BY MIN(weekly_usage.date_start) DESC";

    //Maak een tabel aan voor gejoinde result
    TableView<ObservableList<String>> tableView = new TableView<>();
    tableView.setEditable(true);

    //Aanmaken observable list met daarin een observable list die alle strings van een row bevat
    ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    //Maak kolom aan
    //Verkrijg de waarde uit de observable list die alle strings van een row bevat
    //Dit voor elke property van het gejoinde resultaat
    TableColumn<ObservableList<String>, String> column0 = new TableColumn<>("Periode");
    column0.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
    TableColumn<ObservableList<String>, String> column1 = new TableColumn<>("Gebruik elek");
    column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
    TableColumn<ObservableList<String>, String> column2 = new TableColumn<>("Gebruik gas");
    column2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
    TableColumn<ObservableList<String>, String> column3 = new TableColumn<>("Tarief elek");
    column3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));
    TableColumn<ObservableList<String>, String> column4 = new TableColumn<>("Tarief gas");
    column4.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4)));
    TableColumn<ObservableList<String>, String> column5 = new TableColumn<>("Kosten elek");
    column5.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5)));
    TableColumn<ObservableList<String>, String> column6 = new TableColumn<>("Kosten gas");
    column6.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(6)));
    TableColumn<ObservableList<String>, String> column7 = new TableColumn<>("Kosten totaal");
    column7.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(7)));
    TableColumn<ObservableList<String>, String> column8 = new TableColumn<>("Voorschot");
    column8.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(8)));
    TableColumn<ObservableList<String>, String> column9 = new TableColumn<>("Overschrijding");
    column9.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(9)));

    //Voeg alle kolommen toe aan de tabel
    tableView.getColumns().add(column0);
    tableView.getColumns().add(column1);
    tableView.getColumns().add(column2);
    tableView.getColumns().add(column3);
    tableView.getColumns().add(column4);
    tableView.getColumns().add(column5);
    tableView.getColumns().add(column6);
    tableView.getColumns().add(column7);
    tableView.getColumns().add(column8);
    tableView.getColumns().add(column9);
    //Stel de items in met de lijst van rows
    tableView.setItems(data);
    //Haal het resultaat op uit de database
    ResultSet result = Application.db.getData(query);
    //Check of alles goed gaat met de database, anders mysql exception
    try {
      //Voor elk resultaat
      while (result.next()) {
        //Maak een nieuwe row aan als observable list
        ObservableList<String> row = FXCollections.observableArrayList();
        //Haal alle kolommen uit het resultaat en zet ze in de row list
        //Het is een list met strings, dus van de resultaten moet een string worden gemaakt
        //Als een kolom 0.0 als resultaat geeft, stop dan "Onbekend" in de row
        String period = String.valueOf(result.getString("Period"));
        row.add(period);
        String usageElec = String.valueOf(result.getInt("Total_elec_usage"));
        row.add(usageElec);
        String usageGas = String.valueOf(result.getInt("Total_gas_usage"));
        row.add(usageGas);
        String rateElec = String.valueOf(result.getFloat("Average_elec_rate")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Average_elec_rate"));
        row.add(rateElec);
        String rateGas = String.valueOf(result.getFloat("Average_gas_rate")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Average_gas_rate"));
        row.add(rateGas);
        String costElec = String.valueOf(result.getFloat("Total_elec_cost")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Total_elec_cost"));
        row.add(costElec);
        String costGas = String.valueOf(result.getFloat("Total_gas_cost")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Total_gas_cost"));
        row.add(costGas);
        String costTotal = String.valueOf(result.getFloat("Total_cost")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Total_cost"));
        row.add(costTotal);
        String averageAdvance = String.valueOf(result.getFloat("Average_advance")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Average_advance"));
        row.add(averageAdvance);
        String exceed = String.valueOf(result.getFloat("Exceed")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Exceed"));
        row.add(exceed);
        //Voeg de row toe aan de lijst met rows
        data.add(row);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    //Geef de tabel terug
    return tableView;
  }

  public WeeklyUsage getCurrentWeeklyUsage()
  {
    //Initieer met null als value
    WeeklyUsage result = null;

    //Loop door de lijst
    for (WeeklyUsage weeklyUsage : this.weeklyUsages) {
      //Als de einddatum van het wekelijks gebruik in de periode van nu min een week ligt
      if (!weeklyUsage.getDateEnd().isBefore(LocalDate.now().minusWeeks(1)) && !weeklyUsage.getDateEnd().isAfter(LocalDate.now())) {
        //dan is dat het huidige wekelijkse verbruik
        result = weeklyUsage;
      }
    }

    //Geef huidige wekelijkse verbruik terug of null
    return  result;
  }

  public ElectricityRate getCurrentElecRate()
  {
    //Initieer met null als value
    ElectricityRate result = null;
    //Door rates heen lopen
    for (Object rate : this.rates) {
      //Als de rate van type electricity rate is
      if (rate instanceof ElectricityRate electricityRate) {
        //Als de huidige datum binnen periode van tarief ligt
        if (!LocalDate.now().isBefore(electricityRate.getDateFrom()) && !LocalDate.now().isAfter(electricityRate.getDateTo())) {
          //dan is dat het huidige tarief
          result = electricityRate;
        }
      }
    }

    //Geef huidige tarief terug of null
    return result;
  }

  public GasRate getCurrentGasRate()
  {
    //Initieer met null als value
    GasRate result = null;
    //Door alle rates heen lopen
    for (Object rate : this.rates) {
      //Als de rate van het type gas rate is
      if (rate instanceof GasRate gasRate) {
        //Als de huidige datum binnen periode van tarief ligt
        if (!LocalDate.now().isBefore(gasRate.getDateFrom()) && !LocalDate.now().isAfter(gasRate.getDateTo())) {
          //dan is dat het huidige tarief
          result = gasRate;
        }
      }
    }

    //Geef huidige tarief terug of null
    return result;
  }

  private void initElectricityRates()
  {
    //Stel een decimalformat in
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    //Query om alle stroomtarieven van een gebruiker op te halen, order datum vanaf meest recent boven
    String query =
            "SELECT * " +
            "FROM electricity_rate " +
            "WHERE customer_number = '"+customerNr+"' " +
            "ORDER BY electricity_rate.date_from DESC";
    //Haal resultaat op uit db
    ResultSet result = Application.db.getData(query);

    try {
      //Voor elk resultaat, maak nieuwe instantie van stroomtarief en voeg toe aan arraylist
      while (result.next()) {
        this.rates.add(new ElectricityRate(
                result.getString("customer_number"),
                Double.parseDouble(decimalFormat.format(result.getFloat("rate")).replace(',', '.')),
                result.getDate("date_from").toLocalDate(),
                result.getDate("date_to").toLocalDate()
        ));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void initGasRates()
  {
    //Stel een decimal format in
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    //Query om alle gastarieven van een gebruiker op te halen, order datum vanaf meest recent boven
    String query =
            "SELECT * " +
            "FROM gas_rate " +
            "WHERE customer_number = '"+customerNr+"' " +
            "ORDER BY gas_rate.date_from DESC";
    //Haal resultaat op uit db
    ResultSet result = Application.db.getData(query);

    try {
      //Voor elk resultaat, maak nieuwe instantie van gastarief en voeg toe aan arraylist
      while (result.next()) {
        this.rates.add(new GasRate(
                result.getString("customer_number"),
                Double.parseDouble(decimalFormat.format(result.getFloat("rate")).replace(',', '.')),
                result.getDate("date_from").toLocalDate(),
                result.getDate("date_to").toLocalDate()
        ));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void initWeeklyUsage()
  {
    //Maak nieuwe arraylist aan
    this.weeklyUsages = new ArrayList<>();
    //Query om alle wekelijkse gebruiken van een gebruiker op te halen, order datum start meest recent boven
    String query =
            "SELECT * " +
            "FROM weekly_usage " +
            "WHERE customer_number = '"+customerNr+"' " +
            "ORDER BY weekly_usage.date_start DESC";
    //Haal resultaat op uit db
    ResultSet result = Application.db.getData(query);

    try {
      //Voor elk resultaat, maak nieuwe instantie van wekelijks gebruik en voeg toe aan arraylist
      while (result.next()) {
        this.weeklyUsages.add(new WeeklyUsage(
                result.getString("customer_number"),
                result.getInt("usage_elec"),
                result.getInt("usage_gas"),
                result.getDate("date_start").toLocalDate(),
                result.getDate("date_end").toLocalDate()
        ));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void store()
  {
    //Query om een customer op te slaan in db
    String query =
            "INSERT INTO customer (number, firstname, lastname) " +
                    "VALUES ('" + customerNr + "', '" + firstname + "', '" + lastname + "')";

    //Roep store functie in db class
    Application.db.storeData(query);
  }

  public void update()
  {
    //Query om een customer up-te-daten
    String query =
            "UPDATE customer " +
                    "SET firstname = '" + firstname + "', lastname = '" + lastname + "', advance = '" + advance + "'" +
                    "WHERE number = '" + customerNr + "'";

    //Roep store functie aan in db
    Application.db.storeData(query);
  }

  public void addToRates(Object rate) {
    //Voeg stroomtarief toe aan lijst met stroomtarieven
    this.rates.add(rate);
  }

  public void addToWeeklyUsages(WeeklyUsage weeklyUsage) {
    //Voeg wekelijks gebruik toe aan lijst met wekelijkse gebruiken
    this.weeklyUsages.add(weeklyUsage);
  }

  public void setAdvance(Double advance) {
    this.advance = advance;
  }

  public String getCustomerNr() {
    return customerNr;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public Double getAdvance() {
    return advance;
  }

  public ArrayList<Object> getRates() {
    return rates;
  }

  public ArrayList<WeeklyUsage> getWeeklyUsages() {
    return weeklyUsages;
  }
}
