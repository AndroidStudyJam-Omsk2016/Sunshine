package loc.developer.vladimiry.sunshine;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mForecastAdapter;

    List<String> weekForecast = Arrays.asList(
            "Today - Sunny - 88 / 63",
            "Tomorrow - Foggy - 70 / 46",
            "Weds - Cloudy - 72 / 63",
            "Thurs - Rainy - 64 / 51",
            "Fri - Foggy - 70 / 46",
            "Sat - Sunny - 76 / 68"
    );

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mForecastAdapter =  new ArrayAdapter<>(
                context,                            // The current context (this activity)
                R.layout.list_item_forecast,        // The name of the layout ID.
                R.id.list_item_forecast_textview,   // The ID of the textview to populate.
                weekForecast);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



//        String json = GetForecastJsonString(
//                "Omsk",
//                "json",
//                "metric",
//                7,
//                "6d39fd36fb138d6aca011cd3954a4c97");


        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);


        return rootView;
    }

    /**
     * Get data in json format from site openweathermap.org
     *
     * @param q     - city name and country code divided by comma, use ISO 3166 country codes
     * @param mode  - possible values are xml and html. If mode parameter is empty the format is JSON by default
     * @param units - standard, metric, and imperial units are available
     * @param cnt   - number of days returned (from 1 to 16)
     * @param appid - unique application key
     * @return
     */
    public String GetForecastJsonString(String q, String mode, String units, int cnt, String appid) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try {
            String url_string = String.format("%s?q=%s&mode=%s&units=%s&cnt=%d&appid=%s",
                    "http://api.openweathermap.org/data/2.5/forecast/daily", q, mode, units, cnt, appid);
            URL url = new URL(url_string);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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
        } catch (IOException ex) {
            Log.e("MainActivityFragment", "Error ", ex);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return forecastJsonStr;
    }


}
