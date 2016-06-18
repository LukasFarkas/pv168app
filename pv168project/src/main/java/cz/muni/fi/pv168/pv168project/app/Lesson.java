/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author L
 */
public class Lesson {
    
    //private LocalDateTime start;
    private int skill;
    private Region region;
    private BigDecimal price;
    private Long teacherId; //metoda ktera bude vytvared lesson mi nastavi kdo s kym tu lekci ma
    private Long studentId; //udelal jsem z toho id abych se zbavil nutnosti mit v lesson managerovi teacher a student managera
    private Long id;

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    
    
    @Override
    public String toString() {
        return "Lesson{" + "id=" + id + '}' + "teacher-student" + getTeacherId() + "-" + getStudentId();
    }

    @Override
    public boolean equals(Object obj) {
        // should comapre IDs or teacher-student 
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
        final Lesson other = (Lesson) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    
}
