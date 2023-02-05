package energie.models;

import energie.Application;

import java.time.LocalDate;

public class ElectricityRate {

  private final String customerNr;
  private final Double rate;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;

  public ElectricityRate(String customerNr, Double rate, LocalDate dateFrom, LocalDate dateTo)
  {
    this.customerNr = customerNr;
    this.rate = rate;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public void destroy()
  {
    //Query voor het verwijderen van een stroomtarief uit de database
    String query = "DELETE FROM electricity_rate WHERE customer_number = '" + customerNr + "' " +
            "AND date_from = '" + this.dateFrom + "' " +
            "AND date_to = '" + this.dateTo + "'";
    //Roep de store functie aan van de database class
    Application.db.storeData(query);
  }

  public void store()
  {
    //Query voor het opslaan van een stroomtarief in de database
    String query =
            "INSERT INTO electricity_rate (customer_number, rate, date_from, date_to) " +
            "VALUES ('" + customerNr + "', '" + rate + "', '" + dateFrom + "', '" + dateTo + "')";

    //Roep de store functie aan van de database class
    Application.db.storeData(query);
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
