package energie.models;

import energie.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Customer {

  private String customerNr;
  private String firstname;
  private String lastname;
  private Double advance;
  private ArrayList<ElectricityRate> electricityRates;
  private ArrayList<GasRate> gasRates;
  private ArrayList<WeeklyUsage> weeklyUsages;

  public Customer(String customerNr, String firstname, String lastname, Double advance) {
    this.customerNr = customerNr;
    this.firstname = firstname;
    this.lastname = lastname;
    this.advance = advance;
    initElectricityRates();
    initGasRates();
    initWeeklyUsage();
  }

  public TableView<ObservableList<String>> getTableMonth()
  {
    int monthsInYear = 12;

    String query =
    "SELECT " +
    //Selecteer de gewenste kolommen
    "MONTH(weekly_usage.date_start) as Month, " +
    "CONCAT(MIN(weekly_usage.date_start), ' - ', MAX(weekly_usage.date_end)) as Period, " +
    "SUM(weekly_usage.usage_elec) as Total_elec_usage, " +
    "SUM(weekly_usage.usage_gas) as Total_gas_usage, " +
    "ROUND(AVG(electricity_rate.rate), 2) as Average_elec_rate, " +
    "ROUND(AVG(gas_rate.rate), 2) as Average_gas_rate, " +
    "ROUND(SUM(weekly_usage.usage_elec * electricity_rate.rate), 2) as Total_elec_cost, " +
    "ROUND(SUM(weekly_usage.usage_gas * gas_rate.rate), 2) as Total_gas_cost, " +
    "ROUND((SUM(weekly_usage.usage_elec * electricity_rate.rate) + SUM(weekly_usage.usage_gas * gas_rate.rate)), 2) as Total_cost, " +
    "ROUND(SUM(customer.advance / "+monthsInYear+"), 0) as Average_advance, " +
    "ROUND((SUM(weekly_usage.usage_elec * electricity_rate.rate) + SUM(weekly_usage.usage_gas * gas_rate.rate)) - SUM(customer.advance / "+monthsInYear+"), 0) as Exceedance " +
    //Van tabel (wekelijks) gebruik met joins (rates tabellen en customer tabel)
    //Gebruik left join zodat de weekly usage nogsteeds wordt weergegeven als rate of advance niet bestaat
    "FROM weekly_usage " +
    "LEFT JOIN electricity_rate " +
    //Join elektarief als de periode van het (wekelijks)gebruik binnen de periode van het tarief valt
    "ON weekly_usage.date_start >= electricity_rate.date_from " +
    "AND weekly_usage.date_end <= electricity_rate.date_to " +
    "LEFT JOIN gas_rate " +
    //Join gastarief als de periode van het (wekelijks)gebruik binnen de periode van het tarief valt
    "ON weekly_usage.date_start >= gas_rate.date_from " +
    "AND weekly_usage.date_end <= gas_rate.date_to " +
    "LEFT JOIN customer " +
    //Join customer als het customer nummer gelijk is aan het customer nummer van het (wekelijks)gebruik
    "ON weekly_usage.customer_number = customer.number " +
    //Alleen resultaten ophalen van deze customer
    "WHERE weekly_usage.customer_number = '" + customerNr + "' " +
    //Geen resultaten ophalen die in de toekomst liggen
    "AND weekly_usage.date_end <= CURRENT_DATE " +
    //Haal het resultaat op per maand
    "GROUP BY MONTH(weekly_usage.date_start)";

    return createTableView(query);
  }

  private TableView<ObservableList<String>> createTableView(String query)
  {
    TableView<ObservableList<String>> tableView = new TableView<>();
    tableView.setEditable(true);

    ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

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

    tableView.setItems(data);

    ResultSet result = Application.db.getData(query);

    try {
      while (result.next()) {
        ObservableList<String> row = FXCollections.observableArrayList();

        String period = String.valueOf(result.getString("Period")).equals("0") ? "Onbekend" : String.valueOf(result.getString("Period"));
        row.add(period);
        String usageElec = String.valueOf(result.getInt("Total_elec_usage"));
        row.add(usageElec);
        String usageGas = String.valueOf(result.getInt("Total_gas_usage"));
        row.add(usageGas);
        String rateElec = String.valueOf(result.getFloat("Average_elec_rate"));
        row.add(rateElec);
        String rateGas = String.valueOf(result.getFloat("Average_gas_rate"));
        row.add(rateGas);
        String costElec = String.valueOf(result.getFloat("Total_elec_cost"));
        row.add(costElec);
        String costGas = String.valueOf(result.getFloat("Total_gas_cost"));
        row.add(costGas);
        String costTotal = String.valueOf(result.getFloat("Total_cost"));
        row.add(costTotal);
        String averageAdvance = String.valueOf(result.getFloat("Average_advance"));
        row.add(averageAdvance);
        String exceedance = String.valueOf(result.getFloat("Exceedance"));
        row.add(exceedance);

        data.add(row);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return tableView;
  }

  private void initElectricityRates()
  {
    this.electricityRates = new ArrayList<>();

    String query =
            "SELECT * " +
                    "FROM electricity_rate " +
                    "WHERE customer_number = '"+customerNr+"'";

    ResultSet result = Application.db.getData(query);

    try {
      while (result.next()) {
        this.electricityRates.add(new ElectricityRate(
                result.getString("customer_number"),
                (double) result.getFloat("rate"),
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
    this.gasRates = new ArrayList<>();

    String query =
            "SELECT * " +
            "FROM gas_rate " +
            "WHERE customer_number = '"+customerNr+"'";

    ResultSet result = Application.db.getData(query);

    try {
      while (result.next()) {
        this.gasRates.add(new GasRate(
                result.getString("customer_number"),
                (double) result.getFloat("rate"),
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
    this.weeklyUsages = new ArrayList<>();

    String query =
            "SELECT * " +
            "FROM weekly_usage " +
            "WHERE customer_number = '"+customerNr+"'";

    ResultSet result = Application.db.getData(query);

    try {
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
    String query =
            "INSERT INTO customer (number, firstname, lastname) " +
                    "VALUES ('" + customerNr + "', '" + firstname + "', '" + lastname + "')";

    Application.db.storeData(query);
  }

  public void update()
  {
    String query =
            "UPDATE customer " +
                    "SET firstname = '" + firstname + "', lastname = '" + lastname + "', advance = '" + advance + "'" +
                    "WHERE number = '" + customerNr + "'";


    Application.db.storeData(query);
  }

  public void addToElectricityRates(ElectricityRate electricityRate) {
    this.electricityRates.add(electricityRate);
  }

  public void addToGasRates(GasRate gasRate) {
    this.gasRates.add(gasRate);
  }

  public void setAdvance(Double advance) {
    this.advance = advance;
  }

  public void addToWeeklyUsages(WeeklyUsage weeklyUsage) {
    this.weeklyUsages.add(weeklyUsage);
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

  public ArrayList<ElectricityRate> getElectricityRates() {
    return electricityRates;
  }

  public ArrayList<GasRate> getGasRates() {
    return gasRates;
  }

  public ArrayList<WeeklyUsage> getWeeklyUsages() {
    return weeklyUsages;
  }
}
