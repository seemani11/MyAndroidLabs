package algonquin.cst2335.thap0011;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private String apiKey = "7e943c97096a9784391a981c4d878b22";
    /** This string represents the address of the server we will connect to */
    private String stringURL;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button forecastBtn = findViewById(R.id.foreCastButton);
        EditText cityText = findViewById(R.id.cityTextField);

        forecastBtn.setOnClickListener((click)->{
            String cityName  = cityText.getText().toString();

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Getting Forecast")
                    .setMessage("we are calling people in "+ cityName+ " to look outside their windows and tell us whats the weather like over there.")
                    .setView(new ProgressBar(MainActivity.this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(()->{
                //this runs on another thread
                try{


                    stringURL = "https://api.openweathermap.org/data/2.5/weather?q="+ URLEncoder.encode(cityName,"UTF-8")+"&appid=ed9f3df7fe11d51c89d844765d66bce0&units=Metric&mode=xml";
                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( in  , "UTF-8");

                    String description = null;
                    String iconName = null;
                    String current = null;
                    String min = null;
                    String max = null;
                    String humidity = null;

                    //iterate over whole document
                    while(xpp.next() != XmlPullParser.END_DOCUMENT){
                        switch(xpp.getEventType()){
                            case XmlPullParser.START_TAG:
                                if(xpp.getName().equals("temperature")){
                                    current = xpp.getAttributeValue(null, "value");  //this gets the current temperature
                                    min = xpp.getAttributeValue(null, "min"); //this gets the min temperature
                                    max = xpp.getAttributeValue(null, "max"); //this gets the max temperature

                                }else if(xpp.getName().equals("weather")){
                                    description = xpp.getAttributeValue(null, "value");//this gets the weather description
                                    iconName = xpp.getAttributeValue(null, "icon"); //this gets the icon
                                }else if(xpp.getName().equals("humidity")){
                                    humidity = xpp.getAttributeValue(null, "value");
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                break;
                        }

                        String finalCurrent = current;
                        String finalMin = min;
                        String finalMax = max;
                        String finalHumidity = humidity;
                        String finalDescription = description;


                        runOnUiThread(() -> {
                            TextView tv = findViewById(R.id.temp);
                            tv.setText("The current temperature is " + finalCurrent);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.minTemp);
                            tv.setText("The min temperature is " + finalMin);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.maxTemp);
                            tv.setText("The max temperature is " + finalMax);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.humidity);
                            tv.setText("The humidityis " + finalHumidity);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.description);
                            tv.setText(finalDescription);
                            tv.setVisibility(View.VISIBLE);

                            dialog.hide();
//                            ImageView iv = findViewById(R.id.icon);
//                            iv.setImageBitmap(finalImage);
//                            iv.setVisibility(View.VISIBLE);
                        });
                    }



                }catch (IOException | XmlPullParserException ioe){
                    Log.e("Connection error: ",ioe.getMessage());
                }

            });
        });

    }
}