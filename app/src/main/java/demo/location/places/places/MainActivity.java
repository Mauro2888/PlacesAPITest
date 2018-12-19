package demo.location.places.places;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

//data binding
import demo.location.places.places.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private static final int PLACES_BUILDER_ID = 143;
    private static final int PERMISSION_ID = 123;
    private CheckBox mCheckBoxGPS;
    private Button mAddLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            mCheckBoxGPS.setChecked(false);
        }else {
            mCheckBoxGPS.setChecked(true);
            mCheckBoxGPS.setEnabled(false);
        }
    }

    public void setupUI(){
     mCheckBoxGPS = findViewById(R.id.checkbox_gps);
     mAddLocation = findViewById(R.id.add_location);
     mAddLocation.setOnClickListener(this);
        GoogleApiClient places = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,this)
                .build();
    }

    @Override
    public void onClick(View view) {
        if (view == mAddLocation){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ID);
            PlacePicker.IntentBuilder placePicker = new PlacePicker.IntentBuilder();
            try {
                Intent intent = placePicker.build(this);
                startActivityForResult(intent,PLACES_BUILDER_ID);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_ID:
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   Toast.makeText(this, "Add location", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(this, "Please grant permission", Toast.LENGTH_SHORT).show();
               }
               break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACES_BUILDER_ID && resultCode == RESULT_OK){
            Place place = PlacePicker.getPlace(this,data);
            Toast.makeText(this, "" + place.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
