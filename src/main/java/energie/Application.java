package energie;

import energie.screens.Home;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
  public static int[] applicationSize = {1200, 650};
  @Override
  public void start(Stage stage) {
    stage.setWidth(applicationSize[0]);
    stage.setHeight(applicationSize[1]);
    stage.setResizable(false);
    stage.setTitle("Energie");

    stage.setScene(new Home().getHomeScene());
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
