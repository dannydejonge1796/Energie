package energie;

import java.time.LocalDate;

public class ElectricityRate {

  Double rate;
  LocalDate dateFrom;
  LocalDate dateTo;

  public ElectricityRate(Double rate, LocalDate dateFrom, LocalDate dateTo) {
    this.rate = rate;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
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
