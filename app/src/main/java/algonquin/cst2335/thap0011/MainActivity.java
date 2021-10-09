package algonquin.cst2335.thap0011;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener( clk-> {
            Intent nextPage = new Intent( this, SecondActivity.class);
            startActivity(nextPage);
        } );
        Log.w( TAG, "In onCreate() - Loading Widgets" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( TAG, "In onResume() - The application is now responding to user input" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( TAG, "In onDestroy() - Any memory used by the application is freed." );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( TAG, "In onPause() -  The application no longer responds to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( TAG, "In onStop() - The application is no longer visible" );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onStart() - The application is now visible on screen" );
    }
}