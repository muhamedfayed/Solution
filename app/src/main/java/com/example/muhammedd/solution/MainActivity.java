package com.example.muhammedd.solution;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {

    public static String signedin_username = null;

    private EditText username_field, password_field;
    private Button signin_btn, create_btn;
    ProgressDialog progressDialog;
    DBConnector dbConnector;
    Connection con;

    String uri = "android.resource://com.example.muhammedd.solution/"+R.raw.videoo;

    // MediaPlayer instance to control playback of video file.
    private MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        username_field = (EditText) findViewById(R.id.username_field);
        password_field = (EditText) findViewById(R.id.password_field);
        signin_btn = (Button) findViewById(R.id.signin_btn);
        create_btn = (Button) findViewById(R.id.createaccountBtn);


        dbConnector = new DBConnector();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setTitle("Signing in");
        progressDialog.setCancelable(false);

//        Bundle extras = getIntent().getExtras();
//        if(extras!=null){
//            username_field.setText(extras.getString("username").toString());
//        }

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

    private void initView() {
        TextureView textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        Surface surface = new Surface(surfaceTexture);

        try {
            // AssetFileDescriptor afd = getAssets().openFd(FILE_NAME);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, Uri.parse(uri));
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();

            // Play video when the media source is ready for playback.
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (Exception e){
            Log.i("Video","video didn't started");
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
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
                }finally {
                    progressDialog.dismiss();
                }

            }

            return z;
        }



    }


}


