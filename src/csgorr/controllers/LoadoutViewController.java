/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controllers;

import csgorr.CsgoRr;
import csgorr.controls.CustomNameOnlyListCell;
import csgorr.db.DbUtil;
import csgorr.model.Loadout;
import csgorr.utils.Info;
import csgorr.utils.Utils;
import csgorr.weapon.Weapon;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 *
 * @author Kofola
 */
public class LoadoutViewController implements Initializable {

    @FXML
    private HBox toggleButtonHolder;
    @FXML
    private ListView<Loadout> listViewLoadouts;
    @FXML
    private ListView<Weapon> listViewWeapons;
    @FXML
    private TextField txtFieldLoadoutName;
    @FXML
    private Button btnClearSlot;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClearAll;
    @FXML
    private Button btnDuplicate;

    private final ToggleGroup loadoutTogglesToggleGroup;

    private final IndexedToggleButton[] loadoutToggles = new IndexedToggleButton[10];

    private Loadout selectedLoadout;//use this to dont change original data before change is confirme

    private class IndexedToggleButton extends ToggleButton {

        private int index;

        public IndexedToggleButton(String text, int index) {
            super(text);
            this.index = index;
        }

        /**
         * @return the index
         */
        public int getIndex() {
            return index;
        }

        /**
         * @param index the index to set
         */
        public void setIndex(int index) {
            this.index = index;
        }
    }

