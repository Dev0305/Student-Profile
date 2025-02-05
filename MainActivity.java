package com.example.studentprofiledb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.studentprofiledb.databinding.ActivityMainBinding;
import com.example.studentprofiledb.databinding.DialogStudentBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private StudentViewModel viewModel;
    private StudentAdapter adapter;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        setupRecyclerView();
        setupFabButton();

        viewModel.getStudents().observe(this, adapter::updateStudentList);
    }

    private void setupRecyclerView() {
        adapter = new StudentAdapter(new ArrayList<>(), this, new StudentAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Student student) {
                showStudentDialog(student);
            }

            @Override
            public void onDelete(Student student) {
                confirmDelete(student);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupFabButton() {
        binding.fab.setOnClickListener(v -> showStudentDialog(null));
    }

    private void showStudentDialog(Student student) {
        DialogStudentBinding dialogBinding = DialogStudentBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .setPositiveButton(student == null ? "Add" : "Update", (d, which) -> saveStudent(student, dialogBinding))
                .setNegativeButton("Cancel", null)
                .show();

        if (student != null) {
            dialogBinding.etStudentName.setText(student.getName());
            dialogBinding.etStudentEmail.setText(student.getNumber());
            selectedImageUri = Uri.parse(student.getImagePath());
        }

        dialogBinding.ivProfilePicture.setOnClickListener(v -> selectImage());
    }

    private void saveStudent(Student student, DialogStudentBinding binding) {
        String name = binding.etStudentName.getText().toString().trim();
        String email = binding.etStudentEmail.getText().toString().trim();
        String imagePath = (selectedImageUri != null) ? selectedImageUri.toString() : (student != null ? student.getImagePath() : "");

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(imagePath)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (student == null) {
            viewModel.insertStudent(name, email, imagePath);
        } else {
            viewModel.updateStudent(student.getId(), name, email, imagePath);
        }
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                }
            });

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void confirmDelete(Student student) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteStudent(student.getId()))
                .setNegativeButton("Cancel", null)
                .show();
    }
}
