package main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javafx.scene.image.Image;

public class Forecast {
	
	private static final String key = "268ffd0c5f0d04fae0c911ece2cce4f2";	//openweathermap api key
	
	private static Image icon;
	private static Calendar sunriseTime, sunsetTime, lastUpdated;
	private static double latitude, longitude, windDirectionDegrees;;
	private static String cityID, windName, windValue, windDirectionCode, windDirection, cloudsValue, cloudsDescriptor,	weatherID, 
				weatherDescriptor, weatherIcon, location, temperature, maxTemp, minTemp, rainfall, humidity, pressure, visibility;
	
	
	public Forecast(Coordinates coords) {
	
		String url = "http://api.openweathermap.org/data/2.5/weather?lat="+coords.getLatitude()+"&lon="+coords.getLongitude()+"&units=metric&mode=xml&APPID="+ key;
		
		System.out.println(url);
		
		Document openWeatherMap = getJSoupDocument(url);
		
		this.latitude = parseLatitude(openWeatherMap);
		this.longitude = parseLongitude(openWeatherMap);
		this.location = parseLocation(openWeatherMap);
		this.temperature = parseTemperature(openWeatherMap);
		this.maxTemp = parseMaxTemp(openWeatherMap);							
		this.minTemp = parseMinTemp(openWeatherMap);							
		this.rainfall = parseRainfall(openWeatherMap);
		this.humidity = parseHumidity(openWeatherMap);
		this.pressure = parsePressure(openWeatherMap);
		this.visibility = parseVisibility(openWeatherMap);
		this.cityID = parseCityID(openWeatherMap);
		this.sunriseTime = parseDate(openWeatherMap, "sun","rise");
		this.sunsetTime = parseDate(openWeatherMap, "sun", "set");
		this.windName = parseWindName(openWeatherMap);
		this.windValue = parseWindValue(openWeatherMap);							//in mph
		this.windDirectionDegrees = parseWindDirectionDegrees(openWeatherMap);
		this.windDirectionCode = parseWindDirectionCode(openWeatherMap);			//E.G: E for east
		this.windDirection = parseWindDirection(openWeatherMap);					//E.G: East					
		this.cloudsValue = parseCloudsValue(openWeatherMap);						//'Cloudiness'					
		this.cloudsDescriptor = parseCloudsDescriptor(openWeatherMap);
		this.weatherID = parseWeatherID(openWeatherMap);							//Weather condition					
		this.weatherDescriptor = parseWeatherDescriptor(openWeatherMap);			//Weather condition name					
		this.weatherIcon = parseWeatherIcon(openWeatherMap);						//Icon ID
		this.lastUpdated = parseDate(openWeatherMap, "lastupdate", "value");
		this.icon = parseIcon(openWeatherMap);
	}
	
	
	private Document getJSoupDocument(String url) {
		
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	
	
	private double parseLatitude(Document doc) {
		
		double lat = Double.parseDouble(doc.select("coord").first().attr("lat"));
		
		return lat;
	}
	
	private double parseLongitude(Document doc) {
		
		double lon = Double.parseDouble(doc.select("coord").first().attr("lon"));
		
		return lon;
	}
	
	private String parseLocation(Document doc) {
		
		String cityName = doc.select("city").first().attr("name");
		String country = doc.select("country").first().text();
		
		String location = cityName + ", " + country;
		
		return location;
	}
	
	
	private String parseTemperature(Document doc) {
		
		String temperature = doc.select("temperature").first().attr("value");
		String unit = "°C";
		
		//Below rounds value to nearest decimal place
		double value = Double.parseDouble(temperature);
		double valueRounded = (double) Math.round(value * 10) / 10;
		
		return valueRounded + " " + unit;
	}
	
	
	private String parseMaxTemp(Document doc) {
		
		String maxTemp = doc.select("temperature").first().attr("max");
		
		return maxTemp;
	}
	
	
	private String parseMinTemp(Document doc) {
		
		String minTemp = doc.select("temperature").first().attr("min");
		
		return minTemp;
	}
	
	
	private String parseRainfall(Document doc) {
		
		String willRain = doc.select("precipitation").first().attr("mode");
		
		return willRain;
	}
	
	
	private String parseHumidity(Document doc) {
		
		String value = doc.select("humidity").first().attr("value");
		String unit = doc.select("humidity").first().attr("unit");
		
		return value + " " +unit;
	}
	
	
	private String parsePressure(Document doc) {
		
		String value = doc.select("pressure").first().attr("value");
		String unit = doc.select("pressure").first().attr("unit");
		
		return value + " " + unit;
	}
	
	
	private String parseVisibility(Document doc) {
	
		String valueStr = doc.select("visibility").first().attr("value");
		String unit = "km";
		
		//Below rounds value to nearest decimal place
		double valueKM = Integer.parseInt(valueStr) / 1000;
		double valueRounded = (double) Math.round(valueKM * 10) / 10;
		
		return valueRounded + " " + unit;
	}
	
	
	private String parseCityID(Document doc) {
		
		String id = doc.select("city").first().attr("id");
		
		return id;
	}
	
	
	private Calendar parseDate(Document doc, String cssQuery, String attribute) {
		
		String rawTime = doc.select(cssQuery).first().attr(attribute);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = null;
		
		try {
			date  = dateFormat.parse(rawTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		GregorianCalendar gregCal = new GregorianCalendar();
		gregCal.setTime(date);
		
		Calendar cal = gregCal;
		
		return cal;
	}
	
	
	private String parseWindName(Document doc) {
		
		String name = doc.select("wind").select("speed").first().attr("name");
		
		return name;
	}
	
	
	private String parseWindValue(Document doc) {
		
		String value = doc.select("wind").select("speed").first().attr("value");
		String unit = "mph";
		
		return value + " " + unit;
	}
	
	
	private Double parseWindDirectionDegrees(Document doc) {
		
		String angleStr = doc.select("wind").select("direction").first().attr("value");
		Double angle = Double.parseDouble(angleStr);
		
		return angle;
	}
	
	
	private String parseWindDirectionCode(Document doc) {
		
		String code = doc.select("wind").select("direction").first().attr("code");
		
		return code;
	}
	
	
	private String parseWindDirection(Document doc) {
		
		String code = doc.select("wind").select("direction").first().attr("name");
		
		return code;
	}
	

	private String parseCloudsValue(Document doc) {
		
		String value = doc.select("clouds").first().attr("value");
		
		return value;
	}
	
	
	private String parseCloudsDescriptor(Document doc) {
		
		String description = doc.select("clouds").first().attr("name");
		
		//Capitalises first word
		description = description.substring(0,1).toUpperCase() + description.substring(1);
		
		return description;
	}
	
	
	private String parseWeatherID(Document doc) {
		
		String id = doc.select("weather").first().attr("number");
		
		return id;
	}
	
	
	private String parseWeatherDescriptor(Document doc) {
		
		String description = doc.select("weather").first().attr("value");
		
		//Capitalises first word
		description = description.substring(0,1).toUpperCase() + description.substring(1);
		
		return description;
	}
	
	
	private String parseWeatherIcon(Document doc) {
		
		String icon = doc.select("weather").first().attr("icon");
		
		return icon;
	}
	
	
	private Image parseIcon(Document doc) {
		
		//https://openweathermap.org/weather-conditions
		//https://www.wunderground.com/weather/api/d/docs?d=resources/icon-sets
		String url = "http://openweathermap.org/img/w/" + weatherIcon +".png";
		Image image = new Image(url);
		
		return image;
	}


	public static double getLatitude(){
		return latitude;
	}
	
	public static double getLongitude() {
		return longitude;
	}
	
	public static String getCityID() {
		return cityID;
	}


	public static Calendar getSunriseTime() {
		return sunriseTime;
	}


	public static Calendar getSunsetTime() {
		return sunsetTime;
	}


	public static String getWindName() {
		return windName;
	}


	public static String getWindValue() {
		return windValue;
	}


	public static Double getWindDirectionDegrees() {
		return windDirectionDegrees;
	}


	public static String getWindDirectionCode() {
		return windDirectionCode;
	}


	public static String getWindDirection() {
		return windDirection;
	}


	public static String getCloudsValue() {
		return cloudsValue;
	}


	public static String getCloudsDescriptor() {
		return cloudsDescriptor;
	}


	public static String getWeatherID() {
		return weatherID;
	}


	public static String getWeatherDescriptor() {
		return weatherDescriptor;
	}


	public static String getWeatherIcon() {
		return weatherIcon;
	}


	public static Calendar getLastUpdated() {
		return lastUpdated;
	}


	public static String getLocation() {
		return location;
	}


	public static String getTemperature() {
		return temperature;
	}


	public static String getMaxTemp() {
		return maxTemp;
	}


	public static String getMinTemp() {
		return minTemp;
	}


	public static String getRainfall() {
		return rainfall;
	}


	public static String getHumidity() {
		return humidity;
	}


	public static String getPressure() {
		return pressure;
	}


	public static String getVisibility() {
		return visibility;
	}


	public static Image getIcon() {
		return icon;
	}
	
}
	
	/*
	 * Elements spanTags = wundergroundDoc.getElementsByTag("span");
		
		for (Element span : spanTags) {
			
			//System.out.println(span);
			
			if (span.attr("data-variable").equals("visibility")) {
				String content = span.text();
				System.out.println(content);
				break;
			}
			
			
		}
	 * 
	 * 
	 * 
	 * private String parseLocation(Document doc) {
		Element location = doc.select("h1.city-nav-header").first();
		return location.text();
	}
	
	private String parseTemperature(Document doc) {

		Elements divTags = doc.getElementsByTag("div");
		
		for (Element div : divTags) {
			if (div.attr("id").equals("curTemp")) {
				return div.text();
				
			}
		}
		
		return null;
	}
	
	private String parseHighTemp(Document doc) {
		String high = doc.select("div[class=small-6 columns]").select("strong.high").text() ;
		return high;
	}
	
	private String parseLowTemp(Document doc) {
		return "empty";
	}
	
	private String parseImageURL(Document doc) {
		
		
		String high = doc.select("div[id=curIcon]").select("img.wx-data").first().attr("src");
		
		System.out.println(high);
		
		
//		Elements photos = doc.select("div");
//		
//		if(photos!=null) {                   
//		    String imgSrc1 = photos.select("img").first().attr("src");
//		    System.out.println(imgSrc1);
//		}
//		Elements divTags = doc.getElementsByTag("div");
//		
//
//		for (Element div : divTags) {
//			if (div.attr("id").equals("curIcon")) {
//				return div.text();
//				
//			}
//		}
		
		return high;
	}
	*/
	