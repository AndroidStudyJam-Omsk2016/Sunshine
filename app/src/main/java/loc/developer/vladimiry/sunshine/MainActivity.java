package loc.developer.vladimiry.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import loc.developer.vladimiry.sunshine.core.Util;


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

        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment(), FETCH_WEATHER_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            //getSupportActionBar().setElevation(0f);
        }


        FetchWeatherFragment fwf = ((FetchWeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main_fragment));
        fwf.setUseTodayLayout(!mTwoPane);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.INVISIBLE);



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

        if (id == R.id.action_map)  {
            openPrefferedLocationInMap();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPrefferedLocationInMap()
    {
        String location = Util.getPreferredLocation(this);

        Uri geolocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geolocation);

        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
        else
        {
            Log.d(LOG_TAG, "Couldn't call " + location);
        }
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
