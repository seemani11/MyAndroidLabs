package algonquin.cst2335.thap0011;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Seema Thapa Gurung
 * @student#: 040942109
 * @version 1.0
 *
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the center of the screen*/
    private TextView tv = null;
    /** This holds input box at the center of the screen*/
    private EditText et = null;
    /** This holds the button at the center of the screen*/
    private Button bt = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        bt = findViewById(R.id.button);

        bt.setOnClickListener( clk -> {
            String password = et.getText().toString();
            if(checkPasswordComplexity(password))
                tv.setText("Your password meets the requirements");
            else
                tv.setText("You shall not pass!");
        });
    }

    /**
     * This function checks if string has an upper case letter,
     * a lower case letter, a number, and a special symbol (#$%^&*!@?)
     *
     * @param pw The String object that we are checking
     * @return Returns true if string matches criteria
     */
    private boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {

            if(Character.isUpperCase(pw.charAt(i)))
                foundUpperCase = true;

            if(Character.isLowerCase(pw.charAt(i)))
                foundLowerCase = true;

            if (Character.isDigit(pw.charAt(i)))
                foundNumber = true;

            if (isSpecialCharacter(pw.charAt(i)))
                foundSpecial = true;

        }

        if(!foundUpperCase)
            Toast.makeText(this, "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();

        if(!foundLowerCase)
            Toast.makeText(this, "Your password does not have a lower case letter", Toast.LENGTH_SHORT).show();

        if(!foundNumber)
            Toast.makeText(this, "Your password does not have a number", Toast.LENGTH_SHORT).show();

        if(!foundSpecial)
            Toast.makeText(this, "Your password does not have a special character", Toast.LENGTH_SHORT).show();

        if(foundUpperCase == true & foundLowerCase == true & foundNumber == true & foundSpecial == true) {
            return true;
        }
        return false;
    }

    /**
     * This function checks if character
     * matches the specified symbols
     *
     * @param c The character object that we are checking
     * @return Returns true if character matches symbol
     */
    boolean isSpecialCharacter(char c)
    {
        switch (c){
            case '#':
                return true;
            case '$':
                return true;
            case '%':
                return true;
            case '^':
                return true;
            case '&':
                return true;
            case '*':
                return true;
            case '!':
                return true;
            case '@':
                return true;
            case '?':
                return true;
            default:
                return false;
        }
    }
}