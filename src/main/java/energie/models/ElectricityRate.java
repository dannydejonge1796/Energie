package energie.models;

import java.time.LocalDate;

public class ElectricityRate extends Rate{

  public ElectricityRate(String customerNr, Double rate, LocalDate dateFrom, LocalDate dateTo)
  {
    super(customerNr, rate, dateFrom, dateTo);
  }

  @Override
  public void destroy()
  {
    destroyRate("electricity_rate");
  }

  @Override
  public void store()
  {
    storeRate("electricity_rate");
  }
}
