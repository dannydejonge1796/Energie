package energie.models;

import energie.Application;

import java.time.LocalDate;

public class GasRate {

  private String customerNr;
  private Double rate;
  private LocalDate dateFrom;
  private LocalDate dateTo;

  public GasRate(String customerNr, Double rate, LocalDate dateFrom, LocalDate dateTo)
  {
    this.customerNr = customerNr;
    this.rate = rate;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public void destroy()
  {
    String query = "DELETE FROM gas_rate WHERE date_from = '" + this.dateFrom + "' " +
            "AND date_to = '" + this.dateTo + "'";
    Application.db.storeData(query);
  }

  public void store()
  {
    String query =
            "INSERT INTO gas_rate (customer_number, rate, date_from, date_to) " +
            "VALUES ('" + customerNr + "', '" + rate + "', '" + dateFrom + "', '" + dateTo + "')";

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
