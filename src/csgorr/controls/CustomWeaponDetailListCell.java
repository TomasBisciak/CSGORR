/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controls;

import csgorr.weapon.Weapon;
import javafx.scene.control.ListCell;

/**
 *
 * @author Kofola
 */
public class CustomWeaponDetailListCell<T extends Weapon> extends ListCell<T> {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            sb.append("").append(item.getName());

            setText(sb.toString());
            sb.delete(0, sb.length());
        }
    }
}
