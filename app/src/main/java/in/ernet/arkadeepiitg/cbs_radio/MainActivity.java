package in.ernet.arkadeepiitg.cbs_radio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    static MediaPlayer mPlayer;
   ImageButton buttonPlay;
    ImageButton buttonStop;
    Button VideoBtn;

    String url = "http://172.16.101.105/radio/1080_src/index.m3u8";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = (ImageButton) findViewById(R.id.play);
        VideoBtn=(Button)findViewById(R.id.VideoButton);
        final WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        wifiLock.acquire();
        final Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);
        VideoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent=new Intent(MainActivity.this,VideoViewActivity.class);
                startActivity( myintent);
            }
        });
        buttonPlay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mPlayer = new MediaPlayer();
                mPlayer.setWakeMode(getApplicationContext(), PowerManager.FULL_WAKE_LOCK);
                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

                scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        String path = "http://172.16.101.104/namefiles/test.txt";
                        URL u = null;
                        try {
                            u = new URL(path);
                            HttpURLConnection c = (HttpURLConnection) u.openConnection();
                            c.setRequestMethod("GET");
                            c.connect();
                            InputStream in = c.getInputStream();
                            final ByteArrayOutputStream bo = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            in.read(buffer); // Read from Buffer.
                            bo.write(buffer); // Write Into Buffer.

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView text = (TextView) findViewById(R.id.TextView1);
                                    text.setText(bo.toString());
                                    try {
                                        bo.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, 0, 10, TimeUnit.SECONDS);
                try {
                    mPlayer.setDataSource(url);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                }
                mPlayer.start();
            }
        });
        buttonStop = (ImageButton) findViewById(R.id.stop);
        buttonStop.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();

                }
            }
        });
        ImageButton about_btn=(ImageButton)findViewById(R.id.About_US);
        about_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(MainActivity.this,About.class);
                startActivity(intent2);
            }
        });
        buttonStop = (ImageButton) findViewById(R.id.stop);
        buttonStop.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();

                }
            }
        });
    }


    protected void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }



}