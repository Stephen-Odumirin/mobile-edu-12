package com.skool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.skool.model.constants.LECTURE_CATEGORY;

public class LectureListActivity extends AppCompatActivity {

     RecyclerView recyclerView;
      TextView categoryNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

       recyclerView = findViewById(R.id.lecture_list_rv);
       categoryNameTv = findViewById(R.id.category_name);

        Intent intent = getIntent();
        if(intent.hasExtra(LECTURE_CATEGORY)){
            categoryNameTv.setText(intent.getStringExtra(LECTURE_CATEGORY));
        }

    }
}