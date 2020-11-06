package com.skool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skool.custom.LectureAdapter;
import com.skool.model.Lecture;

import java.util.ArrayList;
import java.util.List;

import static com.skool.model.constants.ID_FIELD;
import static com.skool.model.constants.LECTURE;

public class MyLectureActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView;
    ProgressBar progressBar;


    RecyclerView.LayoutManager layoutManager;
    LectureAdapter lectureAdapter;
    List<Lecture> myLectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lecture);
        recyclerView = findViewById(R.id.my_lectures_rv);
        textView = findViewById(R.id.no_lecture_tv);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        myLectures = new ArrayList<>();
        getMyLectures();
    }

    private void getMyLectures() {
        String authorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.v("fetching my lectures", "getting lectures");
        progressBar.setVisibility(View.VISIBLE);
        db.collection(LECTURE).whereEqualTo(ID_FIELD,authorId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.v("fetching my lectures", "getting lectures completed");
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Log.v("fetching my lectures", "successful");
                            for (QueryDocumentSnapshot lectureSnapShot : task.getResult()){
                                Lecture lecture = lectureSnapShot.toObject(Lecture.class);
                                myLectures.add(lecture);
                            }

                            if (myLectures.size()>0){
                                lectureAdapter = new LectureAdapter(myLectures);
                                recyclerView.setAdapter(lectureAdapter);
                            }
                            else {
                                recyclerView.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            recyclerView.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            Log.v("fetching my lectures", "failed"+task.getException().getLocalizedMessage());
                        }
                    }
                });



    }
}