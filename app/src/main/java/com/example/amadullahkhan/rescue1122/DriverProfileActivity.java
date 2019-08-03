package com.example.amadullahkhan.rescue1122;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DriverProfileActivity extends AppCompatActivity {

    ListView requestListView;
    ArrayList<String> request;
    ArrayAdapter arrayAdapter;
    TextView name;
    TextView id;
    TextView phoneNo;
    TextView emailAddress;
    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<Double> requestLatitudes=new ArrayList<>();
    ArrayList<Double> requestLongitudes=new ArrayList<>();



    public void updateListView(Location location)
    {
        //request.clear();
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("ActiveRequest");
        final ParseGeoPoint geoPoint=new ParseGeoPoint(location.getLatitude(),location.getLongitude());
        query.whereNear("location",geoPoint);
        query.whereExists("username");
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){

                    request.clear();
                    requestLongitudes.clear();
                    requestLatitudes.clear();
                    if(objects.size()>0)
                    {

                        for(ParseObject object : objects)
                        {

                            //Toast.makeText(DriverProfileActivity.this, "check", Toast.LENGTH_SHORT).show();
                            ParseGeoPoint gp=new ParseGeoPoint(object.getParseGeoPoint("location"));
                            requestLatitudes.add(gp.getLatitude());
                            requestLongitudes.add(gp.getLongitude());
                            double distanceInMiles=geoPoint.distanceInMilesTo(gp);
                            double distanceOneDP=(double)Math.round(distanceInMiles*10)/10;
                            request.add(distanceOneDP +" miles Away");

                        }

                    }
                    else
                    {
                        request.clear();
                        request.add("No Emergency Requests");
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        requestListView=(ListView) findViewById(R.id.listview);
        name=(TextView)findViewById(R.id.dpname);
        id=(TextView)findViewById(R.id.dpID);
        phoneNo=(TextView)findViewById(R.id.dpPhoneNo);
        emailAddress=(TextView)findViewById(R.id.dpEmail);
        this.setTitle("Driver Profile");

        String profileName;




        request=new ArrayList<String>();


        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,request);

        request.clear();
        request.add("Getting Requests");

        requestListView.setAdapter(arrayAdapter);

        if(ParseUser.getCurrentUser()!=null) {
            profileName = ParseUser.getCurrentUser().getString("name");
            name.setText(profileName);
            id.setText(ParseUser.getCurrentUser().getUsername());
            phoneNo.setText(ParseUser.getCurrentUser().getString("phoneNo"));
            emailAddress.setText(ParseUser.getCurrentUser().getString("emailID"));

        }

        try {

            requestListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(DriverProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(DriverProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (requestLatitudes.size() > pos & requestLongitudes.size() > pos & lastKnownLocation != null) {

                            Intent intent = new Intent(getApplicationContext(), CallLocationMapsActivity.class);
                            intent.putExtra("requestLatitude", requestLatitudes.get(pos));
                            intent.putExtra("requestLongitude", requestLongitudes.get(pos));
                            intent.putExtra("driverLatitude", lastKnownLocation.getLatitude());
                            intent.putExtra("driverLongitude", lastKnownLocation.getLongitude());
                            startActivity(intent);
                        }
                    }

                    return true;
                }
            });


           /* requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(DriverProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(DriverProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (requestLatitudes.size() > position & requestLongitudes.size() > position & lastKnownLocation != null) {

                            Intent intent = new Intent(getApplicationContext(), CallLocationMapsActivity.class);
                            intent.putExtra("requestLatitude", requestLatitudes.get(position));
                            intent.putExtra("requestLongitude", requestLongitudes.get(position));
                            intent.putExtra("driverLatitude", lastKnownLocation.getLatitude());
                            intent.putExtra("driverLongitude", lastKnownLocation.getLongitude());
                            startActivity(intent);
                        }
                    }
                }
            });*/
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateListView(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
            else{

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastKnownLocation!= null)
                {
                    updateListView(lastKnownLocation);
                }

            }


        }


    }


    public void logout(View view)
    {


        if(ParseUser.getCurrentUser()!=null) {


            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        Intent intent = new Intent(getApplicationContext(), MainPage1.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
