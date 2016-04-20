package loc.developer.vladimiry.sunshine;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);


        return rootView;
    }
}
