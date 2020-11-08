package com.skool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skool.custom.LectureCategoryAdapter;
import com.skool.model.LectureCategory;
import com.skool.model.User;
import com.skool.splash.AuthActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import static com.skool.model.constants.STUDENT_USER_TYPE;
import static com.skool.model.constants.USER_PATH;

public class HomeActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private User user;

    ArrayList<LectureCategory> lectureCategories;
    ImageView profilePhoto;
    TextView profileName;
    RecyclerView lectureCategoriesRv;
    ProgressBar progressBar;
    TextView findLecByCatTv;
    CardView cardView;
    TextView addLectureTv;
    Button myLecturesBtn;
    RecyclerView.LayoutManager layoutManager;
    LectureCategoryAdapter lectureCategoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        progressBar.setVisibility(View.VISIBLE);
        Log.v("check","views inititalized");

        getUserFromDb();
        Log.v("check","getUser from db ran");

    }

    private void getUserFromDb() {
        hideViews();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child(USER_PATH).child(firebaseUser.getUid());
            Log.v("check","fire base reference gotten "+ firebaseUser.getUid());
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    Log.v("user is: ", Objects.requireNonNull(user).getFirstName());
                    profileName.setText(String.format("Hello %s", user.getFirstName()));
                    showImage(user.getImageUrl());
                    if (user.getImageUrl()!=null) Log.v("image url is ", user.getImageUrl());
                    showViews();
                    if(user.getUserType()==STUDENT_USER_TYPE){
                        addLectureTv.setVisibility(View.GONE);
                        myLecturesBtn.setVisibility(View.GONE);
                        Log.v("image url is ", "is hidden user type is " + user.getUserType());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v("check","onCancelled "+ "Called");
                    Toast.makeText(HomeActivity.this, "Error fetching User: "+error.getMessage(), Toast.LENGTH_LONG).show();
                    showViews();
                }
            };

            databaseReference.addValueEventListener(valueEventListener);
            Log.v("check", "eventListener added");
        }
        else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(HomeActivity.this, "Email is not Verified! Check your Inbox", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener!=null) databaseReference.removeEventListener(valueEventListener);
    }

    private void initViews() {
        profilePhoto = findViewById(R.id.profile_photo);
        progressBar = findViewById(R.id.progressBar);
        profileName = findViewById(R.id.profile_name);
        lectureCategoriesRv = findViewById(R.id.category_rv);
        findLecByCatTv = findViewById(R.id.findLectureByCat);
        cardView = findViewById(R.id.materialCardView);
        addLectureTv = findViewById(R.id.add_lecture_tv);
        myLecturesBtn = findViewById(R.id.my_lectures_btn);
        layoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL,false);
        lectureCategoriesRv.setLayoutManager(layoutManager);
        loadLectureCategories();
        lectureCategoryAdapter = new LectureCategoryAdapter(lectureCategories);
        lectureCategoriesRv.setAdapter(lectureCategoryAdapter);

        addLectureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(AddLectureActivity.class);
            }
        });

        myLecturesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(MyLectureActivity.class);
            }
        });
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(ProfileActivity.class);
            }
        });
    }

    private void hideViews(){
        profileName.setVisibility(View.GONE);
        profilePhoto.setVisibility(View.GONE);
        findLecByCatTv.setVisibility(View.GONE);
        lectureCategoriesRv.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        addLectureTv.setVisibility(View.GONE);
        myLecturesBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void showViews(){
        profileName.setVisibility(View.VISIBLE);
        profilePhoto.setVisibility(View.VISIBLE);
        findLecByCatTv.setVisibility(View.VISIBLE);
        lectureCategoriesRv.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        addLectureTv.setVisibility(View.VISIBLE);
        myLecturesBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void loadLectureCategories() {
        lectureCategories = new ArrayList<>();
        LectureCategory science = new LectureCategory("Science", R.drawable.ic_baseline_science_24);
        LectureCategory engineering = new LectureCategory("Engineering", R.drawable.ic_baseline_engineering_24);
        LectureCategory medicine = new LectureCategory("Medicine", R.drawable.ic_baseline_medical_services_24);
        LectureCategory ict = new LectureCategory("ICT", R.drawable.ic_baseline_laptop_mac_24);
        LectureCategory arts = new LectureCategory("Arts", R.drawable.ic_baseline_sports_kabaddi_24);
        LectureCategory socialSciences = new LectureCategory("Social Sciences", R.drawable.ic_baseline_group_24);
        LectureCategory business = new LectureCategory("Business", R.drawable.ic_baseline_attach_money_24);





        lectureCategories.add(science);
        lectureCategories.add(engineering);
        lectureCategories.add(medicine);
        lectureCategories.add(ict);
        lectureCategories.add(socialSciences);
        lectureCategories.add(arts);
        lectureCategories.add(business);

    }

    private void showImage(String imageUrl) {
        if (imageUrl != null && imageUrl.trim().length()!=0) {
            Picasso.get().load(imageUrl)
                    .into(profilePhoto);
        }
    }

    private void launchActivity(Class activityClass) {
        Intent intent = new Intent(HomeActivity.this, activityClass);
        intent.putExtra(USER_PATH, user);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_xml, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.log_out){
            logUserOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logUserOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}