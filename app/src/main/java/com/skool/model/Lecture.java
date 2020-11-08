package com.skool.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@IgnoreExtraProperties
public class Lecture implements Parcelable {

   private String title;
   private String id;
   private String author;
   private String authorId;
   private @ServerTimestamp Date date;
   private String imageUrl;
   private String imageStorageRef;
   private String lectureResourceStorageRef;
   private String category;
   private String description;
   private String lectureResourceUrl;
   private List<Comment> comments;
   private int numberOfComments;


   public Lecture(){}
    public Lecture(String title, String author, String imageUrl, String category, String description, String authorId) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;
        comments = new ArrayList<>();
        this.authorId = authorId;
    }



    protected Lecture(Parcel in) {
        title = in.readString();
        author = in.readString();
        imageUrl = in.readString();
        category = in.readString();
        description = in.readString();
        lectureResourceUrl = in.readString();
        numberOfComments = in.readInt();
        comments = in.readArrayList(null);
        date = (Date) in.readSerializable();
        id = in.readString();
        authorId = in.readString();
        imageStorageRef = in.readString();
        lectureResourceStorageRef = in.readString();
    }

    public static final Creator<Lecture> CREATOR = new Creator<Lecture>() {
        @Override
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getLectureResourceUrl() {
        return lectureResourceUrl;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLectureResourceUrl(String lectureResourceUrl) {
        this.lectureResourceUrl = lectureResourceUrl;
    }

    public String getImageStorageRef() {
        return imageStorageRef;
    }

    public void setImageStorageRef(String imageStorageRef) {
        this.imageStorageRef = imageStorageRef;
    }

    public String getLectureResourceStorageRef() {
        return lectureResourceStorageRef;
    }

    public void setLectureResourceStorageRef(String lectureResourceStorageRef) {
        this.lectureResourceStorageRef = lectureResourceStorageRef;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public int getNumberOfComments() {
        return comments.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(imageUrl);
        parcel.writeString(category);
        parcel.writeString(description);
        parcel.writeString(lectureResourceUrl);
        parcel.writeInt(numberOfComments);
        parcel.writeList(comments);
        parcel.writeSerializable(date);
        parcel.writeString(id);
        parcel.writeString(authorId);
        parcel.writeString(imageStorageRef);
        parcel.writeString(lectureResourceStorageRef);
    }
}
