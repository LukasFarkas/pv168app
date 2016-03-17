/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.util.List;

/**
 *
 * @author L
 */
public class Teacher {
    public String fullName;
    public String details;
    public int level;
    public long teacherId;
        
    public List<Lesson> listAllLessons(){
        return null;
    }
    
    public String getFullName() {
        return fullName;
    }

    public String getDetails() {
        return details;
    }

    public int getLevel() {
        return level;
    }

    public long getTeacherId() {
        return teacherId;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }
}
