package loc.developer.vladimiry.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import loc.developer.vladimiry.sunshine.core.Util;
import loc.developer.vladimiry.sunshine.sync.SunshineSyncAdapter;


public class MainActivity extends AppCompatActivity implements FetchWeatherFragment.Callback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String FETCH_WEATHER_FRAGMENT_TAG = "FWFTAG";
    private final String FETCH_WEATHER_DETAIL_FRAGMENT_TAG = "FWDFTAG";
    private boolean mTwoPane;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Util.getPreferredLocation(this);
        setContentView(R.layout.activity_main);

        FetchWeatherFragment fwf = ((FetchWeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main_fragment));
        fwf.setUseTodayLayout(!mTwoPane);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.INVISIBLE);

        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment(), FETCH_WEATHER_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        SunshineSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Util.getPreferredLocation( this );
        if (location != null && !location.equals(mLocation)) {
            FetchWeatherFragment fwf = (FetchWeatherFragment)getSupportFragmentManager().findFragmentByTag(FETCH_WEATHER_FRAGMENT_TAG);
            if ( null != fwf ) {
                fwf.onLocationChanged();
            }

            DetailActivityFragment daf = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(FETCH_WEATHER_DETAIL_FRAGMENT_TAG);
            if ( null != daf ) {
                daf.onLocationChanged(location);
            }

            mLocation = location;
        }
    }

    @Override
    public void onItemSelected(Uri dateUri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, dateUri);
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, FETCH_WEATHER_DETAIL_FRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(dateUri);
            startActivity(intent);
        }
    }
}
