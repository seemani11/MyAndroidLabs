package algonquin.cst2335.thap0011;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {
    private ImageView profileImage;
    private String filename = "Picture.png";
    Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();

        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView headerText =  findViewById(R.id.header_text);
        headerText.setText("Welcome back " + emailAddress);

        EditText phoneNumber =  findViewById(R.id.phone_number);
        Button callNumber =  findViewById(R.id.call_button);
        callNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
                startActivity(call);

            }
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();
                            thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);
                            profileImage.invalidate();

                        }

                        FileOutputStream fOut = null;
                        try { fOut = openFileOutput(filename, Context.MODE_PRIVATE);
                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                        }
                        catch (FileNotFoundException e)
                        { e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


        Button changePicture =  findViewById(R.id.change_picture_button);
        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraResult.launch(cameraIntent);
            }
        });



        File file = new File(getFilesDir(), filename);
        if(file.exists()){
            Bitmap theImage = BitmapFactory.decodeFile(filename);
            profileImage.setImageBitmap(theImage);
            Toast.makeText(this, "This file already exist :" + filename, Toast.LENGTH_LONG).show();
        }
    }


}