package com.app.regform;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;




public class RegisterActivity extends AppCompatActivity {

    EditText Email, Password, Name ;
    Button Register;
    //Button button;
    String NameHolder, EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";



    //var
    private int chunkNumbers, rows, cols, chunkHeight, chunkWidth;
    private ArrayList<Bitmap> chunkedImages;
    private Boolean split = true;

    //widgets
    private ImageView imageView;
    private GridView gridView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // button = (Button)findViewById(R.id.buttonBT);

        Register = (Button)findViewById(R.id.buttonRegister);

        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        Name = (EditText)findViewById(R.id.editName);

        sqLiteHelper = new SQLiteHelper(this);




        imageView = findViewById(R.id.imageView);
        gridView = findViewById(R.id.gridView_GV);
        button = findViewById(R.id.buttonBT);

        chunkedImages = new ArrayList<>();

        /*// Adding click listener to split button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(getApplicationContext(), SplitActivity.class);
                startActivity(intent);

            }
        });*/
        // Adding click listener to register button.
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating SQLite database if dose n't exists
                SQLiteDataBaseBuild();

                // Creating SQLite table if dose n't exists.
                SQLiteTableBuild();

                // Checking EditText is empty or Not.
                CheckEditTextStatus();

                // Method to check Email is already exists or not.
                CheckingEmailAlreadyExistsOrNot();

                // Empty EditText After done inserting process.
                EmptyEditTextAfterDataInsert();


            }
        });

    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Email + " VARCHAR, " + SQLiteHelper.Table_Column_3_Password + " VARCHAR);");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase(){

        // If editText is not empty then this block will executed.
        if(EditTextEmptyHolder == true)
        {

            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,email,password) VALUES('"+NameHolder+"', '"+EmailHolder+"', '"+PasswordHolder+"');";

            // Executing query.
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            // Closing SQLite database object.
            sqLiteDatabaseObj.close();

            // Printing toast message after done inserting.
            Toast.makeText(RegisterActivity.this,"User Registered Successfully", Toast.LENGTH_LONG).show();

        }
        // This block will execute if any of the registration EditText is empty.
        else {

            // Printing toast message if any of EditText is empty.
            Toast.makeText(RegisterActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }

    // Empty edittext after done inserting process method.
    public void EmptyEditTextAfterDataInsert(){

        Name.getText().clear();

        Email.getText().clear();

        Password.getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        NameHolder = Name.getText().toString() ;
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    // Checking Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot(){

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Email is already exists then Result variable value set as Email Found.
                F_Result = "Email Found";

                // Closing cursor.
                cursor.close();
            }
        }

        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();

    }


    // Checking result
    public void CheckFinalResult(){

        // Checking whether email is already exists or not.
        if(F_Result.equalsIgnoreCase("Email Found"))
        {

            // If email is exists then toast msg will display.
            Toast.makeText(RegisterActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();

        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();

        }

        F_Result = "Not_Found" ;

    }


    public void splitImage(View view) {

        if (split){
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

            split = false;
            button.setText("Combine");
        }else {

            imageView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            button.setText("Split");
            split = true;

        }

    }


    private void initGridView(ArrayList<Bitmap> chunkedImages) {

        gridView.setAdapter(new ImageAdapter(this, chunkedImages));
        gridView.setNumColumns(cols);
        imageView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);

    }

}
