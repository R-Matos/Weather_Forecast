package views;



import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.Launcher;

public class LocationInputPage extends BorderPane {
	
	private static boolean colourChangeFlag = true;
	private static VBox topPanel,bottomPanel;
	private static Label instructionLabel;
	private static TextField postcodeField;
	private static Button submitButton;
	private static Image image;
	private static ImageView imageView;
	
	BorderPane borderPane;
	
	
	public LocationInputPage() {
		
		borderPane = this;
		
		//Top ####################################################################################################
		image = new Image("file:resources/SunToRain.gif");
		imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
		
		instructionLabel = new Label("Enter your postcode");
		instructionLabel.setId("instructionLabel");

		topPanel = new VBox();
        topPanel.getChildren().addAll(imageView, instructionLabel);
        topPanel.setAlignment(Pos.CENTER);
        
		this.setTop(topPanel);
		//Top ####################################################################################################
		
		
		//Centre #################################################################################################
		postcodeField = new TextField();
		postcodeField.setText(Launcher.getPostcode());
		postcodeField.deselect();
		this.setCenter(postcodeField);
		
		//Below is used to remove focus from postcodeField
		final BooleanProperty firstTime = new SimpleBooleanProperty(true);
		
		postcodeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue && firstTime.get()) {
				this.requestFocus(); 				// Delegate the focus to container
				firstTime.setValue(false); 			// Variable value changed for future references
			}
		});
		//Centre #################################################################################################
		
		
		//Bottom #################################################################################################
		submitButton = new Button("Submit");
		
		bottomPanel = new VBox();
		bottomPanel.getChildren().add(submitButton);
		bottomPanel.setAlignment(Pos.CENTER);
		bottomPanel.setPadding(new Insets(10, 0, 10, 0));
		this.setBottom(bottomPanel);
		
		submitButton.setOnAction(e -> submitClicked(e));
		//Bottom #################################################################################################
		
		
		//Background change ######################################################################################
		//Animates background to match gif
		Timeline tl = new Timeline();
        tl.setCycleCount(Animation.INDEFINITE);
        KeyFrame changeBackground = new KeyFrame(Duration.seconds(.479),
                new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent event) {

                        if (colourChangeFlag) {
                        	borderPane.setStyle("-fx-background-color: #6F6F6F");
                        	colourChangeFlag = false;
                        } else {
                        	borderPane.setStyle("-fx-background-color: #00A9E0");
                        	colourChangeFlag = true;
                        }

                    }
                });

        tl.getKeyFrames().add(changeBackground);
        tl.play();
		//Background change #####################################################################################
		
	}
	

	
	private void submitClicked(ActionEvent e) {
		Launcher.informationPage(postcodeField.getText());
	}
	
	
	//Called when postcode is invalid. TextField is highlighted red and set focus.
	public void notifyInputError() {
		postcodeField.setStyle("-fx-border-color: red;");
		postcodeField.requestFocus();
	}
	
	

}
