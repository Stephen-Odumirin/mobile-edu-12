package com.skool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Objects;

import static android.util.Log.v;
import static com.skool.model.constants.LECTURE;
import static com.skool.model.constants.LECTURE_ATTACHMENT_PATH;
import static com.skool.model.constants.LECTURE_COVER_IMAGE_PATH;
import static com.skool.model.constants.UPLOAD_LECTURE_ATTACHMENT_REQUEST_CODE;
import static com.skool.model.constants.UPLOAD_LECTURE_COVER_IMAGE_REQUEST_CODE;
import static com.skool.model.constants.USER_PATH;

public class AddLectureActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView imageView;
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
    private String lectureMimeType;

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


        uploadImageOnClickListener();
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
                v("Upload lecture attach", "start activity for result");
                startActivityForResult(Intent.createChooser(intent, "insert lecture attachment"), UPLOAD_LECTURE_ATTACHMENT_REQUEST_CODE);
            }
        });
    }

    private void uploadImageOnClickListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
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
                v("saving lecture", "fire store instance gotten");
                DocumentReference newLectureRefrence = database.collection(LECTURE).document();
                v("saving lecture", "documrnt refrence created");
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
                    lecture.setLectureMimeType(lectureMimeType);
                    Log.v("attach", lecture.getLectureMimeType());

                    lecture.setLectureResourceStorageRef(lectureAttachmentRef);
                    lecture.setLectureResourceUrl(lectureAttachmentDownloadUrl);
                    showProgressBar();
                    v("saving lecture", "saving lecture in database");
                    newLectureRefrence.set(lecture).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressBar();
                            if (task.isSuccessful()) {
                                toast = Toast.makeText(AddLectureActivity.this, "Lecture Upload Successful!", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(AddLectureActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                toast = Toast.makeText(AddLectureActivity.this, "Lecture Upload failed!" + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT);
                                toast.show();
                                v("saving lecture", "failed" + Objects.requireNonNull(task.getException()).getLocalizedMessage());
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
            v("Upload Image", "on activity for result called");

            Uri imageUri = data.getData();
            v("Upload Image", "image gotten: " + imageUri.toString());

            uploadImage(imageUri, LECTURE_COVER_IMAGE_PATH);

        }

        else if(requestCode==UPLOAD_LECTURE_ATTACHMENT_REQUEST_CODE && resultCode==RESULT_OK){
            v("Upload lecture attach", "on activity for result called");
            Uri lectureAttachmentUri = data.getData();
            if(lectureAttachmentUri!=null) {
                try {
                    String path = getPath(this, lectureAttachmentUri);
                    lectureMimeType = getFileExtension(path);

                    v("Upload lecture attach", "lecture attchment path gotten" + path);
                    v("Upload lecture attach", "lecture type gotten" + lectureMimeType);

                    uploadFile(lectureAttachmentUri, LECTURE_ATTACHMENT_PATH);
                }
                catch (Exception e){
                    v("Upload lecture attach", e.getLocalizedMessage());

                }
            }
        }
        else{
            v("Upload lecture attach", "lecture attchment failed"+ requestCode +" "+ resultCode);
        }
    }

    private void uploadFile(Uri lectureAttachmentUri, String lectureAttachmentPath) {
        FirebaseStorage db = FirebaseStorage.getInstance();
        final StorageReference storageReference = db.getReference().child(lectureAttachmentPath).child(lectureAttachmentUri.getLastPathSegment());
        lectureAttachmentRef = storageReference.getPath();
        v("Upload lecture attach", "lecture attach storage ref: " + lectureAttachmentRef);
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
                            v("Upload lecture attach", "lecture attachment download url is: " + lectureAttachmentDownloadUrl);
                            uploadLectureStatus.setText(R.string.upload_sucess_alert);

                        }
                    });
                } else {
                    Toast.makeText(AddLectureActivity.this, "attachment upload failed" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    v("Upload lecture attach", "attachment upload failed: " + task.getException().toString());
                    uploadLectureStatus.setText(R.string.upload_failure_alert);

                }
            }
        });


    }

    private void uploadImage(final Uri imageUri, String path) {
        FirebaseStorage db = FirebaseStorage.getInstance();
        final StorageReference storageReference = db.getReference().child(path).child(imageUri.getLastPathSegment());
        imageRef = storageReference.getPath();
        v("Upload Image", "Image storage ref: " + imageRef);
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
                            v("Upload Image", "image download url is: " + imageUri);
                            showImage(imageDownloadUrl);

                        }
                    });
                } else {
                    Toast.makeText(AddLectureActivity.this, "image upload failed" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    v("Upload Image", "image upload failed: " + task.getException().toString());

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

    public  String getPath(Context context, Uri uri) throws URISyntaxException {
        if("content".equalsIgnoreCase(uri.getScheme())){
         String[] projection = {"_data"};
            Cursor cursor;
            try{
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if(cursor.moveToFirst()){
                    return cursor.getString(column_index);
                }
                cursor.close();

            }
            catch (Exception e){
                v("exception", e.getLocalizedMessage());
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

    public String getFileExtension(String filePath){
        String extension = "";
        try {
            extension  = filePath.substring(filePath.lastIndexOf("."));
        }
        catch (Exception e){
            Log.v("exception", e.getLocalizedMessage());
        }

        return extension;
    }

}