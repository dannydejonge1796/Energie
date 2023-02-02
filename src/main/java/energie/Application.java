package energie;

import energie.models.CustomerRegister;
import energie.models.Database;
import energie.screens.Home;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

  public static Database db;

  @Override
  public void start(Stage primaryStage) {
    db = new Database("energy");
    CustomerRegister cR = new CustomerRegister();
    primaryStage.setScene(new Home(primaryStage, cR).getHomeScene());
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
