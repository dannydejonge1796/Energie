package energie.models;

import energie.Application;

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
