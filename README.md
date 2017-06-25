# Weather Forecast
Gets the forecast for your area. <br />
This is achieved by obtaining the user's postcode which in turn gives us their coordinates, allowing us to obtain the forecast for that given area.

![Alt text](https://github.com/RMatos2442/Weather_Forecast/blob/master/InputPage_Screenshot.png?raw=true "Postcode input page")

![Alt text](https://github.com/RMatos2442/Weather_Forecast/blob/master/InformationPage_Screenshot.png?raw=true "Forecast information page")

Postcode is determined through: 
- user's IP (estimate) 
- user's input

Postcode 'estimate' is parsed from [ipinfo](http://ipinfo.io). <br />
Coordinates determined from postcode using the [MapQuest](https://developer.mapquest.com/) [API](https://developer.mapquest.com/documentation/). <br />
Forecast determined from coordinates using the [OpenWeatherMap](https://openweathermap.org/) [API](https://openweathermap.org/api).
