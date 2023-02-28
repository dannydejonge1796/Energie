package energie.models;

import energie.Application;

import java.time.LocalDate;

//Abstract betekend dat dit geen op zichzelf staande class is
public abstract class Rate {

  protected String customerNr;
  protected Double rate;
  protected LocalDate dateFrom;
  protected LocalDate dateTo;

  public Rate(String customerNr, Double rate, LocalDate dateFrom, LocalDate dateTo)
  {
    this.customerNr = customerNr;
    this.rate = rate;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public abstract void destroy();

  public abstract void store();

  protected void storeRate(String table) {
    //Query voor het opslaan van een tarieven in de database
    String query = "INSERT INTO " + table + " (customer_number, rate, date_from, date_to) " +
            "VALUES ('" + customerNr + "', '" + rate + "', '" + dateFrom + "', '" + dateTo + "')";
    //Roep de store functie aan van de database class
    Application.db.storeData(query);
  }

  protected void destroyRate(String table) {
    //Query voor het verwijderen van een tarief uit de database
    String query = "DELETE FROM " + table + " WHERE customer_number = '" + customerNr + "' " +
            "AND date_from = '" + dateFrom + "' " +
            "AND date_to = '" + dateTo + "'";
    //Roep de store functie aan van de database class
    Application.db.storeData(query);
  }

  public String getCustomerNr() {
    return customerNr;
  }

  public Double getRate() {
    return rate;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }
}
