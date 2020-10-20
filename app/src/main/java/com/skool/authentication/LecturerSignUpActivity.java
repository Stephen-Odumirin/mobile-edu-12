package com.skool.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.ETC1;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.skool.R;

public class LecturerSignUpActivity extends AppCompatActivity {
    EditText lecturerFirstNameEt;
    EditText lecturerLastNameEt;
    EditText lecturerEmailEt;
    EditText lecturerPasswordEt;
    EditText lecturerConfirmPasswordEt;
    Button lecturerSignUpBtn;
    TextView lecturerSignInTv;
    TextView studentSignInTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_sign_up);
        intViews();
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

    private void intViews() {
        lecturerFirstNameEt = findViewById(R.id.lecturer_sign_up_first_name);
        lecturerLastNameEt = findViewById(R.id.lecturer_sign_up_last_name);
        lecturerEmailEt = findViewById(R.id.lecturer_sign_up_email);
        lecturerPasswordEt = findViewById(R.id.lecturer_sign_up_password);
        lecturerConfirmPasswordEt = findViewById(R.id.lecturer_sign_up_confirm_password);
        lecturerSignUpBtn = findViewById(R.id.lecturer_sign_up_button);
        lecturerSignInTv = findViewById(R.id.sign_in_as_lecturer_tv);
        studentSignInTv = findViewById(R.id.sign_in_as_student_tv);
    }
    private void launchActivity(Class activityClass) {
        Intent intent = new Intent(LecturerSignUpActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}