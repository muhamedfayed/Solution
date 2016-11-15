package com.example.muhammedd.solution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    Connection con = null;
    DBConnector dbConnector = null;
    ArrayList<Problem> arrayList = null;
    RecyclerView recyclerView;
    ProblemsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        arrayList = new ArrayList<Problem>();
        dbConnector = new DBConnector();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            AllQuestions allQuestions = new AllQuestions();
            allQuestions.execute("");

        mAdapter = new ProblemsAdapter(arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Problem problem = arrayList.get(position);
                Intent intent = new Intent(getApplicationContext(),Solve_Problem.class);
                intent.putExtra("problem",(Serializable) problem);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    public class AllQuestions extends AsyncTask<String, String, String> {

        String z = "";
        boolean isSucc = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {

            con = dbConnector.connect();

            try {
                Statement st = con.createStatement();
                String sql = "SELECT ID, problemHead, problemBody, problemBy, soulution, solvedBy FROM Problems";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    Problem temp = new Problem();
                    temp.setId(rs.getInt("ID"));
                    temp.setProblemHead(rs.getString("problemHead"));
                    Log.i("problem head",temp.problemHead);
                    temp.setProblemBody(rs.getString("problemBody"));
                    temp.setProblemBy(rs.getString("problemBy"));
                    temp.setSoulution(rs.getString("soulution"));
                    temp.setSolvedBy(rs.getString("solvedBy"));
                    arrayList.add(temp);
                }

                mAdapter.notifyDataSetChanged();


            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("Error log","ERRRROR");
            }


            return z;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(getApplicationContext(), NewQuestion.class));
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
