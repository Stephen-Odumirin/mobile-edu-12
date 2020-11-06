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
import com.skool.model.LectureCategory;

import java.util.List;

import static com.skool.model.constants.LECTURE_CATEGORY;

public class LectureCategoryAdapter extends RecyclerView.Adapter<LectureCategoryAdapter.ViewHolder> {
    private List<LectureCategory> categoryList;
    private Context context;
    public LectureCategoryAdapter(List<LectureCategory> lectureCategoryList){
        categoryList = lectureCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.lecture_category_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LectureCategory category = categoryList.get(position);
        holder.bind(category);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category_name);
            imageView = itemView.findViewById(R.id.category_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, LectureListActivity.class);
                    intent.putExtra(LECTURE_CATEGORY,textView.getText().toString());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(LectureCategory category) {
           imageView.setImageResource(category.getImageResource());
           textView.setText(category.getCategoryName());

        }
    }
}
