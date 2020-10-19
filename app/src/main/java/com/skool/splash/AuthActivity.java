package com.skool.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skool.R;
import com.skool.authentication.LecturerSignInActivity;
import com.skool.authentication.LecturerSignUpActivity;
import com.skool.authentication.StudentSignInActivity;
import com.skool.authentication.StudentSignUpActivity;

public class AuthActivity extends AppCompatActivity {
    Button signInAsLecturer;
    Button signInAsStudent;
    TextView signUpAsLecturer;
    TextView signUpAsStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        signInAsStudent = findViewById(R.id.sign_in_as_student);
        signInAsLecturer = findViewById(R.id.sign_in_as_lecturer);
        signUpAsStudent = findViewById(R.id.sign_up_as_student);
        signUpAsLecturer = findViewById(R.id.sign_up_as_lecturer);

        setOnClickListeners();

    }

    private void setOnClickListeners() {
        signInAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(StudentSignInActivity.class);
            }
        });

        signInAsLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(LecturerSignInActivity.class);
            }
        });

        signUpAsLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(LecturerSignUpActivity.class);
            }
        });
        signUpAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(StudentSignUpActivity.class);
            }
        });
    }

    private void launchActivity(Class activityClass) {
        Intent intent = new Intent(AuthActivity.this, activityClass);
        startActivity(intent);
        finish();
    }

}