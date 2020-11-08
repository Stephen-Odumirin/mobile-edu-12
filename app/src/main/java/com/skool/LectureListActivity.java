package com.skool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skool.custom.CommentAdapter;
import com.skool.custom.LectureAdapter;
import com.skool.model.Comment;
import com.skool.model.Lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.skool.model.constants.LECTURE;
import static com.skool.model.constants.LECTURE_CATEGORY;

public class LectureListActivity extends AppCompatActivity {

     RecyclerView recyclerView;
     RecyclerView.LayoutManager layoutManager;
      TextView categoryNameTv;
      LectureAdapter lectureAdapter;
      String category;
      List<Lecture> lectures;
      ProgressBar progressBar;
      TextView noLecturesTv;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);


        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        lectures = new ArrayList<>();
        getCategory();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.lecture_list_rv);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(layoutManager);
        categoryNameTv = findViewById(R.id.category_name);
        noLecturesTv = findViewById(R.id.no_lectures_tv);
    }


    private void getCategory() {
        progressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        collectionReference= db.collection(LECTURE);
        Intent intent = getIntent();
        if(intent.hasExtra(LECTURE_CATEGORY)){
            category = intent.getStringExtra(LECTURE_CATEGORY);
            categoryNameTv.setText(category);

            collectionReference.whereEqualTo("category", category).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    progressBar.setVisibility(View.GONE);
                    if(error != null){
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(LectureListActivity.this,"error fetching lectures "+error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        for (DocumentSnapshot lectureSnapShot : value.getDocuments()) {
                            Lecture lecture = lectureSnapShot.toObject(Lecture.class);
                            lectures.add(lecture);
                        }

                            if (lectures.size()>0){
                                lectureAdapter = new LectureAdapter(lectures);
                                recyclerView.setAdapter(lectureAdapter);
                            }
                            else {
                                recyclerView.setVisibility(View.GONE);
                                noLecturesTv.setVisibility(View.VISIBLE);
                            }
                    }
                }
            });
        }
    }
}