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
import java.time.LocalDate;
import java.util.ArrayList;

public class Customer {

  private final String customerNr;
  private final String firstname;
  private final String lastname;
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

  public TableView<WeeklyUsage> getTableUsage()
  {
    TableView<WeeklyUsage> table = new TableView<>();
    table.setItems(FXCollections.observableArrayList(this.weeklyUsages));

    TableColumn<WeeklyUsage, String> colUsageElec = new TableColumn<>("Stroom gebruik");
    colUsageElec.setCellValueFactory(new PropertyValueFactory<>("usageElec"));

    TableColumn<WeeklyUsage, String> colUsageGas = new TableColumn<>("Gas gebruik");
    colUsageGas.setCellValueFactory(new PropertyValueFactory<>("usageGas"));

    TableColumn<WeeklyUsage, String> colDateStart = new TableColumn<>("Start datum");
    colDateStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));

    TableColumn<WeeklyUsage, String> colDateEnd = new TableColumn<>("Eind datum");
    colDateEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));

    TableColumn<WeeklyUsage, Void> deleteCol = new TableColumn<>("Delete");
    deleteCol.setCellFactory(new Callback<>() {
      @Override
      public TableCell<WeeklyUsage, Void> call(final TableColumn<WeeklyUsage, Void> param) {
        return new TableCell<>() {
          private final Button deleteButton = new Button("Delete");

          {
            deleteButton.setOnAction((ActionEvent event) -> {
              WeeklyUsage weeklyUsage = getTableView().getItems().get(getIndex());
              weeklyUsage.destroy();
              weeklyUsages.remove(weeklyUsage);
              table.getItems().remove(weeklyUsage);
            });
          }

          @Override
          public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
            } else {
              setGraphic(deleteButton);
            }
          }
        };
      }
    });

    table.getColumns().add(colUsageElec);
    table.getColumns().add(colUsageGas);
    table.getColumns().add(colDateStart);
    table.getColumns().add(colDateEnd);
    table.getColumns().add(deleteCol);

    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    table.autosize();

    return table;
  }

  public TableView<ElectricityRate> getTableElec()
  {
    TableView<ElectricityRate> table = new TableView<>();
    table.setItems(FXCollections.observableArrayList(this.electricityRates));

    TableColumn<ElectricityRate, String> colRate = new TableColumn<>("Stroom tarief");
    colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

    TableColumn<ElectricityRate, String> colDateFrom = new TableColumn<>("Datum vanaf");
    colDateFrom.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));

    TableColumn<ElectricityRate, String> colDateTo = new TableColumn<>("Datum tot");
    colDateTo.setCellValueFactory(new PropertyValueFactory<>("dateTo"));

    TableColumn<ElectricityRate, Void> deleteCol = new TableColumn<>("Delete");
    deleteCol.setCellFactory(new Callback<>() {
      @Override
      public TableCell<ElectricityRate, Void> call(final TableColumn<ElectricityRate, Void> param) {
        return new TableCell<>() {
          private final Button deleteButton = new Button("Delete");

          {
            deleteButton.setOnAction((ActionEvent event) -> {
              ElectricityRate electricityRate = getTableView().getItems().get(getIndex());
              electricityRate.destroy();
              electricityRates.remove(electricityRate);
              table.getItems().remove(electricityRate);
            });
          }

          @Override
          public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
            } else {
              setGraphic(deleteButton);
            }
          }
        };
      }
    });

    table.getColumns().add(colRate);
    table.getColumns().add(colDateFrom);
    table.getColumns().add(colDateTo);
    table.getColumns().add(deleteCol);

    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    table.autosize();

    return table;
  }

  public TableView<GasRate> getTableGas()
  {
    TableView<GasRate> table = new TableView<>();
    table.setItems(FXCollections.observableArrayList(this.gasRates));

    TableColumn<GasRate, String> colRate = new TableColumn<>("Gas tarief");
    colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

    TableColumn<GasRate, String> colDateFrom = new TableColumn<>("Datum vanaf");
    colDateFrom.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));

    TableColumn<GasRate, String> colDateTo = new TableColumn<>("Datum tot");
    colDateTo.setCellValueFactory(new PropertyValueFactory<>("dateTo"));

    TableColumn<GasRate, Void> deleteCol = new TableColumn<>("Delete");
    deleteCol.setCellFactory(new Callback<>() {
      @Override
      public TableCell<GasRate, Void> call(final TableColumn<GasRate, Void> param) {
        return new TableCell<>() {
          private final Button deleteButton = new Button("Delete");

          {
            deleteButton.setOnAction((ActionEvent event) -> {
              GasRate gasRate = getTableView().getItems().get(getIndex());
              gasRate.destroy();
              gasRates.remove(gasRate);
              table.getItems().remove(gasRate);
            });
          }

          @Override
          public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
            } else {
              setGraphic(deleteButton);
            }
          }
        };
      }
    });

    table.getColumns().add(colRate);
    table.getColumns().add(colDateFrom);
    table.getColumns().add(colDateTo);
    table.getColumns().add(deleteCol);

    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    table.autosize();

    return table;
  }

  public TableView<ObservableList<String>> getOverview(String selectedPeriod)
  {
    int divideAdvance = 0;
    String groupString = "";
    if (selectedPeriod.equals("Wekelijks")) {
      //Haal het resultaat op per week
      divideAdvance = 52;
      groupString = "GROUP BY WEEK(weekly_usage.date_start) ";
    }

    if (selectedPeriod.equals("Maandelijks")) {
      //Haal het resultaat op per maand
      divideAdvance = 12;
      groupString = "GROUP BY MONTH(weekly_usage.date_start) ";
    }

    if (selectedPeriod.equals("Jaarlijks")) {
      //Haal het resultaat op per jaar
      divideAdvance = 1;
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
    "ROUND((SUM(weekly_usage.usage_elec * electricity_rate.rate) + SUM(weekly_usage.usage_gas * gas_rate.rate)) - (customer.advance / "+divideAdvance+"), 2) as Exceedance " +
    //Van tabel (wekelijks)gebruik met joins (rates tabellen en customer tabel)
    //Gebruik left join zodat de weekly usage wordt weergegeven als rate of advance niet bestaat
    "FROM weekly_usage " +
    //Join elektarief als de periode van het (wekelijks)gebruik binnen de periode van het tarief valt, als er meerdere zijn geef de laatst toegevoegde
    "LEFT JOIN electricity_rate " +
    "ON electricity_rate.id = (SELECT MAX(id) " +
    "FROM electricity_rate er " +
    "WHERE er.date_from <= weekly_usage.date_start " +
    "AND er.date_to >= weekly_usage.date_end " +
    //Als het tarief veranderd midden in de periode van een wekelijks gebruik dan geldt het oude tarief voor die periode
    "OR (er.date_to BETWEEN weekly_usage.date_start AND weekly_usage.date_end) " +
    "AND er.date_from <= weekly_usage.date_start) " +
    //Join gastarief als de periode van het (wekelijks)gebruik binnen de periode van het tarief valt, als er meerdere zijn geef de laatst toegevoegde
    "LEFT JOIN gas_rate " +
    "ON gas_rate.id = (SELECT MAX(id) " +
    "FROM gas_rate gr " +
    "WHERE (gr.date_from <= weekly_usage.date_start " +
    "AND gr.date_to >= weekly_usage.date_end) " +
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
        String exceedance = String.valueOf(result.getFloat("Exceedance")).equals("0.0") ? "Onbekend" : String.valueOf(result.getFloat("Exceedance"));
        row.add(exceedance);

        data.add(row);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return tableView;
  }

  public WeeklyUsage getCurrentWeeklyUsage()
  {
    WeeklyUsage result = null;

    for (WeeklyUsage weeklyUsage : this.weeklyUsages) {
      if (!weeklyUsage.getDateEnd().isBefore(LocalDate.now().minusWeeks(1)) && !weeklyUsage.getDateEnd().isAfter(LocalDate.now())) {
        result = weeklyUsage;
      }
    }
    return  result;
  }

  public ElectricityRate getCurrentElecRate()
  {
    ElectricityRate result = null;

    for (ElectricityRate electricityRate : this.electricityRates) {
      if (!LocalDate.now().isBefore(electricityRate.getDateFrom()) && !LocalDate.now().isAfter(electricityRate.getDateTo())) {
        result = electricityRate;
      }
    }

    return result;
  }

  public GasRate getCurrentGasRate()
  {
    GasRate result = null;

    for (GasRate gasRate : this.gasRates) {
      if (!LocalDate.now().isBefore(gasRate.getDateFrom()) && !LocalDate.now().isAfter(gasRate.getDateTo())) {
        result = gasRate;
      }
    }

    return result;
  }

  private void initElectricityRates()
  {
    this.electricityRates = new ArrayList<>();

    String query =
            "SELECT * " +
            "FROM electricity_rate " +
            "WHERE customer_number = '"+customerNr+"' " +
            "ORDER BY electricity_rate.date_from DESC";

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
            "WHERE customer_number = '"+customerNr+"' " +
            "ORDER BY gas_rate.date_from DESC";

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
            "WHERE customer_number = '"+customerNr+"' " +
            "ORDER BY weekly_usage.date_start DESC";

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
