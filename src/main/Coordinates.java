package main;

import java.io.IOException;

import org.jsoup.Jsoup;

/**
* Obtains coordinates that correspond to postcode.
* Uses MapQuest API.
*
* @version 1.0
*/

public class Coordinates {
	
	private final static String key = "kpX1RRUAjnR7YTo6cW1f8x7qdJRmErBw";		//Map quest api key
	private String latitude;
	private String longitude;
	
	
	
	public Coordinates(String postcode) {
		postcode = formatPostcode(postcode);
		String json = getJSON("http://www.mapquestapi.com/geocoding/v1/address?key=" + key + "&location=" + postcode);
		parseJSON(json);
	}
	
	
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}


	
	private String formatPostcode(String postcode) {
		
		postcode = postcode.trim();
		postcode = postcode.replaceAll(" ", "_");
		
		return postcode;
	}
	
	
	
	private String getJSON(String url) {
		
		String json = null;
		
		try {
			json = Jsoup.connect(url).ignoreContentType(true).execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json;
	}

	
	//Gets latitude and longitude
	private void parseJSON(String json) {
		
		boolean latDone = false;
		boolean lngDone = false;
		
		for (int i = 0; i < json.length() ; i++) {
			
			if (!latDone && json.length() > i+6 && json.substring(i, i+6).equals("\"lat\":")) {
				latitude = json.substring(i+6, i+12);
				latDone = true;
			}
			
			if (!lngDone && json.length() > i+6 && json.substring(i, i+6).equals("\"lng\":")) {
				longitude = json.substring(i+6, i+12);
				lngDone = true;
			}
			
			if (latDone && lngDone) {
				break;
			}
		}
	}
	
	
	//By default incorrect postcode gives you lat=39.390, lon=-99.06
	public boolean isValid() {
		
		if (latitude.equals("39.390") && longitude.equals("-99.06")) {
			System.out.println("incorrect");
			return false;
		}
		
		return true;
	}
	
}
