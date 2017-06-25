package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import main.Forecast;

public class InformationPage extends BorderPane {
	
	private static BorderPane centerBorderPane;
	private static Label summary, cloudDescription, tempRanges, pressure, humidity, visibility,
			rainfall, windDescription, windValue;
	private static Text location, coords, temp, tempUnit, high, low, highTemp, lowTemp;
	private static TextFlow titleFlow, tempFlow, rangeFlow;
	private static ImageView icon;
	private static VBox iconBox, tempBox, windBox, miscBox;
	private static HBox centerBox;
	private static Circle circle;
	
	public InformationPage(Forecast forecast) {
		
		
		//Top ####################################################################
		location = new Text(forecast.getLocation());
		location.setFont(Font.font("Helvetica", FontPosture.REGULAR, 20));
		coords = new Text("    "+forecast.getLatitude()+"°N, "+forecast.getLongitude()+"°W");
		coords.setFont(Font.font("Arial", FontPosture.REGULAR, 11));
		
		titleFlow = new TextFlow(location, coords);
		titleFlow.setPadding(new Insets(10, 0, 10, 20));
		
		this.setTop(titleFlow);
		//Top ####################################################################
		
		
		//[start] Center #########################################################
		centerBox = new HBox();
		
		Region leftOuterPadding = new Region();
		leftOuterPadding.setMinWidth(20);
		centerBox.getChildren().add(leftOuterPadding);
		
		//[start] Left ------------------------------------------------------------------
		icon = new ImageView();
		icon.setImage(forecast.getIcon());
		icon.setFitWidth(75);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        
        summary = new Label(forecast.getWeatherDescriptor());
        cloudDescription = new Label(forecast.getCloudsDescriptor()+" ("+forecast.getCloudsValue()+")");
        
        iconBox = new VBox();
        iconBox.setAlignment(Pos.TOP_CENTER);
        iconBox.setPadding(new Insets(10,10,10,10));
        iconBox.getChildren().addAll(icon, summary, cloudDescription);
       
		centerBox.getChildren().add(iconBox);
		//[end] Left --------------------------------------------------------------------
	
		Region leftInnerPadding = new Region();
		leftInnerPadding.setMinWidth(20);
		centerBox.getChildren().add(leftInnerPadding);
		
		//[start] Center-Left------------------------------------------------------------
		Region shiftTempDown = new Region();
		shiftTempDown.setMinHeight(10);
		
		temp = new Text(forecast.getTemperature().substring(0, forecast.getTemperature().length()-3));
		temp.setFont(Font.font("Helvetica", FontPosture.REGULAR, 50));
		temp.setFill(determineTempColour(forecast));
		tempUnit = new Text(forecast.getTemperature().substring(forecast.getTemperature().length()-3));
		tempFlow = new TextFlow(temp, tempUnit);
		tempFlow.setTextAlignment(TextAlignment.CENTER);
		
		Region shiftRangeDown = new Region();
		shiftRangeDown.setMinHeight(8);
		
		high = new Text("    High: ");
		high.setFill(Color.RED);
		highTemp = new Text(forecast.getMaxTemp());
		low = new Text("Low: ");
		low.setFill(Color.BLUE);
		lowTemp = new Text(forecast.getMinTemp());
		rangeFlow = new TextFlow(high, highTemp, new Text("    "),low, lowTemp);
		
		tempBox = new VBox();
		tempBox.setAlignment(Pos.TOP_CENTER);
		tempBox.setPadding(new Insets(10,10,10,10));
		tempBox.getChildren().addAll(shiftTempDown, tempFlow, shiftRangeDown, rangeFlow);
		
		centerBox.getChildren().add(tempBox);
		//[end] Center-Left -----------------------------------------------------------
		
		Region rightInnerPadding = new Region();
		rightInnerPadding.setMinWidth(20);
		centerBox.getChildren().add(rightInnerPadding);
		
		//[start] Center-Right ----------------------------------------------------------
		circle = new Circle(20);
		circle.setCenterX(100.0f);
		circle.setCenterY(100.0f);
		
		Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
            0.0, 10.0,
            10.0, 10.0,
            5.0, 35.0 });
        polygon.setTranslateY(20);
        polygon.getTransforms().add(new Rotate(forecast.getWindDirectionDegrees(), 5, 35));
		
		Region shiftWindDown = new Region(); 
		shiftWindDown.setMinHeight(18);
		
		windDescription = new Label(forecast.getWindName());
		windValue = new Label(forecast.getWindValue());
		
		windBox = new VBox();
		windBox.setAlignment(Pos.TOP_CENTER);
		windBox.setPadding(new Insets(0,10,10,10));
		windBox.getChildren().addAll(polygon, circle, shiftWindDown, windDescription, windValue);
		
		
		centerBox.getChildren().add(windBox);
		//[end]Center-Right ----------------------------------------------------------

		//[start] Right ---------------------------------------------------------------- 
		humidity = new Label("Humidity:   " + forecast.getHumidity());
		rainfall = new Label("Rainfall:      " + forecast.getRainfall());
		visibility = new Label("Visibility:    " + forecast.getVisibility());
		pressure = new Label("Pressure:    " + forecast.getPressure());

		miscBox = new VBox();
		miscBox.setAlignment(Pos.TOP_LEFT);
		miscBox.setPadding(new Insets(35, 10, 0, 30));
		miscBox.getChildren().addAll(humidity, rainfall, visibility, pressure);

		centerBox.getChildren().add(miscBox);
		//[end] Right -------------------------------------------------------------------
		
		this.setCenter(centerBox);
		//[end] Center ###########################################################
		
		
		
		//Bottom #################################################################
		Region bottom = new Region();
		bottom.setMinHeight(30);
		this.setBottom(bottom);
		//Bottom #################################################################
		
		
		//[start] id's for CSS formatting
		location.setId("location");
		circle.setId("circle");
		summary.setId("summary");
		cloudDescription.setId("cloudDescription");
		tempFlow.setId("tempFlow");
		rangeFlow.setId("rangeFlow");
		windDescription.setId("windDescription");
		windValue.setId("windValue");
		humidity.setId("humidity");
		rainfall.setId("rainfall");
		visibility.setId("rainfall");
		pressure.setId("pressure");
		this.setId("mainPane");
		iconBox.setId("iconBox");
		tempBox.setId("tempBox");
		windBox.setId("windBox");
		miscBox.setId("miscBox");
		//[end]
	}
	
	
	/**
	 * Determines temperature colour from temperature range.
	 * Blue <5. Green 5-12, yellow 13-19, orange 20-29, red <29
	 * Loops till validity check is passed.
	 *
	 * @param forecast contains the temperature value
	 * @return Paint the colour fill to be used
	 * @since 1.0
	 */
	private static Paint determineTempColour(Forecast forecast) {
		
		double value = Double.parseDouble(forecast.getTemperature().substring(0, forecast.getTemperature().length()-3));
		
		if (value < 5) 
		{
			return Color.BLUE;
		}
		else if (value >= 5 && value < 13) 
		{
			return Color.GREEN;
		}
		else if (value >= 13 && value < 20) 
		{
			return Color.YELLOW;
		}
		else if (value >= 20 && value < 30) 
		{
			return Color.ORANGE;
		}
		else if (value >= 30) 
		{
			return Color.RED;
		}
		else 
		{
			throw new IllegalStateException("Temperature not in any range.");
		}
	}

	
}