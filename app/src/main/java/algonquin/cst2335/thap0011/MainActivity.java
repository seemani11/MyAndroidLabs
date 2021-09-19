package algonquin.cst2335.thap0011;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
Button mybtn;
TextView mytv;
EditText myet;
CheckBox mycb;
Switch mysw;
ImageButton myib;
RadioButton myrb;
StringBuilder str = new StringBuilder();
int width;
int height;
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        str= new StringBuilder();
        width = myib.getWidth();
        height = myib.getHeight();
        // sb.append("the width is:"+width+" the height is :"+height);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mybtn = (Button) findViewById(R.id.btn);
        mytv = (TextView) findViewById(R.id.tv);
        myet = (EditText) findViewById(R.id.et);
        myib = (ImageButton) findViewById(R.id.ib);

        mycb = (CheckBox) findViewById(R.id.cb);
        mysw = (Switch) findViewById(R.id.sw);
        myrb = (RadioButton) findViewById(R.id.rb);

        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textobtained = myet.getText().toString();
                mytv.setText("the text you entered is  " + textobtained);
            }
        });
        mycb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "checkbox clicked", Toast.LENGTH_SHORT).show();
            }


        });
        mysw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "switch clicked", Toast.LENGTH_SHORT).show();
            }
        });
        myrb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Radiobutton clicked", Toast.LENGTH_SHORT).show();
            }
        });
        myib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mycb.isChecked())
                    str.append("checkbox on \n");
                else
                    str.append("checkbox off \n");
                if(myrb.isChecked())
                    str.append("radiobutton on \n");
                else
                    str.append("radiobutton off \n");
                if(mysw.isChecked())
                    str.append("switch on \n");
                else
                    str.append("switch off \n");



                str.append("height is" + height);
                str.append("width is" + width);
                Toast.makeText(getApplicationContext(), str.toString(), Toast.LENGTH_SHORT).show();
                str.delete(0, str.length());
            }
        });
    }
}