    public LoadoutViewController() {
        loadoutTogglesToggleGroup = new ToggleGroup();
        selectedLoadout = new Loadout();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (int i = 0; i < 10; i++) {
            loadoutToggles[i] = new IndexedToggleButton("", i);
            loadoutToggles[i].setPrefWidth(89);
            loadoutToggles[i].setPrefHeight(74);
            loadoutToggles[i].setToggleGroup(loadoutTogglesToggleGroup);

            final int temp = i;

            loadoutToggles[i].selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    listViewWeapons.setDisable(false);
                    if (selectedLoadout.getWeaponIds().get(temp) != null) {//slot is null dont select anything
                        try {
                            listViewWeapons.getSelectionModel().select(CsgoRr.getModel().getWeaponById(selectedLoadout.getWeaponIds().get(temp)));
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Weapon thats in current loadout at LoadoutView is not found in weaponCache");
                        }

                    }

                } else {//deseledct
                    listViewWeapons.getSelectionModel().select(null);
                    listViewWeapons.setDisable(true);
                }
            });
            toggleButtonHolder.getChildren().add(loadoutToggles[i]);
        }

        //dont use toString(); in the ListView instead use getLoadoutName();
        listViewLoadouts.setCellFactory(lv -> new CustomNameOnlyListCell<>());

        listViewLoadouts.setItems(CsgoRr.getModel().getLoadoutCache());

        listViewLoadouts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Loadout>() {

            @Override
            public void changed(ObservableValue<? extends Loadout> observable, Loadout oldValue, Loadout newValue) {

                try {
                    System.out.println("SELECTED LOADOUT ID NAME:" + newValue.getName());
                    System.out.println("SELECTED LOADOUT ID VALUE:" + newValue.getId());

                    selectedLoadout = newValue.copy();//create copy of loadout that is in cache and is selected

                    //set name of new seleccted loadout
                    txtFieldLoadoutName.setText(selectedLoadout.getName());
                    //deselect any before selected toggles since it might have changed from already diffferent selected loadout
                    loadoutTogglesToggleGroup.selectToggle(null);

                    for (int i = 0; i < Loadout.LOADOUT_SIZE; i++) {
                //set loadout toggle text
                        //TODO FIX PROBLEM WITH NPE

                        // System.out.println("debug13:" + CsgoRr.getModel().getWeaponById(((Loadout) newValue).getWeaponIds().get(i)).toString());//npe
                        if (selectedLoadout.getWeaponIds().get(i) != null && CsgoRr.getModel().getWeaponById(selectedLoadout.getWeaponIds().get(i)) != null) {
                            //would throw NPE cause i would call (null).toString() if there is no second check after &&
                            loadoutToggles[i].setText(CsgoRr.getModel().getWeaponById((newValue).getWeaponIds().get(i)).toString());
                            selectedLoadout.getWeaponIds().set(i, (newValue).getWeaponIds().get(i));
                        } else {
                            loadoutToggles[i].setText(null);
                            selectedLoadout.getWeaponIds().set(i, null);
                        }
                    }
                } catch (Exception e) {// in cases when i deselect it with code for instance delete of loadout 
                    e.printStackTrace();
                }

            }
        });

        listViewWeapons.setDisable(true);
        listViewWeapons.setCellFactory(lv -> new CustomNameOnlyListCell<>());

        listViewWeapons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Weapon>() {

            @Override
            public void changed(ObservableValue<? extends Weapon> observable, Weapon oldValue, Weapon newValue) {
                try {

                    ((ToggleButton) loadoutTogglesToggleGroup.getSelectedToggle()).setText((newValue).getName());//change text on toggle
                    selectedLoadout.getWeaponIds().set(((IndexedToggleButton) loadoutTogglesToggleGroup.getSelectedToggle()).getIndex(), (newValue).getId());//sould work 

                } catch (NullPointerException e) {//thrown when newValue is null, not selected anything for isntance select loadout 2 select weapon 2 and the nselect loadout 1
                    // e.printStackTrace();
                }
            }
        });

        listViewWeapons.setItems(CsgoRr.getModel().getWeaponCache());
        //add listener on textField property to store name into currentLoadout.name
        txtFieldLoadoutName.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (selectedLoadout != null) {
                if (isValidTxtFieldLoadoutName()) {
                    selectedLoadout.setLoadoutName(newValue);
                    txtFieldLoadoutName.setStyle("");
                } else {
                    txtFieldLoadoutName.setStyle("-fx-border-color:red");
                }
            }

        });

    }

    private boolean isValidTxtFieldLoadoutName() {//litlle mindfuck here tbh
        for (Loadout loadout : CsgoRr.getModel().getLoadoutCache()) {
            if (loadout.getId() != selectedLoadout.getId()) {//its not same id as selected then check for fuplicate name
                //check for name duplication
                if (loadout.getName().equals(txtFieldLoadoutName.getText()) || txtFieldLoadoutName.getText().equals("")) {
                    return false;
                }
            }
            //check for string format validity
//            if (txtFieldLoadoutName.getText().equals("")) {//in any case check if its empty
//                return false;
//            }
        }
        return true;
    }

    private void setToggleGroupWeaponNames(String[] weaponNames) {
        for (int i = 0; i < 10; i++) {
            loadoutToggles[i].setText(weaponNames[i]);
        }
    }

    @FXML
    private void deleteOnAction() {
        if (listViewLoadouts.getSelectionModel().getSelectedItem() != null) {
            Loadout loadoutToDelete = listViewLoadouts.getSelectionModel().getSelectedItem();
            //clean up selected view
            loadoutTogglesToggleGroup.selectToggle(null);//deselect toggle
            listViewLoadouts.getSelectionModel().select(null);//deselect list loadout
            listViewWeapons.getSelectionModel().select(null);//not sure if needed when we set items to null

            setToggleGroupWeaponNames(new String[Loadout.LOADOUT_SIZE]);

            loadoutTogglesToggleGroup.selectToggle(null);

            if (CsgoRr.getModel().getActiveLoadout() == loadoutToDelete) {//if deleting selected loadout?
                CsgoRr.getModel().setActiveLoadout(null);
            }

            //delete in loadoutcache 
            CsgoRr.getModel().getLoadoutCache().remove(loadoutToDelete);
            listViewLoadouts.getSelectionModel().selectLast();
            CsgoRr.runLater(() -> {
                try {
                    DbUtil.removeLoadout(loadoutToDelete);
                } catch (SQLException ex) {//even if is thrown if thrown most likily means id is not there.So its not in database and its removed.
                    Logger.getLogger(LoadoutViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        }
    }

    @FXML
    private void clearAllOnAction() {
        selectedLoadout.setWeaponIds(new Long[10]);
        loadoutTogglesToggleGroup.selectToggle(null);
        setToggleGroupWeaponNames(new String[10]);
    }

    @FXML
    private void saveLoadoutOnAction() {

        if (listViewLoadouts.getSelectionModel().getSelectedItem() != null) {
            //current loadout holds changes made
            if (isValidTxtFieldLoadoutName()) {

                System.out.println("DEBUG gonna update loadout , id of loadout is :" + selectedLoadout.getId());
                try {
                    //update db
                    DbUtil.updateLoadout(selectedLoadout);
                    //update model
                    CsgoRr.getModel().getLoadoutById(selectedLoadout.getId()).setLoadoutName(selectedLoadout.getName());
                    CsgoRr.getModel().getLoadoutById(selectedLoadout.getId()).setWeaponIdsNoRef((Long[]) selectedLoadout.getWeaponIds().toArray());
                    //update list
                    Utils.triggerUpdate(listViewLoadouts, selectedLoadout.copy(), listViewLoadouts.getSelectionModel().getSelectedIndex());
                    //notify user
                    MainViewController.showPaneNotification("Loadout saved.", Info.Resource.IMG_ALERT_INFO);
                } catch (SQLException ex) {
                    Logger.getLogger(LoadoutViewController.class.getName()).log(Level.SEVERE, null, ex);
                    MainViewController.showPaneNotification("Failed to save loadout. -error code:" + ex.getErrorCode(), Info.Resource.IMG_ALERT_ERROR);
                }

            } else {
                MainViewController.showPaneNotification("Loadout name not valid", Info.Resource.IMG_ALERT_FAILURE);
            }

        } else {
            //TODO notify user that wtf he doesnt selected any loadout and didnt changed probably
            MainViewController.showPaneNotification("No loadout selected.", Info.Resource.IMG_ALERT_FAILURE);
        }

    }

    @FXML
    private void clearSlotOnAction() {
        if (loadoutTogglesToggleGroup.getSelectedToggle() != null) {
            ((ToggleButton) loadoutTogglesToggleGroup.getSelectedToggle()).setText(null);//change text on toggle
            selectedLoadout.getWeaponIds().set(((IndexedToggleButton) loadoutTogglesToggleGroup.getSelectedToggle()).getIndex(), null);
        } else {
            System.out.println("Nothing selecteds");
        }

    }

    private static int newDuplicateNameLoadoutIncrement = 1;

    @FXML
    private void newLoadoutOnAction() {
        try {

            Loadout loadoutToBeStored = new Loadout(new Long[10], "Loadout" + newDuplicateNameLoadoutIncrement);
            loadoutToBeStored.setId(DbUtil.storeLoadout(loadoutToBeStored));//store and set id.
            CsgoRr.getModel().getLoadoutCache().add(loadoutToBeStored);
            System.out.println("DEBUG Stored new loadout ");
            listViewLoadouts.getSelectionModel().select(loadoutToBeStored);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 23505) {//duplicate name
                newDuplicateNameLoadoutIncrement++;
                newLoadoutOnAction();
            }
            Logger.getLogger(LoadoutViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    @SuppressWarnings("UnnecessaryBoxing")
    private void duplicateOnAction() {
        if (listViewLoadouts.getSelectionModel().getSelectedItem() != null) {

            //copy item that is selected (selectedLoadout) and insert it into database
            Loadout loadoutToBeStored = selectedLoadout.copy();

            loadoutToBeStored.setLoadoutName(loadoutToBeStored.getName() + "+");

            CsgoRr.runLater(() -> {
                try {
                    loadoutToBeStored.setId(DbUtil.storeLoadout(loadoutToBeStored));//store and set id.id=;
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 23505) {//duplicate name//might have been duplicated before
                        Platform.runLater(() -> {
                            duplicateOnAction();
                        });

                    }
                    Logger.getLogger(LoadoutViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            CsgoRr.getModel().getLoadoutCache().add(loadoutToBeStored);
            System.out.println("DEBUG Stored new DUPLICATED loadout ");
            listViewLoadouts.getSelectionModel().select(loadoutToBeStored);
            //as well into list and cache, then select it as user would normally do by default

        } else {
            MainViewController.showPaneNotification("Please select loadout from \"Saved Loadouts\".", Info.Resource.IMG_ALERT_FAILURE);
        }
    }

}
