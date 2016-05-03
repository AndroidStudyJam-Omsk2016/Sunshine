package loc.developer.vladimiry.sunshine;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.concurrent.Exchanger;

import loc.developer.vladimiry.sunshine.core.OpenWeatherMapParam;
import loc.developer.vladimiry.sunshine.data.WeatherContract;
import loc.developer.vladimiry.sunshine.data.WeatherContract.WeatherEntry;

/**
 * Created by User on 22.04.2016.
 */
public class FetchWeatherTask extends AsyncTask<OpenWeatherMapParam, Void, Void> {

    private final Context mContext;

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    public FetchWeatherTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(OpenWeatherMapParam... params) {

        if (params.length == 0) {
            return null;
        }


        final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String QUERY_PARAM = "q";
        final String MODE_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String CNT_PARAM = "cnt";
        final String APPID_PARAM = "appid";

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

//        // dummy
//        forecastJsonStr = "{\"city\":{\"id\":1496153,\"name\":\"Kirov\",\"coord\":{\"lon\":73.400002,\"lat\":55},\"country\":\"RU\",\"population\":0},\"cod\":\"200\",\"message\":0.0085,\"cnt\":7,\"list\":[{\"dt\":1461308400,\"temp\":{\"day\":14.57,\"min\":11.51,\"max\":14.57,\"night\":11.51,\"eve\":14.57,\"morn\":14.57},\"pressure\":1012.18,\"humidity\":81,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":1.87,\"deg\":327,\"clouds\":8,\"rain\":4.56},{\"dt\":1461394800,\"temp\":{\"day\":19,\"min\":9.86,\"max\":19.79,\"night\":14.75,\"eve\":19.79,\"morn\":9.86},\"pressure\":1012.7,\"humidity\":89,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":2.01,\"deg\":258,\"clouds\":8,\"rain\":0.22},{\"dt\":1461481200,\"temp\":{\"day\":12.35,\"min\":9.23,\"max\":14.6,\"night\":9.39,\"eve\":14.38,\"morn\":9.23},\"pressure\":1016.74,\"humidity\":79,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":6.11,\"deg\":287,\"clouds\":0,\"rain\":3.53},{\"dt\":1461567600,\"temp\":{\"day\":11.09,\"min\":5.5,\"max\":11.22,\"night\":7.9,\"eve\":11.2,\"morn\":5.5},\"pressure\":1022.22,\"humidity\":62,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"02d\"}],\"speed\":7.97,\"deg\":276,\"clouds\":8},{\"dt\":1461654000,\"temp\":{\"day\":9.47,\"min\":4.22,\"max\":10.65,\"night\":4.35,\"eve\":10.26,\"morn\":4.22},\"pressure\":1016.92,\"humidity\":66,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":5.82,\"deg\":293,\"clouds\":68,\"rain\":0.4},{\"dt\":1461740400,\"temp\":{\"day\":9.73,\"min\":4.03,\"max\":9.73,\"night\":5.42,\"eve\":9.55,\"morn\":4.03},\"pressure\":1027.65,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":5.44,\"deg\":31,\"clouds\":0,\"rain\":1.45},{\"dt\":1461826800,\"temp\":{\"day\":9.94,\"min\":5.23,\"max\":10.36,\"night\":6.21,\"eve\":10.36,\"morn\":5.23},\"pressure\":1024.39,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":4.65,\"deg\":15,\"clouds\":76,\"rain\":1.53}]}";
//        Log.i("dummy sunshine", forecastJsonStr);
//
//        try {
//            getWeatherDataFromJson(forecastJsonStr, params[0].getLocation());
//        }
//        catch (Exception e) {
//            Log.d("dummy sunshine ex", e.getMessage().toString());
//        }
//
//
//        return null;

        try {

            Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, params[0].getLocation())
                    .appendQueryParameter(MODE_PARAM, params[0].getMode())
                    .appendQueryParameter(UNITS_PARAM, params[0].getUnits())
                    .appendQueryParameter(CNT_PARAM, Integer.toString(params[0].getCnt()))
                    .appendQueryParameter(APPID_PARAM, params[0].getAppid())
                    .build();

            URL url = new URL(uri.toString());

            Log.i(LOG_TAG, uri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            Log.i("sunshine", forecastJsonStr);
            getWeatherDataFromJson(forecastJsonStr, params[0].getLocation());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;

    }

    long addLocation(String locationSetting, String cityName, double lat, double lon) {

        long locationId;

        ContentResolver cr = mContext.getContentResolver();
        Uri uri = WeatherContract.LocationEntry.CONTENT_URI;

        Cursor locationCursor = mContext.getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI,
                new String[] {WeatherContract.LocationEntry._ID},
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[] {locationSetting},
                null );

        if(locationCursor.moveToFirst()){
            int locationIndex = locationCursor.getColumnIndex(WeatherContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIndex);
        }
        else {
            ContentValues locationValues = new ContentValues();
            locationValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, cityName);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, lat);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, lon);

            Uri insertedUri = mContext.getContentResolver().insert(
                    WeatherContract.LocationEntry.CONTENT_URI,
                    locationValues
            );

            locationId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        return locationId;
    }


    /**
     *
     * @param forecastJsonStr
     * @param locationSetting
     * @return
     * @throws JSONException
     */
    private void getWeatherDataFromJson(String forecastJsonStr, String locationSetting)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        // Location information
        final String OWM_CITY = "city";
        final String OWM_CITY_NAME = "name";
        final String OWM_COORD = "coord";

        // Location coordinate
        final String OWM_LATITUDE = "lat";
        final String OWM_LONGITUDE = "lon";

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "list";

        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WINDSPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";

        // All temperatures are children of the "temp" object.
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_WEATHER_ID = "id";

        try
        {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
            String cityName = cityJson.getString(OWM_CITY_NAME);

            JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
            double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
            double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);

            long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(weatherArray.length());

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            for(int i = 0; i < weatherArray.length(); i++) {
                long dateTime;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                double high;
                double low;

                String description;
                int weatherId;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                dateTime = dayTime.setJulianDay(julianStartDay+i);

                pressure = dayForecast.getDouble(OWM_PRESSURE);
                humidity = dayForecast.getInt(OWM_HUMIDITY);
                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                weatherId = weatherObject.getInt(OWM_WEATHER_ID);


                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                high = temperatureObject.getDouble(OWM_MAX);
                low = temperatureObject.getDouble(OWM_MIN);

                ContentValues weatherValues = new ContentValues();
                weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationId);
                weatherValues.put(WeatherEntry.COLUMN_DATE, dateTime);
                weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, humidity);
                weatherValues.put(WeatherEntry.COLUMN_PRESSURE, pressure);
                weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
                weatherValues.put(WeatherEntry.COLUMN_DEGREES, windDirection);
                weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, high);
                weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, low);
                weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, description);
                weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, weatherId);


                cVVector.add(weatherValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVector.size() + " Inserted");
            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

}

