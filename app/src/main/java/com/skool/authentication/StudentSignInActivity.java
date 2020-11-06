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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.skool.HomeActivity;
import com.skool.R;

import java.util.Objects;

public class StudentSignInActivity  extends AppCompatActivity {


    private static final String TAG = "StudentSignInActivity";


    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText studentEmailEt;
    EditText studentPasswordEt;
    Button studentSignInBtn;
    TextView createStudentAccountTv;
    TextView signInAsLectureTv;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signin);



         initViews();
         setOnClickListeners();
        setupFirebaseAuth();

        Button studentSignInBtn = findViewById(R.id.student_sign_in_button);
        studentSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the fields are filled out
                if (!isEmpty(studentEmailEt.getText().toString())
                        && !isEmpty(studentPasswordEt.getText().toString())) {
                    Log.d(TAG, "onClick: attempting to authenticate.");

                    showDialog();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(studentEmailEt.getText().toString(),
                            studentPasswordEt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideDialog();

                                    if(task.isSuccessful()) {
                                        Toast.makeText(StudentSignInActivity.this, "Student SignIn successful", Toast.LENGTH_SHORT).show();
                                        launchActivity(HomeActivity.class);
                                    }

                                    else {
                                        Log.v("sign in failed", Objects.requireNonNull(task.getException()).getLocalizedMessage());
                                        Toast.makeText(StudentSignInActivity.this, "Student sign in failed: " + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                } else {
                    Toast.makeText(StudentSignInActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Return true if the @param is null
     *
     * @param string //@return
     */
    private boolean isEmpty(String string) {
        return string.equals("");
    }


    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    public void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    //Firebase setup
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (user.isEmailVerified()) {
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Toast.makeText(StudentSignInActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();


                        launchActivity(HomeActivity.class);

                    } else {
                        // User is signed out
                        Toast.makeText(StudentSignInActivity.this, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    // ...
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
                createStudentAccountTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchActivity(StudentSignUpActivity.class);
                    }
                });
                signInAsLectureTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchActivity(LecturerSignInActivity.class);
                    }
                });
            }

            private void initViews() {
                studentEmailEt = findViewById(R.id.student_sign_in_email);
                studentPasswordEt = findViewById(R.id.student_sign_in_password);
                studentSignInBtn = findViewById(R.id.student_sign_in_button);
                createStudentAccountTv = findViewById(R.id.create_student_account_tv);
                signInAsLectureTv = findViewById(R.id.sign_in_as_lecturer_tv);
                mProgressBar = findViewById(R.id.progressBar);
            }

            private void launchActivity(Class activityClass) {
                Intent intent = new Intent(StudentSignInActivity.this, activityClass);
                startActivity(intent);
                finish();
            }
        }


