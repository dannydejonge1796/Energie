package energie;

import java.time.LocalDate;

public class WeeklyUsage {

  Double usageElec;
  Double usageGas;
  LocalDate dateStart;
  LocalDate dateEnd;

  public WeeklyUsage(Double usageElec, Double usageGas, LocalDate dateStart, LocalDate dateEnd) {
    this.usageElec = usageElec;
    this.usageGas = usageGas;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
  }
}
