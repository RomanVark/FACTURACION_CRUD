package ni.edu.uam.facturacionapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {
 @Override public void start(Stage stage) throws Exception {
  Scene scene=new Scene(FXMLLoader.load(Main.class.getResource("Main.fxml")));
  stage.setTitle("FacturaciónAPP | Gestión de catálogos"); stage.setScene(scene);
  stage.setMinWidth(900); stage.setMinHeight(650); stage.show();
 }
 public static void main(String[] args) { launch(args); }
}
