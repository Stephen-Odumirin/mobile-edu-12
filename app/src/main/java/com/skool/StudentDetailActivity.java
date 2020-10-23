package com.skool;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class StudentDetailActivity extends AppCompatActivity {

     RecyclerView recyclerView;
      //TextView texttv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

       recyclerView = findViewById(R.id.recycler);
       // texttv = findViewById(R.id.texttv);
    }
}