package aircondition;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class QuorView extends Application
implements PropertyChangeListener, EventHandler<ActionEvent>, ChangeListener<String> {
	
	Scene scene;
	Button[][] buttons;
	GridPane grid;
	BorderPane root;
	private ComboBox<Integer> choices;
	int selection=5;
	Stage stage;
	private Label output;
	QuorModel game;
	
	public void start(Stage primaryStage) {
		try {
			game=new QuorModel(selection);
			stage=primaryStage;
			buttons= new Button[(selection*2)-1][(selection*2)-1];
			output=new Label();
			output.setText("Red's turn");
			root = new BorderPane();
			grid = new GridPane();
			choices = new ComboBox<Integer>();
			choices.getItems().addAll(3, 5, 7, 9, 11);
			choices.setOnAction(this);
			choices.setValue(selection);
			int screenSize=(selection*50)+((selection-1)*10);
			//makes the screen size based on barriers being 50x10 and spaces being 50x50 pixesl
			scene = new Scene(root,screenSize,screenSize);
			root.setCenter(grid);
			root.setTop(choices);
			root.setBottom(output);
			int yVal=0;
			for(int i=0;i<(selection*2)-1;i+=1) {
				int xVal=0;
				for(int j=0;j<(selection*2)-1;j+=1) {
					String style="-fx-border-color: black;";
					buttons[i][j] = new Button();
					final int fi=i,fj=j;
					buttons[i][j].setOnAction(new EventHandler<ActionEvent>() {

					
						public void handle(ActionEvent arg0) {
							buttonPress(fi,fj);
							
						}
					
					});
					int x, y;
					if(i%2==0) {
						y=50;
					}else {
						y=10;
		
					}
					if(j%2==0) {
						x=50;
					}else {
						x=10;
						
					}
					//determines what type of button it is based on it's spot in the grid
					if(x==10||y==10) {
						if(!(x==10&&y==10)) {
							style+="-fx-background-color: #00ff00; ";

						}
					}
						buttons[i][j].setStyle(style);
					buttons[i][j].setPrefSize(x, y);
					buttons[i][j].setShape(new Rectangle(x,y));
				grid.add(buttons[i][j], xVal, yVal);
				xVal+=x;
				}
				
				if(i%2==0) {
					yVal+=50;
				}else {
					yVal+=10;
				}
			}
			buttons[0][buttons.length/2].setStyle("-fx-border-color: black; -fx-color: red;");
			buttons[buttons.length-1][buttons.length/2].setStyle("-fx-border-color: black; -fx-color: blue;");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buttonPress(int i, int j) {
		if(i % 2 == 0 && j % 2 == 0){
			System.out.println("move");
			//if (move(i, j)){
			//button(i, j) = getPlayer().color;
		//}
		}else if((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)){
			System.out.print("barrier ");
		char d;
			if(i % 2 == 0){
				d = 'r';
				System.out.println("right");
		}else{
			d='d';
			System.out.println("down");
		}
		//if (checkPath(i, j,d)){
			//button(i, j) = "red";
		//}
		}

	}
	
	public void handle(ActionEvent arg0) {
		selection = choices.getSelectionModel().getSelectedItem();
		start(stage);
		//resets screen when resized
	}
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
}
