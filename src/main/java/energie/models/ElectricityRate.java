package energie.models;

import energie.Application;

import java.time.LocalDate;

public class ElectricityRate {

  private String customerNr;
  private Double rate;
  private LocalDate dateFrom;
  private LocalDate dateTo;

  public ElectricityRate(String customerNr, Double rate, LocalDate dateFrom, LocalDate dateTo)
  {
    this.customerNr = customerNr;
    this.rate = rate;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public void add()
  {
    String query =
            "INSERT INTO electricity_rate (customer_number, rate, date_from, date_to) " +
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
