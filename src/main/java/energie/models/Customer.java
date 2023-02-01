package energie.models;

import java.util.ArrayList;

public class Customer {

  private String customerNr;
  private String firstname;
  private String lastname;
  private Double advance;
  private ArrayList<ElectricityRate> electricityRates;
  private ArrayList<GasRate> gasRates;
  private ArrayList<WeeklyUsage> weeklyUsages;

  public Customer(String customerNr, String firstname, String lastname, Double advance)
  {
    this.customerNr = customerNr;
    this.firstname = firstname;
    this.lastname = lastname;
    this.advance = advance;
    this.electricityRates = new ArrayList<>();
    this.gasRates = new ArrayList<>();
    this.weeklyUsages = new ArrayList<>();
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

  public void setAdvance(Double advance) {
    this.advance = advance;
  }

  public void addToElectricityRates(ElectricityRate electricityRate) {
    this.electricityRates.add(electricityRate);
  }

  public void addToGasRates(GasRate gasRate) {
    this.gasRates.add(gasRate);
  }

  public void addToWeeklyUsages(WeeklyUsage weeklyUsage) {
    this.weeklyUsages.add(weeklyUsage);
  }

  public void setCustomerNr(String customerNr) {
    this.customerNr = customerNr;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public void setElectricityRates(ArrayList<ElectricityRate> electricityRates) {
    this.electricityRates = electricityRates;
  }

  public void setGasRates(ArrayList<GasRate> gasRates) {
    this.gasRates = gasRates;
  }

  public void setWeeklyUsages(ArrayList<WeeklyUsage> weeklyUsages) {
    this.weeklyUsages = weeklyUsages;
  }
}
