package energie;

import java.time.LocalDate;

public class WeeklyUsage {

  private Integer usageElec;
  private Integer usageGas;
  private LocalDate dateStart;
  private LocalDate dateEnd;

  public WeeklyUsage(Integer usageElec, Integer usageGas, LocalDate dateStart, LocalDate dateEnd) {
    this.usageElec = usageElec;
    this.usageGas = usageGas;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
  }

  public Integer getUsageElec() {
    return usageElec;
  }

  public Integer getUsageGas() {
    return usageGas;
  }

  public LocalDate getDateStart() {
    return dateStart;
  }

  public LocalDate getDateEnd() {
    return dateEnd;
  }

  public void setDateStart(LocalDate dateStart) {
    this.dateStart = dateStart;
  }

  public void setDateEnd(LocalDate dateEnd) {
    this.dateEnd = dateEnd;
  }
}
