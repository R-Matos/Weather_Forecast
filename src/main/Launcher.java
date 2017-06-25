package main;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.InformationPage;
import views.LocationInputPage;

/**
* This program allows you to get a forecast from a postcode.
* A postcode estimate is determined from IP or user may input their own.
* 
* IP is determine via http://ipinfo.io
* MapQuest API is used to get coordinates from the post code
* OpenWeatherMap API is used to get a forecast from the coordinates 
*
* @author  Ricardo Matos
* @version 1.0
* @since   23-06-2017
*/

public class Launcher extends Application{
	
	private static boolean isDataCollected = false;							//Used as flag to determine thread progress
	private static Coordinates coords;
	private static Forecast forecast;
	private static InformationPage informationPane;
	private static LocationInputPage locationInputPane;
	private static Scene locationInputScene, informationScene;
	private static Stage theStage;
	private static String locationInputCSS, informationCSS, postcode;
	
	
	public static void main(String args[]) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.theStage = primaryStage;
		
		//Gets postcode from internet service provider from IP
		postcode = findPostcode();
		
		//Loads stylesheets
		locationInputCSS = this.getClass().getResource("/views/LocationInputStyle.css").toExternalForm();
		informationCSS = this.getClass().getResource("/views/InformationStyle.css").toExternalForm();
     
		//Setup initial scene (page)
		locationInputPane = new LocationInputPage();
		locationInputScene = new Scene(locationInputPane, 200, 180);
		locationInputScene.getStylesheets().add(locationInputCSS);
       
		//Setup stage
        primaryStage.setTitle("Weather Forecast");
        primaryStage.setScene(locationInputScene);
        primaryStage.setResizable(false);
        primaryStage.show();
		
	}
	
	public static String getPostcode() {
		return postcode;
	}
	public static void setPostcode(String userSelectedPostcode) {
		postcode = userSelectedPostcode;
	}
	
	
	/**
	 * Estimates closest postcode from IP.
	 * Data parsed from http://ipinfo.io/
	 * Loops till validity check is passed.
	 *
	 * @return postcode which is the postcode
	 * @since 1.0
	 */
	private static String findPostcode() {
		
		boolean isPostcodeValid = false;
		String postcode = "";
		
		while (!isPostcodeValid) {
			
			Document doc = null;
			
			try {
				doc = Jsoup.connect("http://ipinfo.io/").get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Element docText = doc.select("tr").get(4);
			
			postcode = docText.text();
			postcode = postcode.substring(12);								//Removes postcode subtitle
			
			isPostcodeValid = postcodeValidityCheck(postcode);
		}
		
		return postcode;
	}
	
	/**
	 * Estimates closest postcode from IP.
	 * Data parsed from http://ipinfo.io/
	 * Loops till validity check is passed.
	 *
	 * @param postcode the postcode to check validity
	 * @return boolean whether check has passed
	 * @since 1.0
	 */
	private static boolean postcodeValidityCheck(String postcode) {
		
		if (postcode.contains("/") || postcode.contains(".")) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
	/**
	 * Changes scene to information page which contains the forecast
	 * Collects data from new thread to prevent window freezing
	 * Waits till data is collected meaning thread is finished
	 *
	 * @param postcode the postcode user submitted
	 * @since 1.0
	 */
	public static void informationPage(String postcode) {
		
		//Thread obtains data, prevents window from freezing
		Thread thread = new Thread(){
		    public void run(){
				coords = new Coordinates(postcode);
				forecast = new Forecast(coords);
				isDataCollected = true;
		    }
		  };
		  
		thread.start();

		// Wait till thread is finished, prevents window from freezing
		while (!isDataCollected) {
			thread.isAlive();
		}
		
		//Validity check. If not valid sets scene to initial, if is sets scene to information page.
		if (!coords.isValid()) {
			locationInputPane.notifyInputError();
		} else {
			informationPane = new InformationPage(forecast);
			informationScene = new Scene(informationPane, 650, 190);
			informationScene.getStylesheets().add(informationCSS);
			theStage.setScene(informationScene);
		}
	}
	
	
	public static void locationInputPage() {
		locationInputPane = new LocationInputPage();
		locationInputScene = new Scene(locationInputPane, 200, 180);
		locationInputScene.getStylesheets().add(locationInputCSS);
		theStage.setScene(locationInputScene);
	}
	
	

}
