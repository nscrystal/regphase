package com.app.regform;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.GridView;
import android.widget.ImageView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    //var
    private int chunkNumbers, rows, cols, chunkHeight, chunkWidth;
    private ArrayList<Bitmap> chunkedImages;
    private String nameHolder;
    private Integer passwordHolder = null;
    private SQLiteHelper userDB;

    //widgets
    private ImageView imageView;
    private GridView gridView;
    private MaterialButton register;
    private TextInputEditText username_ET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDB = new SQLiteHelper(this);

        register = findViewById(R.id.buttonRegister);
        username_ET = findViewById(R.id.username_ET);
        imageView = findViewById(R.id.imageView);
        gridView = findViewById(R.id.gridView_GV);

        chunkedImages = new ArrayList<>();

        splitImage();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameHolder = String.valueOf(username_ET.getText());

                if (passwordHolder == null)
                    Toast.makeText(getApplicationContext(),"Select a pass image",Toast.LENGTH_LONG).show();
                else {
                    if (!nameHolder.equals("")){
                        Boolean exists = userDB.checkUser(nameHolder);
                        if (exists)
                            Toast.makeText(getApplicationContext(),"Username already exist. Try another one",Toast.LENGTH_LONG).show();
                        else{
                            Boolean added = userDB.addUser(nameHolder,3);
                            if (added){
                                Toast.makeText(getApplicationContext(),"User registration successful",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                finish();
                            }else
                                Toast.makeText(getApplicationContext(),"User registration failed",Toast.LENGTH_LONG).show();
                        }
                    }else
                        Toast.makeText(getApplicationContext(),"Enter a valid username",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void splitImage() {

        chunkedImages.clear();
        chunkNumbers = 16;
        rows = cols = (int) Math.sqrt(chunkNumbers);

        //Getting the scaled bitmap of the source image
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        chunkHeight = bitmap.getHeight()/rows;
        chunkWidth = bitmap.getWidth()/cols;

        //xCoOrd and yCoOrd are the pixel positions of the image chunks
        int yCoord = 0;
        for(int x=0; x<rows; x++){
            int xCoord = 0;
            for(int y=0; y<cols; y++){
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        initGridView(chunkedImages);
    }

    private void initGridView(ArrayList<Bitmap> chunkedImages) {
        gridView.setAdapter(new ImageAdapter(this, chunkedImages));
        gridView.setNumColumns(cols);
        imageView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
    }

    public void selectedPassword(Integer index){
        passwordHolder = index;
//        Toast.makeText(getApplicationContext(),String.valueOf(passwordHolder),Toast.LENGTH_SHORT).show();
        Log.d("PASS IMAGE", String.valueOf(passwordHolder));
    }
}
