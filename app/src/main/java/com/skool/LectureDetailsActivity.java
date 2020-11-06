package com.skool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.skool.custom.CommentAdapter;
import com.skool.model.Comment;
import com.skool.model.Lecture;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.skool.model.constants.LECTURE;

public class LectureDetailsActivity extends AppCompatActivity {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView title;
    private TextView author;
    private TextView date;
    private TextView description;
    private Button downloadButton;

    private Lecture lecture;
    private List<Comment> commentList;
    private RecyclerView.LayoutManager layoutManager;
    private CommentAdapter commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        initViews();


        Intent intent = getIntent();
        if(intent.hasExtra(LECTURE)){
            lecture = intent.getParcelableExtra(LECTURE);
           title.setText(lecture.getTitle());
           author.setText(lecture.getAuthor());
           date.setText(null);
           description.setText(lecture.getDescription());
           showImage(lecture.getImageUrl());
           commentList = lecture.getComments();
           commentAdapter = new CommentAdapter(commentList);
           recyclerView.setAdapter(commentAdapter);
        }

    }

    private void initViews() {
        recyclerView = findViewById(R.id.comments_rv);
        imageView = findViewById(R.id.lecture_image);
        title = findViewById(R.id.my_lecture_title);
        author = findViewById(R.id.lecturer_name);
        date = findViewById(R.id.lecture_creation_date);
        description = findViewById(R.id.lecture_description);
        downloadButton = findViewById(R.id.download_lecture_button);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void showImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl)
                    .into(imageView);
        }
    }
}