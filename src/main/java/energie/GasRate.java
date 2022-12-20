package energie;

import java.time.LocalDate;

public class GasRate {

  Double rate;
  LocalDate dateFrom;
  LocalDate dateTo;

  public GasRate(Double rate, LocalDate dateFrom, LocalDate dateTo) {
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
