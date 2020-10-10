package com.skool;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Student_SignUp extends Fragment {
    private static EditText emailId;
    private static EditText password, confirmpassword, firstname, lastname;
    private static Button signUpbutton, loginButton;



    public Student_SignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.student__sign_up, container, false);
    }
}