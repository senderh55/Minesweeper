package mines;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		HBox hbox;
		MyController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Minesweeper.fxml"));
			hbox = loader.load();
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene s = new Scene(hbox);
		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(s);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
