package aircondition;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author Darby Oleary
 * @date 12-13-2022
 * 
 *       The view class for a game of quoridor, displays the board based on the
 *       information it gets from the model
 */

public class QuorView extends Application implements PropertyChangeListener, EventHandler<ActionEvent> {

	Scene scene;
	Button[][] buttons;
	Button clear;
	GridPane grid;
	BorderPane root;
	BorderPane top;
	private ComboBox<Integer> choices;
	int selection = 5;
	Stage stage;
	private Label output;
	QuorModel game;
	boolean won;
	Alert alert;

	/**
	 * 
	 * Method to create the screen and set/reset all the variables
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			alert = new Alert(AlertType.NONE);
			root = new BorderPane();
			top = new BorderPane();
			grid = new GridPane();
			output = new Label();
			clear = new Button();
			clear.setText("Reset");
			clear.setOnAction(this);
			top = new BorderPane();
			choices = new ComboBox<Integer>();
			choices.getItems().addAll(3, 5, 7, 9, 11);
			choices.setOnAction(this);
			root.setCenter(grid);
			top.setLeft(choices);
			top.setRight(clear);
			root.setTop(top);
			root.setBottom(output);
			createGame(primaryStage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Method that exists for resetting the game, it resets all the variables of the
	 * game so there's a fresh start
	 * 
	 * @param primaryStage
	 */
	public void createGame(Stage primaryStage) {
		game = new QuorModel(selection);
		game.addPropertyChangeListener(this);
		won = false;
		stage = primaryStage;
		buttons = new Button[(selection * 2) - 1][(selection * 2) - 1];
		// there is a barrier in between each node but not on the ends so it ends up
		// being x nodes and x-1 barriers per row/column, same as x*2 - 1
		output.setText("Red's turn");
		choices.setValue(selection);
		int screenSize = (selection * 50) + ((selection - 1) * 10);
		// makes the screen size based on barriers being 50x10 and spaces being 50x50
		// pixels, same logic for the button array size applies here for how many
		// barriers and nodes there are
		scene = new Scene(root, screenSize, screenSize);
		int yVal = 0;
		for (int i = 0; i < (selection * 2) - 1; i += 1) {
			int xVal = 0;
			for (int j = 0; j < (selection * 2) - 1; j += 1) {
				String style = "-fx-border-color: black;";
				buttons[i][j] = new Button();
				final int fi = i, fj = j;
				buttons[i][j].setOnAction(e -> {
					if (!won) {
						// due to the way the button board the view shows is set up, buttons in even
						// columns and even rows are spots players can move to, and when the row and
						// column value are even and odd and vice versa it is a barrier, otherwise it's
						// a decorative button in between real buttons and is ignored

						// it divides the value of the button here by two because the barriers double
						// the size of the columns and rows, integer division also takes care of when
						// it's an odd value
						if (fi % 2 == 0 && fj % 2 == 0) {
							game.move(fi / 2, fj / 2);
							// handles the movement of the players
						} else if ((fi % 2 == 0 && fj % 2 == 1) || (fi % 2 == 1 && fj % 2 == 0)) {
							if (fi % 2 == 0) {

								game.createBarrier(fi / 2, fj / 2, 'r');

							} else {

								game.createBarrier(fi / 2, fj / 2, 'd');

							}

						}
					} else {
						alert = new Alert(AlertType.NONE, "You can not move after the game is over!", ButtonType.CLOSE);
						alert.show();
						// alerts the player that the game is over so they can not move, but the reset
						// and size buttons are still enabled
					}
				});
				int x, y;
				if (i % 2 == 0) {
					y = 50;
				} else {
					y = 10;

				}
				if (j % 2 == 0) {
					x = 50;
				} else {
					x = 10;

				}
				// determines what type of button it is based on it's spot in the grid, even
				// rows have player spots so barriers also must be the same height, and same way
				// for columns with player spots, decoration buttons in between are double-odd
				// so they are just 10x10
				if (x == 10 || y == 10) {
					if (!(x == 10 && y == 10)) {
						style += "-fx-background-color: green; ";

					}
				}
				buttons[i][j].setStyle(style);
				buttons[i][j].setPrefSize(x, y);
				buttons[i][j].setShape(new Rectangle(x, y));
				grid.add(buttons[i][j], xVal, yVal);
				xVal += x;
			}

			if (i % 2 == 0) {
				yVal += 50;
			} else {
				yVal += 10;
			}
		}
		buttons[game.getp1X() * 2][game.getp1Y() * 2].setStyle("-fx-border-color: black; -fx-color: red;");
		buttons[game.getp2X() * 2][game.getp2Y() * 2].setStyle("-fx-border-color: black; -fx-color: blue;");
		// asks the model for the player's locations to make sure they are synced
		// correctly, but the view is about twice as large due to the barrier buttons
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * 
	 * Method that handles resizing the game board
	 * 
	 * @param arg0
	 */
	@Override
	public void handle(ActionEvent arg0) {
		selection = choices.getSelectionModel().getSelectedItem();
		start(stage);
		// allows the screen size to be reset when you choose a new board size because
		// how big the window is, is based on how many places you can move to
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * 
	 * Method that handles the different property changes sent by the model, all the
	 * decisions of the state of the board originate in a message from the model
	 * 
	 * @param evt
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("win")) {
			// multiple cases use both the new and old values of the propertyChange to
			// retrieve distinct information it needs, rather than actual new and old values
			won = true;
			int[] temp = (int[]) evt.getNewValue();
			if ((boolean) evt.getOldValue()) {
				output.setText("Red wins!");
				// only setting border color sets the button's main color the the default as
				// well
				buttons[temp[0] * 2][temp[1] * 2].setStyle("-fx-border-color: black; ");
				buttons[temp[2] * 2][temp[3] * 2].setStyle("-fx-border-color: black; -fx-background-color: red;");
			} else {
				output.setText("Blue wins!");
				buttons[temp[0] * 2][temp[1] * 2].setStyle("-fx-border-color: black; ");
				buttons[temp[2] * 2][temp[3] * 2].setStyle("-fx-border-color: black; -fx-background-color: blue;");
			}
			// adjusts the returned values to fit the dimensions of the visual board because
			// the model only tracks spots that players can move to
		} else if (evt.getPropertyName().equals("move")) {
			int[] temp = (int[]) evt.getNewValue();
			if ((boolean) evt.getOldValue()) {
				output.setText("Red's Turn");
				buttons[temp[0] * 2][temp[1] * 2].setStyle("-fx-border-color: black; ");
				buttons[temp[2] * 2][temp[3] * 2].setStyle("-fx-border-color: black; -fx-background-color: blue;");
			} else {
				output.setText("Blue's Turn");
				buttons[temp[0] * 2][temp[1] * 2].setStyle("-fx-border-color: black; ");
				buttons[temp[2] * 2][temp[3] * 2].setStyle("-fx-border-color: black; -fx-background-color: red;");
			}
		} else if (evt.getPropertyName().equals("barrier")) {
			int[] temp = (int[]) evt.getNewValue();
			// the note about adjusting also applies but it has to account for the decimal
			// shaved off by integer division when the barrier method is called
			if (game.lastDir() == 'd') {
				buttons[(temp[0] * 2) + 1][temp[1] * 2]
						.setStyle("-fx-border-color: black; -fx-background-color: darkred;");
			} else {
				buttons[temp[0] * 2][(temp[1] * 2) + 1]
						.setStyle("-fx-border-color: black; -fx-background-color: darkred;");
			}
			// dark red signifies an erected barrier

			if ((boolean) evt.getOldValue()) {
				output.setText("Red's Turn");

			} else {
				output.setText("Blue's Turn");
			}
		} else if (evt.getPropertyName().equals("fail")) {
			alert = new Alert(AlertType.NONE, (String) evt.getNewValue(), ButtonType.CLOSE);
			alert.show();
		}
	}
}
