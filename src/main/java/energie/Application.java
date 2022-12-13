package energie;

import energie.screens.Home;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setScene(new Home(primaryStage).getHomeScene());
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
