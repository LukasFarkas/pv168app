/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author L
 */
public class Lesson {
    
    public LocalDateTime start;
    public int duration;
    public BigDecimal price;
    public String notes;
    public Teacher teacher;
    public Student student;

    public LocalDateTime getStart() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getNotes() {
        return notes;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    
}
