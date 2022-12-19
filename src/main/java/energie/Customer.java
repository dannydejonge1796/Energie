package energie;

import java.util.ArrayList;
import java.util.Random;

public class Customer {

//  private Database database;
  private String customerNr;
  private String firstname;
  private String lastname;
  private Double advance;
  private ArrayList<ElectricityRate> electricityRates;
  private ArrayList<GasRate> gasRates;
  private ArrayList<WeeklyUsage> weeklyUsages;

  public Customer(String firstname, String lastname) {
//    this.database = new Database("test");

    Random rand = new Random();
    StringBuilder randomCustomerNr = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      int randomNumber = rand.nextInt(10);
      randomCustomerNr.append(randomNumber);
    }

    System.out.println(randomCustomerNr);

    this.customerNr = randomCustomerNr.toString();
    this.firstname = firstname;
    this.lastname = lastname;
    this.advance = null;
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
}
