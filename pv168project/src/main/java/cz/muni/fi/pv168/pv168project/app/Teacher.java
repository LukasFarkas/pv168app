/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author L
 */
public class Teacher {
    private String fullName;
    private String details;
    private int level;
    private Long id;
        
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

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "Teacher{" + "id=" + id + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this.id == null && this != obj) {
            // this is a special case - two entities without id assigned yet
            // should be evaluated as non equal
            return false;
        }
        final Teacher other = (Teacher) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
