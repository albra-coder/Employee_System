package com.example.employee_system;

public class EmployeeItem {
    String name;
    String id;
    String email;
    String salary;
    String date;
    String speciality;
    String imageName;
    String password;
    boolean expanded;

    public EmployeeItem() {
    }

    public EmployeeItem(String name, String id, String email, String salary, String date, String speciality, String imageName, String password) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.salary = salary;
        this.date = date;
        this.speciality = speciality;
        this.imageName = imageName;
        this.password = password;
        this.expanded = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}