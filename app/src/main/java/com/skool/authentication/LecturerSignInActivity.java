package com.skool.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skool.HomeActivity;
import com.skool.LectureDetailsActivity;
import com.skool.R;

import java.util.Objects;

public class LecturerSignInActivity extends AppCompatActivity {

    private static final String TAG = "LecturerSignInActivity";


    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText lecturerEmailEt;
    EditText lecturerPasswordEt;
    Button lecturerSignInBtn;
    TextView createLecturerAccountTv;
    TextView signInAsStudentTv;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_sign_in);

        initViews();
        setOnClickListeners();
        setupFirebaseAuth();


        Button lecturerSignInBtn =  findViewById(R.id.lecturer_sign_in_button);
        lecturerSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the fields are filled out

                if(!isEmpty(lecturerEmailEt.getText().toString())
                        && !isEmpty(lecturerPasswordEt.getText().toString())){
                    Log.d(TAG, "onClick: attempting to authenticate.");

                    showDialog();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(lecturerEmailEt.getText().toString(),
                            lecturerPasswordEt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideDialog();
                                    if(task.isSuccessful()) {
                                        Toast.makeText(LecturerSignInActivity.this, "Lecturer SignIn successful", Toast.LENGTH_SHORT).show();
                                        launchActivity(HomeActivity.class);
                                    }

                                    else {
                                        Log.v("sign in failed", Objects.requireNonNull(task.getException()).getLocalizedMessage());
                                        Toast.makeText(LecturerSignInActivity.this, "Lecturer sign in failed: " + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                }else{
                    Toast.makeText(LecturerSignInActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }


    public void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }



         //Firebase setup
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (user.isEmailVerified()) {
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Toast.makeText(LecturerSignInActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LecturerSignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // User is signed out
                        Toast.makeText(LecturerSignInActivity.this, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                    }else{
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }

                }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }



    private void setOnClickListeners() {
        createLecturerAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(LecturerSignUpActivity.class);
            }
        });
        signInAsStudentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(StudentSignInActivity.class);
            }
        });
    }

    private void initViews() {
        lecturerEmailEt = findViewById(R.id.lecturer_sign_in_email);
        lecturerPasswordEt = findViewById(R.id.lecturer_sign_in_password);
        lecturerSignInBtn = findViewById(R.id.sign_in_as_lecturer);
        createLecturerAccountTv = findViewById(R.id.create_lecturer_account_tv);
        signInAsStudentTv = findViewById(R.id.sign_in_as_student_tv);
        mProgressBar = findViewById(R.id.progressBar);
    }

    private void launchActivity(Class activityClass) {
        Intent intent = new Intent(LecturerSignInActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}