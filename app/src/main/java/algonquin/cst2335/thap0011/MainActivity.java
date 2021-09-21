package algonquin.cst2335.thap0011;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String rbstatus= "No";
    private String cbstatus="No";
     private String swtstatus= "No";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button mybutton = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);

        ImageView myimage = findViewById(R.id.logo_algonquin);
        ImageButton imgbtn = findViewById( R.id.myimagebutton );

        CheckBox mycb = findViewById(R.id.cb);
        Switch mysw = findViewById(R.id.sw);
        RadioButton myrb =  findViewById(R.id.rb);

        mybutton.setOnClickListener(vw ->{

            String textobtained = myedit.getText().toString();
            mytext.setText("the text you entered is  " + textobtained);
        });

        mycb.setOnCheckedChangeListener((btn,isChecked)->{
            cbstatus = isChecked?"yes":"No";
            Toast.makeText(getApplicationContext(), "Movie tonght? "+cbstatus, Toast.LENGTH_SHORT).show();
        });

        mysw.setOnCheckedChangeListener((btn,isChecked)->{
            swtstatus = isChecked?"Yes":"No";
            Toast.makeText(getApplicationContext(), "Coffee Tonght? "+swtstatus, Toast.LENGTH_SHORT).show();
        });

        myrb.setOnCheckedChangeListener((btn,isChecked)->{
            rbstatus = isChecked ? "Yes":"No!";
            Toast.makeText(getApplicationContext(), "agree on terms? ", Toast.LENGTH_SHORT).show();
        });

          imgbtn.setOnClickListener( (View v)->{
              Toast.makeText(getApplicationContext(), "height: "+ imgbtn.getHeight()+" width: "+ imgbtn.getWidth(),Toast.LENGTH_LONG).show();
          });
    }

}