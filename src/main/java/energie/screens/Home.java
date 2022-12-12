package energie.screens;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Home {

  private final Scene homeScene;

  public Home() {

    BorderPane border = new BorderPane();

    homeScene = new Scene(border);
  }

  public Scene getHomeScene() {
    return homeScene;
  }
}
