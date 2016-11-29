package com.example.muhammedd.solution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NewQuestion extends AppCompatActivity {

    EditText problemtitle_Field,problemdes_Field;
    Button addproblem_Btn;
    ProgressDialog progressDialog;

    DBConnector dbConnector = null;
    Connection con = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        problemtitle_Field = (EditText) findViewById(R.id.problemtitle_field);
        problemdes_Field = (EditText) findViewById(R.id.problemdes_field);
        addproblem_Btn = (Button) findViewById(R.id.addproblem_btn);

        dbConnector = new DBConnector();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding your problem");
        progressDialog.setTitle("Please wait");
        progressDialog.setCancelable(false);

        addproblem_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NQuestion question = new NQuestion();
                question.execute("");

            }
        });
    }

    public class NQuestion extends AsyncTask<String,String,String>{

        String z = "";
        boolean isSucc = false;
        String problem_title = problemtitle_Field.getText().toString();
        String problem_des = problemdes_Field.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isSucc){
                startActivity(new Intent(getApplicationContext(),Home.class));
                finish();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {

            if(problem_title.trim().equals("")||problem_des.trim().equals(""))
                z = "Please fill the fields above";
            else{
                try{
                    con = dbConnector.connect();
                    String query = "INSERT INTO Problems (problemHead , problemBody , problemBy) VALUES ('"+problem_title+"' , '"+problem_des+"' , '"+MainActivity.signedin_username+"');";
                    Statement statement = con.createStatement();
                    statement.executeUpdate(query);
                    z = "Your problem has been added successfully";
                    isSucc = true;
                } catch (SQLException e) {
                    z = "error";
                    e.printStackTrace();
                }finally {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.i("SQL connection","problem in con.close()");
                    }
                }
            }

            return z;
        }
    }
}
