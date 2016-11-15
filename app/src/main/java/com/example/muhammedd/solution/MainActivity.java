package com.example.muhammedd.solution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    public static String signedin_username = null;

    private EditText username_field, password_field;
    private Button signin_btn, create_btn;
    ProgressDialog progressDialog;
    DBConnector dbConnector = null;
    Connection con = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_field = (EditText) findViewById(R.id.username_field);
        password_field = (EditText) findViewById(R.id.password_field);
        signin_btn = (Button) findViewById(R.id.signin_btn);
        create_btn = (Button) findViewById(R.id.createaccountBtn);

        dbConnector = new DBConnector();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setTitle("Signing in");
        progressDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            username_field.setText(extras.getString("username").toString());
        }

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                login.execute("");

            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Sign_up.class));
                finish();
            }
        });
    }


    public class Login extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSucc = false;
        String username = username_field.getText().toString();
        String password = password_field.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            if (isSucc) {
                signedin_username = username;
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();

            }
        }


        @Override
        protected String doInBackground(String... params) {

            if (username.trim().equals("") || password.trim().equals(""))
                z = "Please fill the fields above";
            else {
                try {
                    con = dbConnector.connect();
                    String query = "select * from Users where username= '" + username.toString() + "' and password = '" + password.toString() + "'  ";
                    Statement statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(query);

                    if(rs.next()){
                        z = "Login successful";
                        isSucc=true;
                        con.close();

                    }else {
                        z = "Wrong username or password!";
                        isSucc = false;
                    }
                } catch (Exception e) {
                    isSucc = false;
                    z=e.getMessage();
                }

            }

            return z;
        }



    }


}


