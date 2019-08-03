package com.example.amadullahkhan.rescue1122;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;



public class Loginpage2 extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;

    public void registerAccountActivity(View view)
    {

        Intent intent=new Intent(getApplicationContext(),RegisterAccountActivity.class);
        startActivity(intent);
    }

    public void login(View view)
    {
        if (editTextUsername.getText().toString().matches("") || editTextPassword.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(), "Please Fill The Required Fields...!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(ParseUser.getCurrentUser()!=null)
            {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        //Toast.makeText(Loginpage2.this, "Welcome", Toast.LENGTH_SHORT).show();
                    }
                });


            }
//check parse initialization in application class
            ParseUser.logInInBackground(editTextUsername.getText().toString(), editTextPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
  //parse exception
                    if (e == null) {
                        ParseUser.getCurrentUser().put("riderOrdriver","driver");
                        Toast.makeText(Loginpage2.this, ParseUser.getCurrentUser().getUsername()+" Has been Logged in..", Toast.LENGTH_SHORT).show();
                        //DriverlocationsenderActivity
                        Intent intent=new Intent(getApplicationContext(),DriverProfileActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Loginpage2.this, "User Has been failed to login..Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
//  already provided by super class or parent class, and allow specific implementation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage2);
        editTextUsername=(EditText)findViewById(R.id.etusername);
        editTextPassword=(EditText) findViewById(R.id.etpassword);





    }
}
