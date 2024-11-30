package com.example.employee_system;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeDetails implements Parcelable {
    private String id;
    private String name;
    private String email;
    private String entryDate;
    private String password;
    private String salary;
    private String speciality;

    // Default constructor (needed for Firebase and Parcelable)
    public EmployeeDetails() {}

    // Parameterized constructor
    public EmployeeDetails(String id, String name, String email, String entryDate, String password, String salary, String speciality) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.entryDate = entryDate;
        this.password = password;
        this.salary = salary;
        this.speciality = speciality;
    }

    // Parcelable constructor, used when recreating the object from a Parcel
    protected EmployeeDetails(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        entryDate = in.readString();
        password = in.readString();
        salary = in.readString();
        speciality = in.readString();
    }

    // Parcelable Creator, required for Parcelable implementation
    public static final Parcelable.Creator<EmployeeDetails> CREATOR = new Parcelable.Creator<EmployeeDetails>() {
        @Override
        public EmployeeDetails createFromParcel(Parcel in) {
            return new EmployeeDetails(in);
        }

        @Override
        public EmployeeDetails[] newArray(int size) {
            return new EmployeeDetails[size];
        }
    };

    // Getter and Setter methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEntryDate() { return entryDate; }
    public void setEntryDate(String entryDate) { this.entryDate = entryDate; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    // Required method for Parcelable, can return 0 as no special contents
    @Override
    public int describeContents() {
        return 0;
    }

    // Writing object data to the Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(entryDate);
        dest.writeString(password);
        dest.writeString(salary);
        dest.writeString(speciality);
    }
}
