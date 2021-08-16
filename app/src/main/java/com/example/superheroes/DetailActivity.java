package com.example.superheroes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.superheroes.MainActivity.E_ID;
import static com.example.superheroes.MainActivity.E_Name;
import static com.example.superheroes.MainActivity.E_imageUrl;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(E_imageUrl);
        String creatorName = intent.getStringExtra(E_Name);
        int likeCount = intent.getIntExtra(E_ID, 0);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewName = findViewById(R.id.text_view_name_detail);
        TextView textViewID = findViewById(R.id.text_view_id_detail);
        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textViewName.setText(creatorName);
        textViewID.setText("ID: " + likeCount);
    }
}