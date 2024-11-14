# Weather Application

## Overview
This Weather Application provides temperature data for a specified city and date using either a structured input or a free-text query. It integrates the OpenAI API to process free-text inputs, and Tomorrow.io API to fetch weather data.

## Features
- **Date-Specific Weather:** Retrieves hourly temperatures for a specified day in a chosen city.
- **Free-Text Query:** Parses a free-text description of city and date to fetch relevant weather data.


## Project Structure

- **ChatGptApi.java**: Handles requests to the OpenAI GPT API to parse free-text input into structured city and date values.
- **WeatherByDate.java**: Communicates with the Tomorrow.io API to fetch weather data:
    - **get_Specific_Weather()**: Retrieves hourly temperatures for a specified city and date.
    - **get_Real_Time_Weather()**: Retrieves the current temperature for a city.
- **Gui.java**: Builds the user interface where:
    - Users can enter city and date information or free text.
    - Weather data is displayed in a table for each hour of the specified day.

## Setup

### Prerequisites
- Java 8 or higher
- Tomorrow.io and OpenAI API keys (set as environment variables)

### Installation
1. Clone the repository.
2. Install dependencies (if any, such as OkHttp for HTTP requests).
3. Set up environment variables:
    - `API_KEY` for OpenAI (ChatGPT).
    - `TOMORROW_API_KEY` for Tomorrow.io.

### Usage
1. Run `Gui.java` to start the application.
2. In the app:
    - **Direct Input:** Enter city, year, month, and day in the fields provided.
    - **Free Text:** Enter a description (e.g., "weather in New York on 12 March") and click "Show Weather."
3. View the hourly temperature displayed in the table at the bottom of the GUI.

## Dependencies
- OkHttp
- org.json.JSONObject
- org.json.JSONArray

## Notes
- Ensure both APIs are accessible by setting up network permissions if necessary.
- For free-text input, ensure the format includes a recognizable city and date.
