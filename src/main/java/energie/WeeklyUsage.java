package energie;

import java.time.LocalDate;

public class WeeklyUsage {

  private Double usageElec;
  private Double usageGas;
  private LocalDate dateStart;
  private LocalDate dateEnd;

  public WeeklyUsage(Double usageElec, Double usageGas, LocalDate dateStart, LocalDate dateEnd) {
    this.usageElec = usageElec;
    this.usageGas = usageGas;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
  }

  public Double getUsageElec() {
    return usageElec;
  }

  public Double getUsageGas() {
    return usageGas;
  }

  public LocalDate getDateStart() {
    return dateStart;
  }

  public LocalDate getDateEnd() {
    return dateEnd;
  }

  public void setUsageElec(Double usageElec) {
    this.usageElec = usageElec;
  }

  public void setUsageGas(Double usageGas) {
    this.usageGas = usageGas;
  }

  public void setDateStart(LocalDate dateStart) {
    this.dateStart = dateStart;
  }

  public void setDateEnd(LocalDate dateEnd) {
    this.dateEnd = dateEnd;
  }
}
