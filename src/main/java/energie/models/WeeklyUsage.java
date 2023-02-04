package energie.models;

import energie.Application;

import java.time.LocalDate;

public class WeeklyUsage {

  private Integer id;
  private String customerNr;
  private Integer usageElec;
  private Integer usageGas;
  private LocalDate dateStart;
  private LocalDate dateEnd;

  public WeeklyUsage(String customerNr, Integer usageElec, Integer usageGas, LocalDate dateStart, LocalDate dateEnd)
  {
    this.customerNr = customerNr;
    this.usageElec = usageElec;
    this.usageGas = usageGas;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
  }

  public void destroy()
  {
    String query = "DELETE FROM weekly_usage WHERE date_start = '" + this.dateStart + "' " +
            "AND date_end = '" + this.dateEnd + "'";
    Application.db.storeData(query);
  }

  public void store()
  {
    String query =
      "INSERT INTO weekly_usage (customer_number, usage_elec, usage_gas, date_start, date_end) " +
      "VALUES ('" + customerNr + "', '" + usageElec + "', '" + usageGas + "', '" + dateStart + "', '" + dateEnd + "')";

    Application.db.storeData(query);
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
}
