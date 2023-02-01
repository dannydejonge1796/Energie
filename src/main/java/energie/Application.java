package energie;

import energie.models.CustomerRegister;
import energie.models.Database;
import energie.screens.Home;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
  @Override
  public void start(Stage primaryStage) {
    Database db = new Database("energy");
    CustomerRegister cR = new CustomerRegister(db);
    primaryStage.setScene(new Home(primaryStage, db, cR).getHomeScene());
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
