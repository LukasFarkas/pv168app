/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.math.BigDecimal;

/**
 *
 * @author L
 */
public class LessonBuilder {
    private Long id;
    private int skill;
    private BigDecimal price;
    private Region region;
    private Long teacher;
    private Long student;

    public LessonBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public LessonBuilder teacher(Long tid) {
        this.teacher = tid;
        return this;
    }
    
    public LessonBuilder student (Long sid) {
        this.student = sid;
        return this;
    }

    public LessonBuilder skill(int skill) {
        this.skill = skill;
        return this;
    }

    public LessonBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public LessonBuilder region(Region region) {
        this.region = region;
        return this;
    }

    public Lesson build() {
        Lesson s = new Lesson();
        s.setId(id);
        s.setStudentId(student);
        s.setTeacherId(teacher);
        s.setSkill(skill);
        s.setRegion(region);
        s.setPrice(price);
        return s;
    }
}
