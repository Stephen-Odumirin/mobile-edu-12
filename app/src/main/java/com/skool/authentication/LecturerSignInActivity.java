package com.skool.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.skool.R;

public class LecturerSignInActivity extends AppCompatActivity {

    EditText lecturerEmailEt;
    EditText lecturerPasswordEt;
    Button lecturerSignInBtn;
    TextView createLecturerAccountTv;
    TextView signInAsStudentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_sign_in);

        initViews();
        setOnClickListeners();
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
        lecturerSignInBtn = findViewById(R.id.lecturer_sign_in_button);
        createLecturerAccountTv = findViewById(R.id.create_lecturer_account_tv);
        signInAsStudentTv = findViewById(R.id.sign_in_as_student_tv);
    }

    private void launchActivity(Class activityClass) {
        Intent intent = new Intent(LecturerSignInActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}