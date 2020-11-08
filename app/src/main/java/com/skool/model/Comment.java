package com.skool.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Comment implements Serializable {
    private  String name;
    private String commentBody;

    public Comment(String name, String commentBody) {
        this.name = name;
        this.commentBody = commentBody;
    }

    public Comment(){}

    public String getName() {
        return name;
    }

    public String getCommentBody() {
        return commentBody;
    }


}
