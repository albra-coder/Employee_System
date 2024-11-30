package com.example.employee_system;

public class EmployeeListClass {
    private String date;
    private String email;
    private String id;
    private String name;
    private String password;
    private String salary;
    private String speciality;

    public EmployeeListClass() {
    }

    public EmployeeListClass(String date, String email, String id, String name, String password, String salary, String speciality) {
        this.date = date;
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
        this.salary = salary;
        this.speciality = speciality;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSalary() {
        return salary;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
