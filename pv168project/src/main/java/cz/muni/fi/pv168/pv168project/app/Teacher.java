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
public class Teacher {
    private String fullName;
    private Region region;
    private BigDecimal price;
    private int skill;
    private Long id;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getFullName() {
        return fullName;
    }

    public int getSkill() {
        return skill;
    }

    public Long getId() {
        return id;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSkill (int level) {
        this.skill = level;
    }

    public void setId(Long id) {
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
