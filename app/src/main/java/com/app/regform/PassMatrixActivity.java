package com.app.regform;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class PassMatrixActivity extends AppCompatActivity {

    //const
    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Name="name";

    public static final String Table_Column_2_Email="email";

    public static final String Table_Column_3_Password="password";

    //var
    private String username, loginIndicator, passIndex;
    private SQLiteHelper dbHelper;
    private int chunkNumbers, rows, cols, chunkHeight, chunkWidth;
    private ArrayList<Bitmap> chunkedImages;
    private String[] verticalArray, horizontalArray;
    private Integer passwordHolder = null;


    //widgets
    private ImageView login_imageView;
    private GridView login_gridView;
    private RecyclerView verticalList_LV, horizontalList_LV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_phrase);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        loginIndicator = intent.getStringExtra("indicator");

        login_imageView = findViewById(R.id.login_imageView);
        login_gridView = findViewById(R.id.login_gridView_GV);
        verticalList_LV = findViewById(R.id.verticalList_LV);
        horizontalList_LV = findViewById(R.id.horizontalList_LV);

        dbHelper = new SQLiteHelper(this);

        chunkedImages = new ArrayList<>();

        splitImage();

        if (!username.equals("")){
            getUserDetails();
        }

    }

    private void getUserDetails() {
        Cursor cursor = dbHelper.getUserDetails(username);
        if (cursor!=null){
            cursor.moveToFirst();
            passIndex = cursor.getString(2);
            Log.d("PassIndex",passIndex);
        }
    }

    public void splitImage() {

        chunkedImages.clear();
        chunkNumbers = 16;
        rows = cols = (int) Math.sqrt(chunkNumbers);

        //Getting the scaled bitmap of the source image
        Bitmap bitmap = ((BitmapDrawable) login_imageView.getDrawable()).getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,1000, 1000, true);

        chunkHeight = scaledBitmap.getHeight()/rows;
        chunkWidth = scaledBitmap.getWidth()/cols;

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

        initVerticalAdapter();
        initHorizontalAdapter();
    }

    private void initVerticalAdapter() {
        String[] array = new String[] {"0","1", "2", "3"};

        VerticalListAdapter verticalAdapter = new VerticalListAdapter(this,array, chunkHeight-30);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        verticalList_LV.setLayoutManager(layoutManager);
        verticalList_LV.setAdapter(verticalAdapter);
    }

    private void initHorizontalAdapter() {
        String[] array = new String[] {"A","B", "C", "D"};

        HorizontalListAdapter horizontalAdapter = new HorizontalListAdapter(this,array, chunkWidth-30);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalList_LV.setLayoutManager(layoutManager);
        horizontalList_LV.setAdapter(horizontalAdapter);
    }

    private void initGridView(ArrayList<Bitmap> chunkedImages) {
        login_gridView.setAdapter(new LoginImageAdapter(this, chunkedImages));
        login_gridView.setNumColumns(cols);
        login_gridView.setVisibility(View.VISIBLE);
    }

    public void saveVerticalArray(String[] array){
        verticalArray = array;
        Log.d("VerticalArray",String.valueOf(Arrays.asList(verticalArray)));

    }

    public void saveHorizontalArray(String[] array){
        horizontalArray = array;
        Log.d("HorizontalArray",String.valueOf(Arrays.asList(horizontalArray)));
    }

    public void selectedPassword(Integer index){
        passwordHolder = index;
        Log.d("PASS IMAGE", String.valueOf(passwordHolder));
    }

    public void checkPassMatrix(View view) {

        if (passwordHolder == null){
            Toast.makeText(this,"Select your pass image",Toast.LENGTH_LONG).show();
        }else {
            String verticalString = getVerticalString();
            String horizontalString = getHorizontalString();

            if (String.valueOf(passwordHolder).equals(passIndex)){
                if (loginIndicator.equals(verticalString+horizontalString)){
                    Log.d("STATUS","Login Success");
                    showSuccessMessage();
                }else
                    Toast.makeText(getApplicationContext(), "Your login indicator does not match",Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(getApplicationContext(), "Your pass image does not match",Toast.LENGTH_LONG).show();

        }
    }

    private String getVerticalString() {
        String string;
        switch (passwordHolder){
            case 0:
            case 1:
            case 2:
            case 3:
                string = verticalArray[0];
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                string = verticalArray[1];
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                string = verticalArray[2];
                break;
            case 12:
            case 13:
            case 14:
            case 15:
                string = verticalArray[3];
                break;
            default:
                string = "";
        }
        return string;
    }

    private String getHorizontalString() {
        String string;
        switch (passwordHolder){
            case 0:
            case 4:
            case 8:
            case 12:
                string = horizontalArray[0];
                break;
            case 1:
            case 5:
            case 9:
            case 13:
                string = horizontalArray[1];
                break;
            case 2:
            case 6:
            case 10:
            case 14:
                string = horizontalArray[2];
                break;
            case 3:
            case 7:
            case 11:
            case 15:
                string = horizontalArray[3];
                break;
            default:
                string = "";
        }
        return string;
    }

    private void showSuccessMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome "+username)
                .setMessage("You have successfully logged in")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PassMatrixActivity.this,MainActivity.class));
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

//    @SuppressLint("ResourceAsColor")
//    public void highlightImage(int position){
//        View viewPrev;
//        for (int i =0; i<chunkedImages.size();i++){
//           viewPrev = login_gridView.getChildAt(i);
//           if (i == position){
//               viewPrev.setBackgroundColor(R.color.colorPrimary);
//           }
//           //           else viewPrev.setBackgroundColor(R.color.white);
//        }
//    }
}
