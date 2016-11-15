package com.example.muhammedd.solution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Solve_Problem extends AppCompatActivity {

    private Problem problem;
    private TextView head, des, user, solution, by, solutionword;
    private EditText solve;
    private Button btn;
    Connection con = null;
    DBConnector dbConnector = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve__problem);
        Intent intent = getIntent();
        problem = (Problem) getIntent().getExtras().getSerializable("problem");
        solve = (EditText) findViewById(R.id.input_answer);
        head = (TextView) findViewById(R.id.headTxt);
        des = (TextView) findViewById(R.id.desTxt);
        user = (TextView) findViewById(R.id.userTxt);
        solution = (TextView) findViewById(R.id.soultion_Text);
        by = (TextView) findViewById(R.id.solvedBy);
        solutionword = (TextView) findViewById(R.id.solutionword);
        btn = (Button) findViewById(R.id.buttonsolve);

        head.setText(problem.getProblemHead());
        des.setText(problem.getProblemBody());
        user.setText("Problem by "+problem.getProblemBy());

        if (problem.getSoulution() != null) {
            solution.setText(problem.getSoulution());
            by.setText("Solved by "+problem.getSolvedBy());
            solution.setAlpha(1);
            by.setAlpha(1);
            solutionword.setAlpha(1);
        } else {
            btn.setAlpha(1);
            solve.setAlpha(1);
            dbConnector = new DBConnector();
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Adding the solution");
            progressDialog.setMessage("Please wait..");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddSolve addSolve = new AddSolve();
                addSolve.execute("");

            }
        });


    }

    public class AddSolve extends AsyncTask<String, String, String> {

        String z = "";
        String solution = solve.getText().toString();
        boolean isSucc = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            if (isSucc){
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            if (solution.trim().equals(""))
                z = "Please fill the fields above";
            else {
                try {
                    con = dbConnector.connect();
                    String query ="UPDATE Problems SET soulution='"+solution+"' , solvedBy='"+MainActivity.signedin_username+"' where ID="+problem.getId()+";";
                    Statement statement = con.createStatement();
                    statement.executeUpdate(query);
                    isSucc = true;
                    z = "Thanks for your help !";
                } catch (Exception e) {
                    isSucc = false;
                    z = "Failed to add the solution";
                    z = e.getMessage();
                }finally {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return z;
        }
    }

}
