package com.example.studentprofiledb;

public class Student {
    private int id;
    private String name;
    private String number;
    private String imagePath;

    public Student(int id, String name, String number, String imagePath) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getNumber() { return number; }
    public String getImagePath() { return imagePath; }
}