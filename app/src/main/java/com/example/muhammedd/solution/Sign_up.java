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
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sign_up extends Activity implements TextureView.SurfaceTextureListener {

    EditText usernameField,passField,pass2Field;
    Button create_btn,signin_btn;
    ProgressDialog progressDialog;
    DBConnector dbConnector;
    Connection con = null;

    // MediaPlayer instance to control playback of video file.
    private MediaPlayer mMediaPlayer;
    String uri = "android.resource://com.example.muhammedd.solution/"+R.raw.videoo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

        usernameField = (EditText) findViewById(R.id.username_cField);
        passField = (EditText) findViewById(R.id.password_cField);
        pass2Field = (EditText) findViewById(R.id.password2_cField);
        create_btn = (Button) findViewById(R.id.create_cbtn);
        signin_btn = (Button) findViewById(R.id.signIn_Btn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your account");
        progressDialog.setTitle("Please wait");
        progressDialog.setCancelable(false);

        dbConnector = new DBConnector();

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAcc createAcc = new CreateAcc();
                createAcc.execute("");

            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

    public class CreateAcc extends AsyncTask<String,String,String>{

        String z = "";
        boolean isSucc = false;
        String username = usernameField.getText().toString();
        String password = passField.getText().toString();
        String password2 = pass2Field.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(Sign_up.this, s, Toast.LENGTH_SHORT).show();
            if (isSucc) {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            if(username.trim().equals("")||password.trim().equals("")||password2.trim().equals(""))
                z = "Please fill the fields above";
            else if (!password.trim().equals(password2))
                z = "the password doesn't match";
            else {
                try {

                    con = dbConnector.connect();
                    String query = "INSERT INTO Users (username , password) VALUES ('"+username+"' , '"+password+"');";
                    Statement statement = con.createStatement();
                    statement.executeUpdate(query);
                    z = "Done !";
                    isSucc = true;
                    con.close();

                }catch (SQLException e){
                    z = "This username is already exist";
                    isSucc = false;
                }catch (Exception e){
                    z = e.getMessage();
                    Log.i("Exeption Signup",z);
                    isSucc = false;
                }
            }


            return z;
        }
    }
}
