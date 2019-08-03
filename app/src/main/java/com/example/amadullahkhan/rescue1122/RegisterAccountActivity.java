package com.example.amadullahkhan.rescue1122;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class RegisterAccountActivity extends AppCompatActivity {

    Button submitButton;
    EditText name;
    EditText userName;
    EditText password;
    EditText confirmPassword;
    EditText email;
    EditText phoneNo;

    public void registerAccount(View view) {
        if (name.getText().toString().matches("") || userName.getText().toString().matches("") || password.getText().toString().matches("") || confirmPassword.getText().toString().matches("") || email.getText().toString().matches("") || phoneNo.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(), "Provide the Required Info Please....!", Toast.LENGTH_SHORT).show();
        }
        else
        {


            /**/


            ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.whereEqualTo("username",userName.getText().toString());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e==null)
                    {
                        if(objects.size()>0)
                        {
                            Toast.makeText(RegisterAccountActivity.this, "UserName Already Exists...!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            final ParseUser user = new ParseUser();
                            user.setUsername(userName.getText().toString());
                            user.setPassword(password.getText().toString());

                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {


                                        user.put("name", name.getText().toString());

                                        user.put("emailID", email.getText().toString());
                                        user.put("phoneNo", phoneNo.getText().toString());


                                        //user.put("AcceptOrReject","Wait");
                                        user.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if(e==null) {
                                                    Toast.makeText(RegisterAccountActivity.this, "Signup Successfully.....!", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(), "user error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        //Toast.makeText(getApplicationContext(), "Signup Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });




        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        name=(EditText)findViewById(R.id.etName);
        userName=(EditText)findViewById(R.id.etUsername);
        password=(EditText)findViewById(R.id.etPassword);
        confirmPassword=(EditText)findViewById(R.id.etConfirmPassword);
        email=(EditText)findViewById(R.id.etEmail);
        phoneNo=(EditText)findViewById(R.id.etPhoneNo);
        submitButton=(Button) findViewById(R.id.btnsubmit);
    }
}
