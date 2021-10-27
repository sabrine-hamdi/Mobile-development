package com.example.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {
    private Button LgoBtn;
    private Button AllBtn;
    private Button FoodBtn;
    private Button SuppliesBtn;
    private Button ClothesBtn;
    private ImageView ListBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LgoBtn = (Button) findViewById(R.id.LgoBtn);
        AllBtn = (Button) findViewById(R.id.AllBtn);
        FoodBtn = (Button) findViewById(R.id.FoodBtn);
        SuppliesBtn = (Button) findViewById(R.id.SuppliesBtn);
        ClothesBtn = (Button) findViewById(R.id.ClothesBtn);
        ListBtn = (ImageView) findViewById(R.id.ListBtn);
        ListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToList();
            }
        });
        LgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement sessionManagement = new SessionManagement(HomeActivity.this);
                sessionManagement.removeSession();

                moveToLogin();
            }
        });
        AllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                moveToMaps("All");
            }
        });
        FoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                moveToMaps("Food");
            }
        });
        SuppliesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                moveToMaps("Supplies");
            }
        });
        ClothesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                moveToMaps("Clothes");
            }
        });

    }
    private void moveToLogin() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void moveToList() {
        Intent intent = new Intent(HomeActivity.this, ListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void moveToMaps(String markertype) {
        Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type",markertype);
        startActivity(intent);
    }
}