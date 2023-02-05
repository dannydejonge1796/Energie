package energie;

import energie.models.CustomerRegister;
import energie.models.Database;
import energie.screens.Home;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

  //Database overal beschikbaar
  public static Database db;

  @Override
  public void start(Stage primaryStage) {
    //Database aanmaken
    db = new Database("energy");
    //Customer register aanmaken
    CustomerRegister cR = new CustomerRegister();
    //Scene van stage instellen met home scene
    primaryStage.setScene(new Home(primaryStage, cR).getHomeScene());
    //Stage weergeven
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
