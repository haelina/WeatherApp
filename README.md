# WeatherApp

**Name:** Hanna Sepänmaa

**Topic:** Mobile application that shows current weather and five day weather forecast using the [OpenWeatherMap API](https://openweathermap.org/api).

- If user gives permission location coordinates are used to fetch weather data.
- User can search current weather data by city or location name.
- User can also view weather forecast for the next five days.

**Target:** Android/Kotlin

**Google Play link:** -

## Release 1: 2021-05-12 features

- User can search weather data with city or location name
- Weather data shows up if results were found
- If user types invalid search string the app informs that results are not found

## Release 2: 2021-05-21 features

- User gets weather data using mobile phone's current coordinates (with appropriate permission checks)
- User gets forecast data for next five days when searching with city or location name
- User gets forecast data for next five days when searching with mobile phone's current gps coordinates (latitude and longitude)
- Visual elements added to make appearance look better
  - Background image added
  - Buttons, text and background colors adjusted
  - Navigation buttons added so that user can choose to view current weather data or weather forecast data
  - Weather icon shows what the overall weather is like
  - Images for sunrise time and sunset time added
  - Different elements are positioned in a way that makes this app easy to use

## Known Bugs

- All the colours don't work correctly when android phone has darkmode on. I ran out of time trying to fix these.
- When getting the phone's gps coordinates they are a bit inaccurate even though I have used fine location to get these. The weather data that I got using current location is about 1-2 kilometers away from my location.

## Screencast

**Youtube:** [Go and watch the screencast](https://www.youtube.com/)
