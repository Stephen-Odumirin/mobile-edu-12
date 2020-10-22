package com.skool.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.skool.R;


public class StudentSignUpActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private static final String DOMAIN_NAME = "gmail.com";


    //private FirebaseAuth.AuthStateListener mAuthListener;



    EditText studentFirstNameEt;
    EditText studentLastNameEt;
    EditText studentEmailEt;
    EditText studentPasswordEt;
    EditText studentConfirmPasswordEt;
    Button studentSignUpBtn;
    Button studentSignInBtn;
    TextView studentSignInTv;
    TextView lecturerSignInTv;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);


        studentFirstNameEt = findViewById(R.id.student_sign_up_first_name);
        studentLastNameEt = findViewById(R.id.student_sign_up_last_name);
        studentEmailEt = findViewById(R.id.student_sign_up_email);
        studentPasswordEt = findViewById(R.id.student_sign_up_password);
        studentConfirmPasswordEt = findViewById(R.id.student_sign_up_confirm_password);
        studentSignUpBtn = findViewById(R.id.student_sign_up_button);
        studentSignInBtn = findViewById(R.id.student_sign_in_button);
        studentSignInTv = findViewById(R.id.sign_in_as_student_tv);
        lecturerSignInTv = findViewById(R.id.sign_in_as_lecturer_tv);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);



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



        studentSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields

                if(!isEmpty(studentEmailEt.getText().toString())
                        && !isEmpty(studentPasswordEt.getText().toString())
                        && !isEmpty(studentConfirmPasswordEt.getText().toString())){

                    //check if user has a company email address

                    if(isValidDomain(studentEmailEt.getText().toString())){

                        //check if passwords match
                        if(doStringsMatch(studentPasswordEt.getText().toString(), studentConfirmPasswordEt.getText().toString())){

                            //Initiate registration task
                            registerNewEmail(studentEmailEt.getText().toString(), studentPasswordEt.getText().toString());
                        }else{
                            Toast.makeText(StudentSignUpActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(StudentSignUpActivity.this, "Please Register with Company Email", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(StudentSignUpActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hideSoftKeyboard();

    }

   // Register a new email and password to Firebase Authentication
    public void registerNewEmail(final String email, String password){

        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            FirebaseAuth.getInstance().signOut();

                            //redirect the user to the login screen
                            redirectLoginScreen();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(StudentSignUpActivity.this, "Unable to Register",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();

                        // ...
                    }
                });
    }

   // Returns True if the user's email contains '@gmail.com'
    private boolean isValidDomain(String email){
        Log.d(TAG, "isValidDomain: verifying email has correct domain: " + email);
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        Log.d(TAG, "isValidDomain: users domain: " + domain);
        return domain.equals(DOMAIN_NAME);
    }

   // Redirects the user to the login screen

    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(StudentSignUpActivity.this, StudentSignInActivity.class);
        startActivity(intent);
        finish();
    }
   // Return true if @param 's1' matches @param 's2'
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }

   //Return true if the @param is null
    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void launchActivity(Class activityClass) {
        Intent intent = new Intent(StudentSignUpActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}