package energie.models;

import energie.Application;

import java.time.LocalDate;

public class GasRate {

  private final String customerNr;
  private final Double rate;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;

  public GasRate(String customerNr, Double rate, LocalDate dateFrom, LocalDate dateTo)
  {
    this.customerNr = customerNr;
    this.rate = rate;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public void destroy()
  {
    //Query voor het verwijderen van een gastarief uit de database
    String query = "DELETE FROM gas_rate WHERE date_from = '" + this.dateFrom + "' " +
            "AND date_to = '" + this.dateTo + "'";
    //Roep de store functie aan van de database class
    Application.db.storeData(query);
  }

  public void store()
  {
    //Query voor het opslaan van een gastarief in de database
    String query =
            "INSERT INTO gas_rate (customer_number, rate, date_from, date_to) " +
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
