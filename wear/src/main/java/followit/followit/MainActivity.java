package followit.followit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import static com.google.android.gms.wearable.DataMap.TAG;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener {

    private TextView mTextView;
    private GoogleApiClient googleClient;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText(String.valueOf(count));
            }
        });

        // Build a new GoogleApiClient for the Wearable API
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleClient.connect();
        //SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(googleClient, this);
        googleClient.disconnect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(googleClient, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "onConnectionSuspended: " + cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "DATA CHANGED");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    updateCount(dataMap.getInt("COUNT_EXAMPLE"));
                    Log.d(TAG, "RECU" + dataMap.getInt("COUNT_EXAMPLE"));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    // Our method to update the count
    private void updateCount(int c) {
        count += c;
    }
}
