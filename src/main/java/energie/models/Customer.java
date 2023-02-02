package energie.models;

import energie.Application;
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

//  public TableView<Result> getTableMonth()
//  {


//
//  SELECT
//  SUM(weekly_usage.usage_elec) as total_elec_usage,
//  SUM(weekly_usage.usage_gas) as total_gas_usage,
//  ROUND(AVG(electricity_rate.rate), 2) as avg_elec_rate,
//  ROUND(AVG(gas_rate.rate), 2) as avg_gas_rate,
//  ROUND(SUM(weekly_usage.usage_gas * gas_rate.rate), 2) as total_gas_cost,
//  ROUND(SUM(weekly_usage.usage_elec * electricity_rate.rate), 2) as total_elec_cost,
//  ROUND(SUM(customer.advance)/12, 2) as avg_advance
//  FROM weekly_usage
//  JOIN electricity_rate
//  ON weekly_usage.date_start >= electricity_rate.date_from
//  AND weekly_usage.date_end <= electricity_rate.date_to
//  JOIN gas_rate
//  ON weekly_usage.date_start >= gas_rate.date_from
//  AND weekly_usage.date_end <= gas_rate.date_to
//  JOIN customer
//  ON weekly_usage.customer_number = customer.number
//  WHERE weekly_usage.date_end >= DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH)
//  AND weekly_usage.customer_number = 123;


//  }

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
