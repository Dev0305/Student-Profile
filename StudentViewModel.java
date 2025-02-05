package com.example.studentprofiledb;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;

public class StudentViewModel extends AndroidViewModel {
    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<ArrayList<Student>> studentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Uri> selectedImageUri = new MutableLiveData<>();

    public StudentViewModel(@NonNull Application application) {
        super(application);
        databaseHelper = new DatabaseHelper(application);
        loadStudents();
    }

    public LiveData<ArrayList<Student>> getStudents() {
        return studentsLiveData;
    }

    public LiveData<Uri> getSelectedImageUri() {
        return selectedImageUri;
    }

    public void loadStudents() {
        studentsLiveData.setValue(databaseHelper.getAllStudents());
    }

    public void insertStudent(String name, String number, String imagePath) {
        databaseHelper.insertStudent(name, number, imagePath);
        loadStudents();
    }

    public void updateStudent(int id, String name, String number, String imagePath) {
        databaseHelper.updateStudent(id, name, number, imagePath);
        loadStudents();
    }

    public void deleteStudent(int id) {
        databaseHelper.deleteStudent(id);
        loadStudents();
    }

    public void setSelectedImage(Uri uri) {
        selectedImageUri.setValue(uri);
    }
}
