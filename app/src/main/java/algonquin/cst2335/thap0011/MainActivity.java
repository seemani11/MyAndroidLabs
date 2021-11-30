package algonquin.cst2335.thap0011;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

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
    private TextView currentTemp;
    private TextView maxTemp;
    private TextView minTemp;
    private TextView humidity;
    private TextView description;
    private ImageView icon;
    private TextView cityField;

    private float oldSize = 14;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the toolbar view
        Toolbar myToolbar = findViewById(R.id.toolbar);
        //load the toolbar
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);//create the hamburger button
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);//find popout navigation view from the drawer layout
        navigationView.setNavigationItemSelectedListener((item)->{ //add a selection listener to the menu

            onOptionsItemSelected(item); //call the function for the other toolbar
            drawer.closeDrawer(GravityCompat.START); // close the navigation drawer
            return false;
        });

        Button forecastBtn = findViewById(R.id.foreCastButton);
        EditText cityText = findViewById(R.id.cityTextField);

        currentTemp = findViewById(R.id.temp);
        maxTemp = findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);
        humidity = findViewById(R.id.humidity);
        description = findViewById(R.id.description);
        icon = findViewById(R.id.icon);
        cityField = findViewById(R.id.cityTextField);


        forecastBtn.setOnClickListener((click)->{
            String cityName  = cityText.getText().toString();
            myToolbar.getMenu().add(0,5,0,cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);
        });

    }

    /**
     * initialize the toolbar , by loading layout main_menu
     * @param menu Menu object
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions,menu);//load layout file to menu object passed
        return true;
    }

    /**
     * this function responds when user clicks on the menu item
     * @param item , item that was selected by the user
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //get the id of the item selected and check if its one of the ids from the menu
        switch(item.getItemId()){

            case 5:
                String cityName = item.getTitle().toString();
                runForecast(cityName);
                break;
            case R.id.hide_views:
                currentTemp.setVisibility(View.INVISIBLE);
                maxTemp.setVisibility(View.INVISIBLE);
                minTemp.setVisibility(View.INVISIBLE);
                humidity.setVisibility(View.INVISIBLE);
                icon.setVisibility(View.INVISIBLE);
                cityField.setVisibility(View.INVISIBLE);
                break;
            case R.id.id_increase:
                oldSize++;
                currentTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                humidity.setTextSize(oldSize);
                description.setTextSize(oldSize);
                cityField.setTextSize(oldSize);
                break;
            case R.id.id_decrease:
                oldSize = Float.max(oldSize-1, 5);
                currentTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                humidity.setTextSize(oldSize);
                description.setTextSize(oldSize);
                cityField.setTextSize(oldSize);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void runForecast(String cityName){


            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(()->{
                //this runs on another thread
                try{


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

    }
}