package com.example.amadullahkhan.rescue1122;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MainPage1 extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener locationListener;
    Location requestLocation;
    Button buttonCall;
    int active=1;




    public void loginActivity(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Loginpage2.class);
        startActivity(intent);



    }


    public void callRequest(View view)
    {
        if(active==1) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("You Want to Make A Call?");
            alertDialogBuilder
                    .setMessage("Click yes to Proceed!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    gettingLocation();
                                    anonymousLogin();
                                    // Toast.makeText(getApplicationContext(), requestLocation+"", Toast.LENGTH_LONG).show();
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("You Want to Cancel Call?");
            alertDialogBuilder
                    .setMessage("Click yes to Proceed!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    deleteRequest();
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }


    public void gettingLocation() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                requestLocation=location;
                return;



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
        /**/




        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainPage1.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
            else{

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastKnownLocation!= null)
                {
                    requestLocation=lastKnownLocation;
                }

            }


        }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    requestLocation=lastKnownLocation;
                }
            }
        }
    }

//create method onCreate and pass instance from bundle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);
        buttonCall=(Button)findViewById(R.id.buttoncall);

    }
    //for exit application onBackPressed
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



//call emergency with anonymous login with parse  
    public void anonymousLogin()
    {
        if(requestLocation!=null) {
            if (ParseUser.getCurrentUser() == null) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException pe) {
                        if (pe == null) {
                            ParseUser.getCurrentUser().put("riderOrdriver","rider");
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null)
                                    {
                                        Toast.makeText(MainPage1.this, "Anonymous Login Successfull", Toast.LENGTH_SHORT).show();
                                        ParseObject activeRequest=new ParseObject("ActiveRequest");
                                        activeRequest.put("username",ParseUser.getCurrentUser().getUsername());
                                        ParseGeoPoint activeDriverLocation = new ParseGeoPoint(requestLocation.getLatitude(), requestLocation.getLongitude());
                                        activeRequest.put("location", activeDriverLocation);
                                        activeRequest.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {


                                                    active=0;
                                                    buttonCall.setBackgroundResource(R.drawable.endcallbtn);

                                                } else {
                                                    Toast.makeText(MainPage1.this, "Something Went Wrong...Please Try Again Later", Toast.LENGTH_SHORT).show();
                                                    //mySwitch.setChecked(false);
                                                }


                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(MainPage1.this, "Error... Try Again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                        }else {
                            Toast.makeText(MainPage1.this, "Anonymous Login Unsuccessfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else
            {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        anonymousLogin();
                    }
                });
               // Toast.makeText(this, "Jhaa", Toast.LENGTH_SHORT).show();


            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Location is null", Toast.LENGTH_SHORT).show();
        }
    }


//cancel request
    public void deleteRequest()
    {
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("ActiveRequest");
        //query.whereEqualTo("busno","2");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0)
                    {
                        for(ParseObject object:objects)
                        {
                            object.deleteInBackground();

                        }
                        ParseUser.logOutInBackground(new LogOutCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(MainPage1.this, "Request has been Removed...!", Toast.LENGTH_SHORT).show();
                                active=1;
                                buttonCall.setBackgroundResource(R.drawable.emergencycallbtn);
                            }
                        });


                    }
                }
                else
                {

                    Toast.makeText(MainPage1.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
