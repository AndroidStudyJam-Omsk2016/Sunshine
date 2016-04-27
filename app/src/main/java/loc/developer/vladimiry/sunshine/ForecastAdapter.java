package loc.developer.vladimiry.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import loc.developer.vladimiry.sunshine.core.Util;
import loc.developer.vladimiry.sunshine.data.WeatherContract;


/**
 * Created by User on 27.04.2016.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Util.isMetric(mContext);
        String highLowStr = Util.formatTemperature(mContext, high) + "/" + Util.formatTemperature(mContext, low);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {

        String highAndLow = formatHighLows(
                cursor.getDouble(FetchWeatherFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(FetchWeatherFragment.COL_WEATHER_MIN_TEMP));

        return Util.formatDate(cursor.getLong(FetchWeatherFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(FetchWeatherFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView tv = (TextView)view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }
}
