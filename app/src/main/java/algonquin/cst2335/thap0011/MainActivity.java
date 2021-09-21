package algonquin.cst2335.thap0011;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button mybutton = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);

        CheckBox mycb = findViewById(R.id.cb);
        Switch mysw = findViewById(R.id.sw);
        RadioButton myrb =  findViewById(R.id.rb);

        mybutton.setOnClickListener(vw ->{

            String textobtained = myedit.getText().toString();
            mytext.setText("the text you entered is  " + textobtained);
        });

        mycb.setOnCheckedChangeListener((btn,isChecked)->{
            Toast.makeText(getApplicationContext(), "checkbox clicked", Toast.LENGTH_SHORT).show();
        });

        mysw.setOnCheckedChangeListener((btn,isChecked)->{
            Toast.makeText(getApplicationContext(), "switch clicked", Toast.LENGTH_SHORT).show();
        });

        myrb.setOnCheckedChangeListener((btn,isChecked)->{
            Toast.makeText(getApplicationContext(), "Radiobutton clicked", Toast.LENGTH_SHORT).show();
        });
    }

}