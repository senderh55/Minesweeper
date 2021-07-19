package mines;

import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MyController {
	private int heightSize, widthSize, minesSize;
	private Mines game;
	private GridPane board;

	@FXML
	private StackPane menu;

	@FXML
	private Button resetButton;

	@FXML
	private TextField width;

	@FXML
	private TextField height;

	@FXML
	private TextField mines;

	@FXML
	private HBox hbox;

	@FXML
	void reset(MouseEvent event) {
		// create game according user input's
		heightSize = Integer.parseInt(height.getText());
		widthSize = Integer.parseInt(width.getText());
		minesSize = Integer.parseInt(mines.getText());
		game = new Mines(heightSize, widthSize, minesSize);
		BackgroundFill bgFill = new BackgroundFill(Color.CADETBLUE, new CornerRadii(10), null);
		Background bg = new Background(bgFill);
		hbox.setBackground(bg);
		// remove last board
		menu.getChildren().clear();
		hbox.getChildren().remove(board);
		// create board
		board = new GridPane();
		for (int i = 0; i < heightSize; i++)// create buttons
			for (int j = 0; j < widthSize; j++) {
				Button button = new Button();
				button.setOnMouseClicked(new pressedButton(i, j));
				button.setMinSize(40, 40);
				board.add(button, j, i);
			}
		// add and put board in the center 
		board.setAlignment(Pos.CENTER);
		menu.getChildren().add(board);
		print(false); // prints initial board 
	}

	class pressedButton implements EventHandler<MouseEvent> {
		int i, j;

		public pressedButton(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public void handle(MouseEvent event) {
			boolean mineFlag = false;
			// adjust a flag to the board when the right mouse button is pressed
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				game.toggleFlag(i, j);
			} else {
				if (game.open(i, j) == false) // if the place is mine - the game ended in a loss
					mineFlag = true;
				else if (game.isDone()) { // The game ended in victory
					printWinningMsg();
					return;
				}
			}
			print(mineFlag);
		}
	}

	// print markings on the board according game situation
	private void print(boolean mineFlag) {
		// if mineFlag is true - setShowAll will be true and the game ended in a loss
		game.setShowAll(mineFlag);
		// goes over the board buttons and print relevant markings
		List<javafx.scene.Node> list = board.getChildren();
		for (javafx.scene.Node place : list) {
			// clear place from pictures and texts
			((Labeled) place).setGraphic(null);
			((Labeled) place).setText("");
			StringBuilder b = new StringBuilder();
			b.append(game.get(GridPane.getRowIndex(place), GridPane.getColumnIndex(place)));
			((Labeled) place).setFont(Font.font("Ariel", FontWeight.BOLD, 18));
			if (b.toString().equals("X")) { // set mine picture becouse mineFlag is true -> showAll is true
				Image image = new Image("file:src/mines/mine.png", 16, 16, true, true);
				((Labeled) place).setGraphic(new ImageView(image));
			} else if (b.toString().equals("F")) { // set flag picture
				Image image = new Image("file:src/mines/flag.png", 16, 16, true, true);
				((Labeled) place).setGraphic(new ImageView(image));
			} else
				setColor((Labeled) place, b.toString()); // make number colored
		}

	}

	// set color on numbers
	private void setColor(Labeled place, String s) {
		if (s.equals("1"))
			place.setTextFill(Color.GREEN);
		else if (s.equals("2"))
			place.setTextFill(Color.BROWN);
		else if (s.equals("3"))
			place.setTextFill(Color.BLACK);
		else if (s.equals("4"))
			place.setTextFill(Color.ORANGERED);
		else if (s.equals("5"))
			place.setTextFill(Color.YELLOW);
		else if (s.equals("6"))
			place.setTextFill(Color.RED);
		else if (s.equals("7"))
			place.setTextFill(Color.PURPLE);
		else if (s.equals("8"))
			place.setTextFill(Color.BLUE);
		((Labeled) place).setText(s);
	}

	// print the winning message
	private void printWinningMsg() {
		Label label = new Label("OMG YOU JUST WON!!!");
		label.setAlignment(Pos.CENTER);// text in the middle

		// create Background for label
		BackgroundFill bgFill = new BackgroundFill(Color.WHITE, new CornerRadii(10), null);
		Background bg = new Background(bgFill);
		label.setPadding(new Insets(20));
		label.setBackground(bg);
		label.setMaxWidth(Double.MAX_VALUE);// stretch the message
		label.setFont(new Font("Ariel", 25));
		board.getChildren().clear();
		menu.setAlignment(Pos.CENTER); // put label in the center
		menu.getChildren().add(label);

	}

}
