package loc.developer.vladimiry.sunshine;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class FetchWeatherFragment extends Fragment{

    private final String LOG_TAG = FetchWeatherFragment.class.getSimpleName();
    private View mMainView;
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
            mTask = new FetchWeatherTask(this);
            mTask.execute("Omsk");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) mMainView.findViewById(R.id.listview_forecast);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                Snackbar.make(view, forecast, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // or
                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_LONG).show();
            }
        });

        updateView(weekForecast);

        return mMainView;
    }

    public void updateView(String[] data) {
        mForecastAdapter =
                new ArrayAdapter<>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        data);
        listView.setAdapter(mForecastAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void updateData(String[] data) {
        if(null != data)
        {
            updateView(data);
        }
    }

}
