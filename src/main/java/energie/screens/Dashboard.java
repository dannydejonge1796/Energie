package energie.screens;

import energie.Customer;
import energie.CustomerRegister;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Dashboard {

  private Customer customer;
  private Scene dashboardScene;

  public Dashboard(Stage primaryStage, Customer customer) {
    this.customer = customer;
    primaryStage.setTitle("Energie - Welkom " + this.customer.getFirstname() + "!");

    BorderPane border = new BorderPane();
    border.setCenter(addDashboardPane());

    this.dashboardScene = new Scene(border,1280, 720);
  }

  public GridPane addDashboardPane() {
    // Nieuwe gridpane aanmaken
    GridPane grid = new GridPane();
    // Gridpane in het midden van het scherm positioneren
    grid.setAlignment(Pos.CENTER);
    // Padding van 20px aan elke kant
    grid.setPadding(new Insets(40, 40, 40, 40));
    // Horizontale witregel tussen columns
    grid.setHgap(10);
    // Verticale witregel tussen rows
    grid.setVgap(10);

    // Header label toevoegen
    Label headerLabel = new Label("Welkom " + customer.getFirstname() + "!");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));



    return grid;
  }

  public Scene getDashboardScene() {
    return dashboardScene;
  }
}
