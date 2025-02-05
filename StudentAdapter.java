package com.example.studentprofiledb;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.studentprofiledb.databinding.ItemStudentBinding;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> studentList;
    private final Context context;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEdit(Student student);
        void onDelete(Student student);
    }

    public StudentAdapter(List<Student> studentList, Context context, OnItemActionListener listener) {
        this.studentList = studentList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudentBinding binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StudentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void updateStudentList(List<Student> newStudents) {
        this.studentList = newStudents;
        notifyDataSetChanged();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private final ItemStudentBinding binding;

        public StudentViewHolder(ItemStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Student student) {
            binding.studentName.setText(student.getName());
            binding.studentNumber.setText(student.getNumber());


            if (student.getImagePath() != null && !student.getImagePath().isEmpty()) {
                Glide.with(context)
                        .load(Uri.parse(student.getImagePath()))
                        .into(binding.profileImage);
            } else {
                binding.profileImage.setImageResource(R.drawable.default_profile); // Set default image
            }

            binding.editIcon.setOnClickListener(v -> listener.onEdit(student));
            binding.deleteIcon.setOnClickListener(v -> listener.onDelete(student));
        }
    }
}
