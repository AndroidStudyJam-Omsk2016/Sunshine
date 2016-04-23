package loc.developer.vladimiry.sunshine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private View mRootView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (null != intent && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String forecast = intent.getStringExtra(Intent.EXTRA_TEXT);
            updateView(forecast);
        }
        return mRootView;
    }

    private void updateView(String forecast) {
        ((TextView)mRootView.findViewById(R.id.detail_text)).setText(forecast);
    }
}
