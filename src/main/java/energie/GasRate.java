package energie;

import java.time.LocalDate;

public class GasRate {

  private Double rate;
  private LocalDate dateFrom;
  private LocalDate dateTo;

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

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public void setDateFrom(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
  }

  public void setDateTo(LocalDate dateTo) {
    this.dateTo = dateTo;
  }
}
