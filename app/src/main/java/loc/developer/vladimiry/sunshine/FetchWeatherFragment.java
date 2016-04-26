package loc.developer.vladimiry.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import loc.developer.vladimiry.sunshine.core.OpenWeatherMapParam;


/**
 * A placeholder fragment containing a simple view.
 */
public class FetchWeatherFragment extends Fragment{

    private final String LOG_TAG = FetchWeatherFragment.class.getSimpleName();
    private View mRootView;
    private ListView listView;
    private ArrayAdapter<String> mForecastAdapter;
    private FetchWeatherTask mTask;

    String[] weekForecast = new String[]{
            "Today - Sunny - 88 / 63",
            "Tomorrow - Foggy - 70 / 46",
            "Weds - Cloudy - 72 / 63",
            "Thurs - Rainy - 64 / 51",
            "Fri - Foggy - 70 / 46",
            "Sat - Sunny - 76 / 68"
    };

    public FetchWeatherFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            updateWeather();
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
//            String mode = "json";
//            String units = prefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_default));
//            int cnt = 16;
//            String appid = "6d39fd36fb138d6aca011cd3954a4c97";
//
//            OpenWeatherMapParam param = new OpenWeatherMapParam(location, mode, units, cnt, appid);
//
//            mTask = new FetchWeatherTask(this);
//            mTask.execute(param);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mForecastAdapter =
                new ArrayAdapter<>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) mRootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

        return mRootView;
    }

    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        String mode = "json";
        String units = prefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_default));
        int cnt = 16;
        String appid = "6d39fd36fb138d6aca011cd3954a4c97";

        OpenWeatherMapParam param = new OpenWeatherMapParam(location, mode, units, cnt, appid);
        weatherTask.execute(param);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }
}
