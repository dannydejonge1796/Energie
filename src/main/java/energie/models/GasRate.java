package energie.models;

import java.time.LocalDate;

public class GasRate extends Rate{

  public GasRate(String customerNr, Double rate, LocalDate dateFrom, LocalDate dateTo)
  {
    super(customerNr, rate, dateFrom, dateTo);
  }

  @Override
  public void destroy()
  {
    destroyRate("gas_rate");
  }

  @Override
  public void store()
  {
    storeRate("gas_rate");
  }
}
