/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author tomf
 */
public class EntityListRenderer extends JLabel implements ListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList jlist, Object e, int index, boolean isSelected, boolean cellHasFocus) {
            
            if (e.getClass().equals(Student.class) ) {
                Student student = (Student) e;
                setText(student.getFullName());
            }
            else if (e.getClass().equals(Teacher.class)) {
                Teacher teacher = (Teacher) e;
                setText(teacher.getFullName());   
            }
            else  {
                throw new IllegalArgumentException();
            }

            Color background;
            Color foreground;

//            // check if this cell represents the current DnD drop location
//            if (jlist.getDropLocation() != null
//                && !jlist.getDropLocation().isInsert()
//                && jlist.getDropLocation().getIndex() == index) {
//
//                background = Color.WHITE;
//                foreground = Color.BLUE;
//
//            // check if this cell is selected
//            } else 
            if (isSelected) {
                background = Color.WHITE;
                foreground = Color.RED;

            // unselected, and not the DnD drop location
            } else {
                background = Color.WHITE;
                foreground = Color.BLACK;
            }

            setBackground(background);
            setForeground(foreground);
            
            return this;
        }


    }
