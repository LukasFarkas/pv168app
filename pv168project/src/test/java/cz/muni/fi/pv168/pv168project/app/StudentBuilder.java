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
public class StudentBuilder {
    private Long id;
    private String fullName;
    private int skill;
    private BigDecimal price;
    private Region region;

    public StudentBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public StudentBuilder fullName(String name) {
        this.fullName = name;
        return this;
    }

    public StudentBuilder skill(int skill) {
        this.skill = skill;
        return this;
    }

    public StudentBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public StudentBuilder region(Region region) {
        this.region = region;
        return this;
    }

    public Student build() {
        Student s = new Student();
        s.setId(id);
        s.setFullName(fullName);
        s.setSkill(skill);
        s.setRegion(region);
        s.setPrice(price);
        return s;
    }
}
