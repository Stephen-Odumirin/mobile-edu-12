package com.skool.custom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skool.LectureListActivity;
import com.skool.R;
import com.skool.model.Comment;
import com.skool.model.LectureCategory;

import java.util.List;

import static com.skool.model.constants.LECTURE_CATEGORY;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> comments;
    public CommentAdapter(List<Comment> commentList){
        comments = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_questions, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comment comment = comments.get(position);
        holder.bind(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView commentBody;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commentor_name);
            commentBody = itemView.findViewById(R.id.comment_body);

        }

        public void bind(Comment comment) {
           name.setText(comment.getName());
           commentBody.setText(comment.getCommentBody());

        }
    }

    public void setComments(List<Comment> newComments){
        comments=newComments;
        notifyDataSetChanged();
    }
}
