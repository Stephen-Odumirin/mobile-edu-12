package com.skool.custom;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import java.util.List;

import static com.skool.model.constants.LECTURE;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder> {
    private List<Lecture> lectures;
    private Context context;
    public LectureAdapter(List<Lecture> lectureList){
        lectures = lectureList;
    }

    @NonNull
    @Override
    public LectureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.lecture_list_item, parent, false);
        return new LectureAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureAdapter.ViewHolder holder, int position) {

        Lecture lecture = lectures.get(position);
        Log.v("lecture clicked on is: ", "onbind called");

        holder.bind(lecture);

    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView category;
        private TextView author;
        private TextView numberOfComments;
        private ImageView imageUrl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.my_lecture_title);
            category = itemView.findViewById(R.id.my_lecture_category);
            author = itemView.findViewById(R.id.my_lecture_date);
            numberOfComments = itemView.findViewById(R.id.my_lecture_number_of_comments);
            imageUrl = itemView.findViewById(R.id.my_lecture_image_item);

        }

        public void bind(final Lecture lecture) {
            title.setText(lecture.getTitle());
            category.setText(lecture.getCategory());
            author.setText(lecture.getAuthor());
            numberOfComments.setText(String.valueOf(lecture.getNumberOfComments()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("lecture clicked on is: ", "clicked");
                    Intent intent = new Intent(v.getContext(), LectureDetailsActivity.class);
                    intent.putExtra(LECTURE,lecture);
                    Log.v("lecture clicked on is: ", lecture.getTitle());
                    v.getContext().startActivity(intent);

                }
            });

        }
    }
}
