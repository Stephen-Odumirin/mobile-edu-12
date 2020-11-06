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

import com.skool.LectureDetailsActivity;
import com.skool.R;
import com.skool.model.Lecture;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.skool.model.constants.LECTURE;

public class MyLectureAdapter extends RecyclerView.Adapter<MyLectureAdapter.ViewHolder> {
    private List<Lecture> lectures;
    private Context context;
    public MyLectureAdapter(List<Lecture> lectureList){
        lectures = lectureList;
    }

    @NonNull
    @Override
    public MyLectureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.my_lecture_list_item, parent, false);
        return new MyLectureAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLectureAdapter.ViewHolder holder, int position) {

        Lecture lecture = lectures.get(position);
        holder.bind(lecture);

    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView category;
        private TextView date;
        private TextView numberOfComments;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.my_lecture_title);
            category = itemView.findViewById(R.id.my_lecture_category);
            date = itemView.findViewById(R.id.my_lecture_date);
            numberOfComments = itemView.findViewById(R.id.my_lecture_number_of_comments);
            image = itemView.findViewById(R.id.my_lecture_image_item);


        }

        public void bind(final Lecture lecture) {
            title.setText(lecture.getTitle());
            category.setText(lecture.getCategory());
            date.setText(null);
            numberOfComments.setText(String.valueOf(lecture.getNumberOfComments()));
            showImage(lecture.getImageUrl());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, LectureDetailsActivity.class);
                    intent.putExtra(LECTURE,lecture);
                    context.startActivity(intent);

                }
            });

        }

        private void showImage(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl)
                        .into(image);
            }
        }
    }
}
