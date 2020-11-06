package com.skool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skool.model.Lecture;
import com.skool.model.User;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.skool.model.constants.LECTURE;
import static com.skool.model.constants.LECTURE_ATTACHMENT_PATH;
import static com.skool.model.constants.LECTURE_COVER_IMAGE_PATH;
import static com.skool.model.constants.UPLOAD_LECTURE_ATTACHMENT_REQUEST_CODE;
import static com.skool.model.constants.UPLOAD_LECTURE_COVER_IMAGE_REQUEST_CODE;
import static com.skool.model.constants.USER_PATH;

public class AddLectureActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView imageView;
    private Button uploadImageButton;
    private Button uploadLectureButton;
    private TextView uploadLectureStatus;
    private EditText lectureTitleEt;
    private EditText lectureDescriptionEt;
    private Spinner lectureCategorySpinner;
    private Button saveLectureButton;
    private ProgressBar progressBar;


    private String lectureCategory;
    Toast toast;
    private User user;
    String imageRef;
    String imageDownloadUrl;
    String lectureAttachmentRef;
    String lectureAttachmentDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);
        initViews();
        getIntentExtra();
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(USER_PATH)) {
            user = (User) intent.getSerializableExtra(USER_PATH);
        }
    }

    private void initViews() {
        imageView = findViewById(R.id.uploaded_image_view);
        uploadImageButton = findViewById(R.id.upload_image_button);
        uploadLectureButton = findViewById(R.id.upload_lecture_button);
        uploadLectureStatus = findViewById(R.id.upload_lecture_status);
        lectureTitleEt = findViewById(R.id.lecture_title_edit_text);
        lectureDescriptionEt = findViewById(R.id.lecture_description_edit_text);
        lectureCategorySpinner = findViewById(R.id.category_spinner);
        saveLectureButton = findViewById(R.id.save_lecture_button);
        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lectureCategorySpinner.setAdapter(adapter);
        lectureCategorySpinner.setOnItemSelectedListener(this);


        uploadImageButtonOnClickListener();
        uploadLectureResourceButtonOnClickListener();

        saveButtonOnClickListener();
    }

    private void uploadLectureResourceButtonOnClickListener() {

        uploadLectureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                String[] mimeTypes ={"image/jpeg","audio/midi", "audio/aac", "audio/wav", "video/mp4", "image/png", "application/pdf", "text/plain"};
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                Log.v("Upload lecture attach", "start activity for result");
                startActivityForResult(Intent.createChooser(intent, "insert lecture attachment"), UPLOAD_LECTURE_ATTACHMENT_REQUEST_CODE);
            }
        });
    }

    private void uploadImageButtonOnClickListener() {
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "insert lecture display photo"), UPLOAD_LECTURE_COVER_IMAGE_REQUEST_CODE);
            }
        });
    }

    private void saveButtonOnClickListener() {
        saveLectureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                Log.v("saving lecture", "fire store instance gotten");
                DocumentReference newLectureRefrence = database.collection(LECTURE).document();
                Log.v("saving lecture", "documrnt refrence created");
                String lectureTitle = lectureTitleEt.getText().toString().trim();
                String lectureDesc = lectureDescriptionEt.getText().toString().trim();
                if (lectureTitle.isEmpty() || lectureDesc.isEmpty() || lectureCategory.isEmpty()) {
                    toast = Toast.makeText(AddLectureActivity.this, "Fill in all necessary fields", Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    Lecture lecture = new Lecture(lectureTitle, user.getFirstName() + " " + user.getLastName(), " ", lectureCategory, lectureDesc, user.getUserId());
                    lecture.setId(newLectureRefrence.getId());
                    lecture.setImageStorageRef(imageRef);
                    lecture.setImageUrl(imageDownloadUrl);
                    showProgressBar();
                    Log.v("saving lecture", "saving lecture in database");
                    newLectureRefrence.set(lecture).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressBar();
                            if (task.isSuccessful()) {
                                toast = Toast.makeText(AddLectureActivity.this, "Lecture Upload Successful!", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(AddLectureActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                toast = Toast.makeText(AddLectureActivity.this, "Lecture Upload failed!" + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT);
                                toast.show();
                                Log.v("saving lecture", "failed" + Objects.requireNonNull(task.getException()).getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
//        imageView.setVisibility(View.GONE);
//        uploadLectureStatus.setVisibility(View.GONE);
//        uploadLectureButton.setVisibility(View.GONE);
//        uploadImageButton.setVisibility(View.GONE);
//        lectureDateEt.setVisibility(View.GONE);
//        lectureCategorySpinner.set
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        lectureCategory = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        try {
            if (toast.getView().isShown()) {
                toast.setText("Select a Category for your lecture");
            }
        } catch (Exception e) {
            toast = Toast.makeText(this, "Select a Category for your lecture", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_LECTURE_COVER_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.v("Upload Image", "on activity for result called");

            Uri imageUri = data.getData();
            Log.v("Upload Image", "image gotten: " + imageUri.toString());

            uploadImage(imageUri, LECTURE_COVER_IMAGE_PATH);

        }

        if(requestCode==UPLOAD_LECTURE_ATTACHMENT_REQUEST_CODE&& resultCode==RESULT_OK){
            Log.v("Upload lecture attach", "on activity for result called");
            Uri lectureAttachmentUri = data.getData();
            Log.v("Upload lecture attach", "lecture attchment gotten"+ lectureAttachmentUri.toString());

            uploadFile(lectureAttachmentUri, LECTURE_ATTACHMENT_PATH);
        }
        else{
            Log.v("Upload lecture attach", "lecture attchment failed"+ String.valueOf(requestCode) +" "+ String.valueOf(resultCode));
        }
    }

    private void uploadFile(Uri lectureAttachmentUri, String lectureAttachmentPath) {
        FirebaseStorage db = FirebaseStorage.getInstance();
        final StorageReference storageReference = db.getReference().child(lectureAttachmentPath).child(lectureAttachmentUri.getLastPathSegment());
        lectureAttachmentRef = storageReference.getPath();
        Log.v("Upload lecture attach", "lecture attach storage ref: " + lectureAttachmentRef);
        progressBar.setVisibility(View.VISIBLE);
        storageReference.putFile(lectureAttachmentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(AddLectureActivity.this, "attachment uploaded successfully", Toast.LENGTH_SHORT).show();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            lectureAttachmentDownloadUrl = uri.toString();
                            Log.v("Upload lecture attach", "lecture attachment download url is: " + lectureAttachmentDownloadUrl);
                            showImage(imageDownloadUrl);
                            uploadLectureStatus.setText("attachment upload sucessful");

                        }
                    });
                } else {
                    Toast.makeText(AddLectureActivity.this, "attachment upload failed" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.v("Upload lecture attach", "attachment upload failed: " + task.getException().toString());
                    uploadLectureStatus.setText("attachment upload failed");

                }
            }
        });


    }

    private void uploadImage(final Uri imageUri, String path) {
        FirebaseStorage db = FirebaseStorage.getInstance();
        final StorageReference storageReference = db.getReference().child(path).child(imageUri.getLastPathSegment());
        imageRef = storageReference.getPath();
        Log.v("Upload Image", "Image storage ref: " + imageRef);
        progressBar.setVisibility(View.VISIBLE);
        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(AddLectureActivity.this, "image upload sucessful", Toast.LENGTH_SHORT).show();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageDownloadUrl = uri.toString();
                            Log.v("Upload Image", "image download url is: " + imageUri);
                            showImage(imageDownloadUrl);

                        }
                    });
                } else {
                    Toast.makeText(AddLectureActivity.this, "image upload failed" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.v("Upload Image", "image upload failed: " + task.getException().toString());

                }
            }
        });


    }

    private void showImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl)
                    .into(imageView);
        }
    }
}