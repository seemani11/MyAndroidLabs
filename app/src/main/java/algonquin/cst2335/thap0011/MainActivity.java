package algonquin.cst2335.thap0011;

import androidx.annotation.RequiresApi;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(()->{
                //this runs on another thread
                try{

                    String cityName  = cityText.getText().toString();
                    stringURL = "https://api.openweathermap.org/data/2.5/weather?q="+ URLEncoder.encode(cityName,"UTF-8")+"&appid=ed9f3df7fe11d51c89d844765d66bce0&units=Metric";
                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    String text = (new BufferedReader(
                                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                                        .lines()
                                        .collect(Collectors.joining("\n"));

                    JSONObject theDocument = new JSONObject(text);

                    JSONObject coord = theDocument.getJSONObject("coord");
                    JSONArray weatherArray = theDocument.getJSONArray("weather");
                    JSONObject position0 = weatherArray.getJSONObject(0);
                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");

                    int vis = theDocument.getInt("visibility");
                    String name =  theDocument.getString("name");

                    JSONObject mainObject = theDocument.getJSONObject("main");
                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble("temp_max");
                    int humidity = mainObject.getInt("humidity");

                    Bitmap image = null;
                    //save the icon

                    File file = new File(getFilesDir(), iconName + ".png");
                    if(file.exists()){
                        image = BitmapFactory.decodeFile(getFilesDir()+ "/"+iconName +".png");
                    }else{
                        URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                        HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                        connection.connect();
                        int responseCode = connection.getResponseCode();

                        if(responseCode == 200){
                            image = BitmapFactory.decodeStream(connection.getInputStream());
                            image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName+ ".png", Activity.MODE_PRIVATE));
                        }
                    }

                            Bitmap finalImage = image;
                            runOnUiThread(() -> {
                                TextView tv = findViewById(R.id.temp);
                                tv.setText("The current temperature is " + current);
                                tv.setVisibility(View.VISIBLE);

                                tv = findViewById(R.id.minTemp);
                                tv.setText("The min temperature is " + min);
                                tv.setVisibility(View.VISIBLE);

                                tv = findViewById(R.id.maxTemp);
                                tv.setText("The max temperature is " + max);
                                tv.setVisibility(View.VISIBLE);

                                tv = findViewById(R.id.humidity);
                                tv.setText("The humidityis " + humidity);
                                tv.setVisibility(View.VISIBLE);

                                tv = findViewById(R.id.description);
                                tv.setText(description);
                                tv.setVisibility(View.VISIBLE);


                                ImageView iv = findViewById(R.id.icon);
                                iv.setImageBitmap(finalImage);
                                iv.setVisibility(View.VISIBLE);
                            });

                    //save icon to the device
                    FileOutputStream fout = null;
                    try{
                        fout = openFileOutput(iconName+ ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fout);
                        fout.flush();
                        fout.close();
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }


                }catch (IOException | JSONException ioe){
                    Log.e("Connection error: ",ioe.getMessage());
                }

            });
        });

    }
}