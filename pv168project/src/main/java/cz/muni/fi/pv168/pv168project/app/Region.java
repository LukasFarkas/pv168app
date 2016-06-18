/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

/**
 *
 * @author L
 */
public enum Region {
    INDIA (0),
    CZECH_REPUBLIC (1),
    SLOVAKIA (2),
    RUSSIA (3),
    ENGLAND (4),
    NORTH_AMERICA (5);
    
    private final int index;

    Region(int index) {
        this.index = index;
    }

    int getIndex() {
        return index;
    }
    
    public static Region getByIndex(int index) {
        for (Region r : Region.values()) {
            if (r.index == index) {
                return r;
            }
        }
        return null;
    }
}
