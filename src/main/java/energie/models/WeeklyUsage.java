package energie.models;

import energie.Application;

import java.time.LocalDate;

public class WeeklyUsage {

  private final String customerNr;
  private final Integer usageElec;
  private final Integer usageGas;
  private final LocalDate dateStart;
  private final LocalDate dateEnd;

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
    //Query voor het verwijderen van een wekelijks gebruik uit de database
    String query = "DELETE FROM weekly_usage WHERE customer_number = '" + customerNr + "' " +
            "AND date_start = '" + this.dateStart + "' " +
            "AND date_end = '" + this.dateEnd + "'";
    //Roep de store functie aan in de database class
    Application.db.storeData(query);
  }

  public void store()
  {
    //Query voor het opslaan van een wekelijks gebruik
    String query =
      "INSERT INTO weekly_usage (customer_number, usage_elec, usage_gas, date_start, date_end) " +
      "VALUES ('" + customerNr + "', '" + usageElec + "', '" + usageGas + "', '" + dateStart + "', '" + dateEnd + "')";
    //Roep de store functie aan in de database class
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
