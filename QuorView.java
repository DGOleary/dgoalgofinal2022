package aircondition;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class QuorView extends Application{
	public void start(Stage primaryStage) {
		Button[][] buttons;
		try {
			buttons= new Button[7][7];
			BorderPane root = new BorderPane();
			GridPane grid = new GridPane();
			Scene scene = new Scene(root,230,230);
			//grid.setGridLinesVisible(true);
			root.setCenter(grid);
			//grid.setHgap(5);
			//grid.setVgap(5);
			//grid.setPadding(new Insets(10, 10, 10, 10)); 
			int yVal=0;
			for(int i=0;i<7;i+=1) {
				int xVal=0;
				for(int j=0;j<7;j+=1) {
					String style="-fx-border-color: black;";
					buttons[i][j] = new Button();
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
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
