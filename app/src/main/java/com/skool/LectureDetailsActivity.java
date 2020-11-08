package com.skool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skool.custom.CommentAdapter;
import com.skool.model.Comment;
import com.skool.model.Lecture;
import com.skool.model.User;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.skool.model.constants.LECTURE;
import static com.skool.model.constants.USER_PATH;

public class LectureDetailsActivity extends AppCompatActivity {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView title;
    private TextView author;
    private TextView date;
    private TextView description;
    private Button downloadButton;
    EditText addCommentEt;
    Button addCommentButton;

    private Lecture lecture;
    private List<Comment> commentList;
    private RecyclerView.LayoutManager layoutManager;
    private CommentAdapter commentAdapter;
    private User user;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        initViews();
        getLectureFromIntent();

        addCommentOnClick();


    }

    private void getLectureFromIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra(LECTURE)){
            lecture = intent.getParcelableExtra(LECTURE);
           title.setText(lecture.getTitle());
           author.setText(lecture.getAuthor());
           date.setText(lecture.getDate().toString());
           description.setText(lecture.getDescription());
           showImage(lecture.getImageUrl());
           commentList = lecture.getComments();
           commentAdapter = new CommentAdapter(commentList);
           recyclerView.setAdapter(commentAdapter);
        }
    }

    private void addCommentOnClick() {
                addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = FirebaseFirestore.getInstance();
                final DocumentReference firestoreDocumentReference = db.collection(LECTURE).document(lecture.getId());
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child(USER_PATH).child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                Log.v("check","fire base reference gotten ");
                valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        Log.v("user is: ", Objects.requireNonNull(user).getFirstName());

                        String comment =addCommentEt.getText().toString();
                        if(comment.isEmpty()){
                            Toast.makeText(LectureDetailsActivity.this, "empty comment", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Comment newComment = new Comment(user.getFirstName()+" "+user.getLastName(),comment);
                            lecture.addComment(newComment);
                            firestoreDocumentReference.set(lecture).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    commentAdapter.setComments(lecture.getComments());
                                    addCommentEt.setText("");
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.v("check","onCancelled "+ "Called");
                        Toast.makeText(LectureDetailsActivity.this, "Error fetching User: "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                };

                databaseReference.addValueEventListener(valueEventListener);
                Log.v("check", "eventListener added");

            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.comments_rv);
        imageView = findViewById(R.id.lecture_image);
        title = findViewById(R.id.my_lecture_title);
        author = findViewById(R.id.lecturer_name);
        date = findViewById(R.id.lecture_creation_date);
        description = findViewById(R.id.lecture_description);
        addCommentButton = findViewById(R.id.add_comment_button);
        addCommentEt = findViewById(R.id.add_comment_et);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}