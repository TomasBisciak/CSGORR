/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controls;

import csgorr.weapon.RecoilPattern;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

/**
 *
 * @author Kofola
 */
public class RecoilDirectionComboBoxListCell extends ListCell<Integer> {
    
    @Override
    public void updateItem(Integer item,
            boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(RecoilPattern.directionToString(item));
        } else {
            setText(null);
        }
    }
}
