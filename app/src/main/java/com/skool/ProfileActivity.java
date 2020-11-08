package com.skool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skool.model.User;
import com.squareup.picasso.Picasso;

import static com.skool.model.constants.LECTURE_COVER_IMAGE_PATH;
import static com.skool.model.constants.PROFILE_PHOTO_IMAGE_PATH;
import static com.skool.model.constants.UPLOAD_LECTURE_COVER_IMAGE_REQUEST_CODE;
import static com.skool.model.constants.UPLOAD_PROFILE_PHOTO_REQUEST_CODE;
import static com.skool.model.constants.USER_PATH;

public class ProfileActivity extends AppCompatActivity {
    ImageView profilePhoto;
    Button button;
    String profileImageDownloadUrl;
    String profileImageRef;
    private User user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePhoto = findViewById(R.id.profile_photo_upload);
        button = findViewById(R.id.profile_photo_upload_button);
        getIntentExtra();
        setButtonOnClick();
    }
    private void getIntentExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(USER_PATH)) {
            user = (User) intent.getSerializableExtra(USER_PATH);
        }
    }

    private void setButtonOnClick() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(USER_PATH).child(user.getUserId());
       profilePhoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
               intent.setType("image/jpeg");
               intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
               startActivityForResult(Intent.createChooser(intent, "insert profile photo"), UPLOAD_PROFILE_PHOTO_REQUEST_CODE);
           }
       });

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               user.setImageUrl(profileImageDownloadUrl);

               databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(ProfileActivity.this, "profile photo upload successful", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_PROFILE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.v("Upload Image", "on activity for result called");

            Uri imageUri = data.getData();
            Log.v("Upload Image", "image gotten: " + imageUri.toString());

            uploadImage(imageUri, PROFILE_PHOTO_IMAGE_PATH);

        }

    }

    private void uploadImage(final Uri imageUri, String path) {
        FirebaseStorage db = FirebaseStorage.getInstance();
        final StorageReference storageReference = db.getReference().child(path).child(imageUri.getLastPathSegment());


        profileImageRef = storageReference.getPath();
        Log.v("Upload Image", "Image storage ref: " + profileImageRef);
        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageDownloadUrl = uri.toString();
                            Log.v("Upload Image", "image download url is: " + imageUri);
                            showImage(profileImageDownloadUrl);


                        }
                    });
                } else {
                    Toast.makeText(ProfileActivity.this, "image upload failed" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.v("Upload Image", "image upload failed: " + task.getException().toString());

                }
            }
        });


    }

    private void showImage(String imageUrl) {
        if (imageUrl != null && imageUrl.trim().length()!=0) {
            Picasso.get().load(imageUrl)
                    .into(profilePhoto);
        }
    }
}