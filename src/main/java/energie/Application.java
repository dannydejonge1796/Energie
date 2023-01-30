package energie;

import energie.models.CustomerRegister;
import energie.screens.Home;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
  @Override
  public void start(Stage primaryStage) {
    CustomerRegister cR = new CustomerRegister();
    primaryStage.setScene(new Home(primaryStage, cR).getHomeScene());
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
