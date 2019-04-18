package com.data.nycschools.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.data.nycschools.BR;

import java.text.DecimalFormat;

public class School extends BaseObservable {

    public String dbn;
    double attendance_rate;
    String school_name;
    String primary_address_line_1;
    String city;
    String state_code;
    String zip;
    public String phone_number;
    String extracurricular_activities;
    public String website;
    String grades2018;
    String total_students;
    public SatScore[] satScores = new SatScore[]{};

    public School(String dbn){
        this.dbn = dbn;
    }

    @Bindable
    public String getFormattedAddress(){
        return String.format("%s, %s, %s %s", primary_address_line_1, city, state_code, zip);
    }

    @Bindable
    public String getExtraCurricularActivities() {
        return extracurricular_activities;
    }

    @Bindable
    public String getAttendanceRate(){
        return "Attendance: "+new DecimalFormat("#0.00%").format(attendance_rate);
    }

    @Bindable
    public String getSchoolPhone() {
        return (phone_number == null)?"NA": phone_number;
    }

    @Bindable
    public String getTotalStudents() {
        return "Total Students: "+total_students;
    }

    @Bindable
    public String getSchoolName() {
        return school_name;
    }

    @Bindable
    public String getSchoolGrades() {
        return "Grades: "+((grades2018 != null && grades2018.length() < 8)? grades2018: "NA");
    }

    @Bindable
    public String getCityName(){
        return (city == null)?"NA": city;
    }

    @Bindable
    public String getMathScore(){
        return (satScores != null && satScores.length > 0)? satScores[0].sat_math_avg_score: "";
    }

    @Bindable
    public String getReadingScore(){
        return (satScores != null && satScores.length > 0)? satScores[0].sat_critical_reading_avg_score:"";
    }

    @Bindable
    public String getWritingScore(){
        return (satScores != null && satScores.length > 0)?satScores[0].sat_writing_avg_score:"";
    }

    @Bindable
    public boolean isSatScoresNonEmpty(){
        return satScores != null && satScores.length > 0;
    }

    // overriden equals method to provide a unique index based on
    // school dbn. Helpful for cases when an object's index is seeked from a
    // collection. hashCode() should be overrided too, but isnt tested here.
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof School)) return false;
        return this.dbn.equalsIgnoreCase(((School) object).dbn);
    }

    // Notifies the bindable properties after API response
    // obtained from SAT scores
    public void notifyChanges() {
        notifyPropertyChanged(BR.satScoresNonEmpty);
        notifyPropertyChanged(BR.mathScore);
        notifyPropertyChanged(BR.readingScore);
        notifyPropertyChanged(BR.writingScore);
    }
}
