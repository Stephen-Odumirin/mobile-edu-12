package com.skool.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.skool.R;
import com.skool.splash.AuthActivity;

public class StudentSignUpActivity extends AppCompatActivity {
    EditText studentFirstNameEt;
    EditText studentLastNameEt;
    EditText studentEmailEt;
    EditText studentPasswordEt;
    EditText studentConfirmPasswordEt;
    Button studentSignUpBtn;
    TextView studentSignInTv;
    TextView lecturerSignInTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        initViews();
        setOnClickListeners();

    }

    private void setOnClickListeners() {
        lecturerSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(LecturerSignInActivity.class);
            }
        });
        studentSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(StudentSignInActivity.class);
            }
        });
    }

    private void initViews() {
        studentFirstNameEt = findViewById(R.id.student_sign_up_first_name);
        studentLastNameEt = findViewById(R.id.student_sign_up_last_name);
        studentEmailEt = findViewById(R.id.student_sign_up_email);
        studentPasswordEt = findViewById(R.id.student_sign_up_password);
        studentConfirmPasswordEt = findViewById(R.id.student_sign_up_confirm_password);
        studentSignUpBtn = findViewById(R.id.student_sign_up_button);
        studentSignInTv = findViewById(R.id.sign_in_as_student_tv);
        lecturerSignInTv = findViewById(R.id.sign_in_as_lecturer_tv);
    }
    private void launchActivity(Class activityClass) {
        Intent intent = new Intent(StudentSignUpActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}