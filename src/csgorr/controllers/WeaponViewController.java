/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controllers;

import csgorr.CsgoRr;
import csgorr.controls.CustomWeaponDetailListCell;
import csgorr.controls.CustomWeaponRecoilRowListCell;
import csgorr.controls.RecoilDirectionComboBoxListCell;
import csgorr.db.DbUtil;
import csgorr.utils.Info;
import csgorr.utils.Utils;
import csgorr.weapon.RecoilPattern;
import csgorr.weapon.Weapon;
import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author Kofola
 */
public class WeaponViewController implements Initializable {

    @FXML
    private Button btnSaveWeapon;
    @FXML
    private Button deleteOnAction;
    @FXML
    private Button btnDuplicate;
    @FXML
    private TextField txtFieldWeaponName;
    @FXML
    private ListView<Weapon> listViewWeapons;
    @FXML
    private ListView<int[]> listViewRecoil;

    private Weapon selectedWeaponCopy;//use this to dont change original data before change is confirmed

    @FXML
    private Button btnOpenPaint;
    @FXML
    private Button btnTestPattern;
    @FXML
    private Button btnAddRow;
    @FXML
    private TextField txtFieldPerStep;

    @FXML
    private TextField txtFieldSF;
    @FXML
    private TextField txtFieldDV;
    @FXML
    private TextField txtFieldDP;

    @FXML
    private ComboBox combobxDir1;
    @FXML
    private ComboBox combobxDir2;

    @FXML
    private TextField txtFieldNumOfSteps;

    private ObservableList<int[]> recoilRowList;

    public WeaponViewController() {
        try {
            selectedWeaponCopy = Weapon.createWeapon(null, null);
        } catch (AWTException ex) {
            Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        recoilRowList = FXCollections.observableList(new ArrayList<int[]>());
    }

    private boolean isValidWeaponName(String weaponName) {
        //check if its not duplicate / empty
        for (Weapon weapon : CsgoRr.getModel().getWeaponCache()) {
            if (weapon.getId() != selectedWeaponCopy.getId()) {
                if (weapon.getName().equals(weaponName) || weaponName.equals("")) {
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewWeapons.setCellFactory(lv -> new CustomWeaponDetailListCell<>());
        listViewWeapons.setItems(CsgoRr.getModel().getWeaponCache());

        txtFieldWeaponName.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (selectedWeaponCopy != null) {
                if (isValidWeaponName(newValue)) {
                    selectedWeaponCopy.setName(newValue);
                    txtFieldWeaponName.setStyle("");
                } else {
                    txtFieldWeaponName.setStyle("-fx-border-color:red");
                }

            }

        });

        listViewWeapons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Weapon>() {

            @Override
            public void changed(ObservableValue<? extends Weapon> observable, Weapon oldValue, Weapon newValue) {

                try {
                    System.out.println("SELECTED Weapon ID NAME:" + newValue.getName());
                    System.out.println("SELECTED Weapon ID VALUE:" + newValue.getId());

                    selectedWeaponCopy = newValue.copy();//create copy of loadout that is in cache and is selected

                    recoilRowList.clear();

                    recoilRowList.addAll(getRecoilData(selectedWeaponCopy.getRecoilPattern()));
                    //debug purpose
                    System.out.println("Contents of recoilRowList:");
                    for (int[] recoilRowList1 : recoilRowList) {
                        System.out.println("DEBUG:" + recoilRowList1[0] + " " + recoilRowList1[1] + " " + recoilRowList1[2] + " ");
                    }
                    System.out.println("---------------");

                    txtFieldSF.setText(String.valueOf(newValue.getRecoilPattern().getSmoothnessFactor()));
                    txtFieldDV.setText(String.valueOf(newValue.getRecoilPattern().getDeviationValue()));
                    txtFieldDP.setText(String.valueOf(newValue.getRecoilPattern().getDeviationPercentage()));
                    //set name of new seleccted loadout
                    txtFieldWeaponName.setText(newValue.getName());

                } catch (Exception e) {// in cases when i deselect it with code for instance delete of loadout 
                    e.printStackTrace();
                }

            }
        });

        listViewRecoil.setCellFactory(lv -> new CustomWeaponRecoilRowListCell());
        listViewRecoil.setItems(recoilRowList);

        listViewRecoil.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends int[]> observable, int[] oldValue, int[] newValue) -> {
            if (newValue != null) {
                txtFieldNumOfSteps.setText(String.valueOf(newValue[0]));
                txtFieldPerStep.setText(String.valueOf(newValue[1]));
                Integer[] directions = RecoilPattern.splitBitDataIntoDirections(newValue[2]);
                if (directions.length > 1) {
                    combobxDir1.getSelectionModel().select(directions[0]);
                    combobxDir2.getSelectionModel().select(directions[1]);
                } else {
                    combobxDir1.getSelectionModel().select(directions[0]);
                    combobxDir2.getSelectionModel().select(null);
                }

            }
        });

        combobxDir1.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {

            @Override
            public ListCell<Integer> call(ListView<Integer> param) {
                return new RecoilDirectionComboBoxListCell();
            }

        });

        combobxDir1.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer obj) {
                if (obj == null) {
                    return "";
                } else {
                    return RecoilPattern.directionToString(obj);
                }
            }

            @Override
            public Integer fromString(String s) {
                return 8;
            }
        });

        combobxDir1.setItems(FXCollections.observableArrayList(
                RecoilPattern.NONE, RecoilPattern.UP, RecoilPattern.DOWN, RecoilPattern.LEFT, RecoilPattern.RIGHT));

        combobxDir1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //change value right away , same as on those other data fields
                int[] directions = getDirectionsFromComboBoxes();

                recoilRowList.set(listViewRecoil.getSelectionModel().getSelectedIndex(), new int[]{
                    Integer.valueOf(txtFieldNumOfSteps.getText()), Integer.valueOf(txtFieldPerStep.getText()), directions[0] | directions[1]});

            }
        });

        combobxDir2.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {

            @Override
            public ListCell<Integer> call(ListView<Integer> param) {
                return new RecoilDirectionComboBoxListCell();
            }

        });

        combobxDir2.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer obj) {
                if (obj == null) {
                    return "";
                } else {
                    return RecoilPattern.directionToString(obj);
                }
            }

            @Override
            public Integer fromString(String s) {
                return 8;
            }
        });

        combobxDir2.setItems(FXCollections.observableArrayList(
                RecoilPattern.NONE, RecoilPattern.UP, RecoilPattern.DOWN, RecoilPattern.LEFT, RecoilPattern.RIGHT));

        combobxDir2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //change value right away , same as on those other data fields
                int[] directions = getDirectionsFromComboBoxes();

                recoilRowList.set(listViewRecoil.getSelectionModel().getSelectedIndex(), new int[]{
                    Integer.valueOf(txtFieldNumOfSteps.getText()), Integer.valueOf(txtFieldPerStep.getText()), directions[0] | directions[1]});

            }
        });

        //txtFieldSF.setTooltip(new Tooltip("Integer value from 1 to 100"));
        //text field data validity checks
        txtFieldSF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (selectedWeaponCopy != null) {

                    if (isValidSF(newValue)) {
                        selectedWeaponCopy.getRecoilPattern().setSmoothnessFactor(Integer.valueOf(txtFieldSF.getText()));
                    }

                }
            }
        });

        txtFieldDV.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (selectedWeaponCopy != null) {

                    if (isValidDV(newValue)) {
                        selectedWeaponCopy.getRecoilPattern().setDeviationValue(Integer.valueOf(txtFieldDV.getText()));
                    }

                }
            }
        });

        txtFieldDP.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (selectedWeaponCopy != null) {

                    if (isValidDP(newValue)) {
                        selectedWeaponCopy.getRecoilPattern().setDeviationPercentage(Integer.valueOf(txtFieldDP.getText()));
                    }

                }
            }
        });

        txtFieldPerStep.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (selectedWeaponCopy != null) {

                    if (isValidPS(newValue) && selectedWeaponCopy != null) {
                        //CHANGE VALUE RIGHT AWAY --UPDATED VERSION
                        int[] directions = getDirectionsFromComboBoxes();

                        recoilRowList.set(listViewRecoil.getSelectionModel().getSelectedIndex(), new int[]{
                            Integer.valueOf(txtFieldNumOfSteps.getText()), Integer.valueOf(txtFieldPerStep.getText()), directions[0] | directions[1]});

                    }

                }
            }
        });

        txtFieldNumOfSteps.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (selectedWeaponCopy != null) {

                    if (isValidNOS(newValue)) {
                        //CHANGE VALUE RIGHT AWAY --UPDATED VERSION
                        int[] directions = getDirectionsFromComboBoxes();

                        //throws NFE when i save for instance nothing is selected at that point in a recoil list , thats when we get it.//nfe thrown from txtfieldperstep
                        recoilRowList.set(listViewRecoil.getSelectionModel().getSelectedIndex(), new int[]{
                            Integer.valueOf(txtFieldNumOfSteps.getText()), Integer.valueOf(txtFieldPerStep.getText()), directions[0] | directions[1]});

                    }

                }
            }
        });

        /*
                
         int[] directions = getDirectionsFromComboBoxes();

         recoilRowList.set(listViewRecoil.getSelectionModel().getSelectedIndex(), new int[]{
         Integer.valueOf(txtFieldNumOfSteps.getText()), Integer.valueOf(txtFieldPerStep.getText()), directions[0] | directions[1]});
                
         */
    }

    private boolean isWeaponFieldsValid() {
        return txtFieldSFValidity && txtFieldDVValidity && txtFieldDPValidity;
    }

    private boolean txtFieldSFValidity;

    public boolean isValidSF(String sf) {

        try {
            int sfint = Integer.valueOf(sf);
            if (sfint < 0 || sfint > 100) {//TODO check  for NPE in WeaponRobot
                throw new NumberFormatException("negative value not alloved,OR HIGHER THEN 100");
            } else {
                txtFieldSF.setStyle("");
                txtFieldSFValidity = true;
                return true;
            }
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            txtFieldSFValidity = false;
            txtFieldSF.setStyle("-fx-border-color:red");
            return false;
        }

    }

    private boolean txtFieldDVValidity;

    public boolean isValidDV(String dv) {

        try {
            int sfint = Integer.valueOf(dv);
            if (sfint < 0) {//TODO check  for NPE in WeaponRobot
                throw new NumberFormatException("negative value not alloved.");
            } else {
                txtFieldDV.setStyle("");
                txtFieldDVValidity = true;
                return true;
            }
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            txtFieldDVValidity = false;
            txtFieldDV.setStyle("-fx-border-color:red");
            return false;
        }

    }

    private boolean txtFieldDPValidity;

    public boolean isValidDP(String dp) {

        try {
            int sfint = Integer.valueOf(dp);
            if (sfint < 0 || sfint > 100) {//TODO check  for NPE in WeaponRobot
                throw new NumberFormatException("negative value not alloved,or higher then 100");
            } else {
                txtFieldDP.setStyle("");
                txtFieldDPValidity = true;
                return true;
            }
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            txtFieldDPValidity = false;
            txtFieldDP.setStyle("-fx-border-color:red");
            return false;
        }

    }

    //txtFieldPerStep
    //txtFieldNumOfSteps
    private boolean txtFieldPSValidity;

    public boolean isValidPS(String ps) {

        try {
            int sfint = Integer.valueOf(ps);
            if (sfint < 0) {//TODO check  for NPE in WeaponRobot
                throw new NumberFormatException("negative value not alloved.");
            } else {
                txtFieldPerStep.setStyle("");
                txtFieldPSValidity = true;
                return true;
            }
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            txtFieldPSValidity = false;
            txtFieldPerStep.setStyle("-fx-border-color:red");
            return false;
        }

    }

    private boolean txtFieldNOSValidity;

    public boolean isValidNOS(String nos) {

        try {
            int sfint = Integer.valueOf(nos);
            if (sfint < 0) {//TODO check  for NPE in WeaponRobot
                throw new NumberFormatException("negative value not alloved.");
            } else {
                txtFieldNumOfSteps.setStyle("");
                txtFieldNOSValidity = true;
                return true;
            }
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            txtFieldNOSValidity = false;
            txtFieldNumOfSteps.setStyle("-fx-border-color:red");
            return false;
        }

    }

    private ArrayList<int[]> getRecoilData(RecoilPattern rp) {
        ArrayList<int[]> list = new ArrayList<>();
        for (int i = 0; i < rp.getPattern().length; i++) {
            list.add(rp.getPattern()[i]);
        }
        return list;
    }

    @FXML
    private void duplicateOnAction() {
        if (listViewWeapons.getSelectionModel().getSelectedItem() != null) {

            Weapon weaponToBeStored = selectedWeaponCopy.copy();
            weaponToBeStored.setName(weaponToBeStored.getName() + "+");

            try {
                weaponToBeStored.setId(DbUtil.storeWeapon(weaponToBeStored));
                Platform.runLater(() -> {
                    CsgoRr.getModel().getWeaponCache().add(weaponToBeStored);
                    System.out.println("DEBUG Stored new DUPLICATED loadout ");
                    listViewWeapons.getSelectionModel().select(weaponToBeStored);
                });

            } catch (SQLException ex) {

                if (ex.getErrorCode() == 23505) {//duplicate name//might have been duplicated before
                    Platform.runLater(() -> {
                        duplicateOnAction();
                    });
                }
                MainViewController.showPaneNotification("Failed to duplicate weapon.", Info.Resource.IMG_ALERT_ERROR);
                Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            MainViewController.showPaneNotification("Please select weapon from \"Saved weapons\".", Info.Resource.IMG_ALERT_FAILURE);
        }
    }

    @FXML
    private void deleteOnAction() {
        if (listViewWeapons.getSelectionModel().getSelectedItem() != null) {

            Weapon weaponToDelete = listViewWeapons.getSelectionModel().getSelectedItem();
            CsgoRr.runLater(() -> {
                try {
                    DbUtil.removeWeapon(weaponToDelete);
                    //clean up selected view
                    Platform.runLater(() -> {
                        listViewWeapons.getSelectionModel().select(null);//deselect list wepaons
                        listViewRecoil.getSelectionModel().select(null);
                        recoilRowList.clear();
                        clearWeaponSettingsControls();
                        //clear lower menu/text fields/combo boxes/smoothness settings

                        //wepaon that is active
                        if (CsgoRr.getModel().getActiveWeapon() == weaponToDelete) {//if deleting selected loadout?
                            CsgoRr.getModel().setActiveWeapon(null);
                        }
                        //delete in weaponCache
                        CsgoRr.getModel().getWeaponCache().remove(weaponToDelete);
                    });

                } catch (SQLException ex) {//even if is thrown if thrown most likily means id is not there.So its not in database and its removed.
                    MainViewController.showPaneNotification("Failed to delete data from database.", Info.Resource.IMG_ALERT_ERROR);
                    Logger.getLogger(LoadoutViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        }
    }

    private void clearWeaponSettingsControls() {
        txtFieldPerStep.setText(null);
        combobxDir1.getSelectionModel().select(null);
        combobxDir2.getSelectionModel().select(null);
        txtFieldNumOfSteps.setText(null);
        txtFieldSF.setText(null);
        txtFieldDV.setText(null);
        txtFieldDP.setText(null);
    }

    private boolean isRowValidFields() {
        return txtFieldNOSValidity && txtFieldPSValidity;
    }

    @FXML
    private void addRowOnAction() {

        if (isRowValidFields()) {
            int[] directions = getDirectionsFromComboBoxes();

            recoilRowList.add(
                    new int[]{
                        Integer.valueOf(txtFieldNumOfSteps.getText()), Integer.valueOf(txtFieldPerStep.getText()),
                        directions[0] | directions[1]
                    });
            listViewRecoil.getSelectionModel().selectLast();

        } else {
            MainViewController.showPaneNotification("Input not valid..", Info.Resource.IMG_ALERT_FAILURE);
        }

    }

    @FXML
    private Button btnRemoveRow;

    @FXML
    private void removeRowOnAction() {

        if (listViewRecoil.getSelectionModel().getSelectedItem() != null) {
            recoilRowList.remove(listViewRecoil.getSelectionModel().getSelectedIndex());
        } else {
            MainViewController.showPaneNotification("Row not selected.", Info.Resource.IMG_ALERT_FAILURE);
        }

    }

    private int[] getDirectionsFromComboBoxes() {

        int[] directions = new int[]{RecoilPattern.NONE, RecoilPattern.NONE};
        try {
            directions[0] = ((int) combobxDir1.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }
        try {
            directions[1] = ((int) combobxDir2.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }

        return directions;
    }

    @FXML
    private void saveOnAction() {
        //in case this weapon is active , deactivate it- save- activate after save again.Or i dont know maybe.

        if (listViewWeapons.getSelectionModel().getSelectedItem() != null) {
            //current weapon is selected wepaon , recoil is hold in recoillist
            //changes have to be made to model
            if (isWeaponFieldsValid()) {
                //save selected weapon , create new recoilPattern from data selected
                // selectedWeapon.setRecoilPattern());

                try {
                    //add unoptimized part
                    selectedWeaponCopy.getRecoilPattern().setPattern(createMultiDimArrayFromRecoilRowList());
                    //save into db
                    // ITS DONE ON JFXAT- Exception in this case to keep code readable and its low weight task to be done for db, basic insert of one row
                    DbUtil.updateWeapon(selectedWeaponCopy);//on JFXAT

                    //CHANGE CACHED VERSION.
                    Weapon weaponInCache = CsgoRr.getModel().getWeaponById(selectedWeaponCopy.getId());
                    weaponInCache.setName(txtFieldWeaponName.getText());
                    weaponInCache.setRecoilPattern(selectedWeaponCopy.getRecoilPattern().copy()
                    );
                    //trigger update on list
                    Utils.triggerUpdate(listViewWeapons, selectedWeaponCopy.copy(), listViewWeapons.getSelectionModel().getSelectedIndex());

                    //dont need this anymore
                    //recoilRowList.clear();
                    //recoilRowList.addAll(getRecoilData(selectedWeaponCopy.getRecoilPattern()));//no reference just data
                    MainViewController.showPaneNotification("Saved.", Info.Resource.IMG_ALERT_INFO);
                    //CLEAN SELECTED WEAPON ?
                } catch (SQLException ex) {
                    MainViewController.showPaneNotification("Failed to insert data into database.", Info.Resource.IMG_ALERT_ERROR);
                    Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

                /*
                 Weapon weaponInCache = CsgoRr.getModel().getWeaponById(selectedWeaponCopy.getId());

                 weaponInCache.setName(txtFieldWeaponName.getText());
                 weaponInCache.setRecoilPattern(new RecoilPattern(createMultiDimArrayFromRecoilRowList(),
                 Integer.valueOf(txtFieldSF.getText()), Integer.valueOf(txtFieldDV.getText()), Integer.valueOf(txtFieldDP.getText())));

                 System.out.println("DEBUG:Selected weapon id:" + selectedWeaponCopy.getId() + ", selected weapon name:" + selectedWeaponCopy.getName());

                 //and applied to database
                 System.out.println("DEBUG gonna update weapon , id of weapon is :" + selectedWeaponCopy.getId());
                 try {
                 DbUtil.updateWeapon(weaponInCache);
                 //CLEAN SELECTED WEAPON ?
                 } catch (SQLException ex) {
                 Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
                 }

                 Utils.triggerUpdate(listViewWeapons, weaponInCache.copy(), listViewWeapons.getSelectionModel().getSelectedIndex());
                 recoilRowList.clear();
                 recoilRowList.addAll(getRecoilData(weaponInCache.getRecoilPattern()));
                
                 */
            } else {
                MainViewController.showPaneNotification("Input not valid.", Info.Resource.IMG_ALERT_FAILURE);
            }

        } else {
            //TODO notify user that wtf he doesnt selected any loadout and didnt changed probably
            MainViewController.showPaneNotification("Please select weapon.", Info.Resource.IMG_ALERT_FAILURE);
        }

    }

    private int[][] createMultiDimArrayFromRecoilRowList() {
        int length = recoilRowList.size();
        int[][] data;
        if (length != 0) {
            data = new int[recoilRowList.size()][recoilRowList.get(0).length];

            for (int i = 0; i < recoilRowList.size(); i++) {
                //still retains reference/caution
                // data[i] = recoilRowList.get(i);
                System.arraycopy(recoilRowList.get(i), 0, data[i], 0, recoilRowList.get(i).length);//no reference
            }

        } else {
            data = new int[0][0];
        }

        return data;
    }

    private static int newWeaponNameIncrement = 1;

    @FXML
    private void newWeaponOnAction() {
        try {
            System.out.println("DEBUG WEAPON NAME TRYING TO BE CREATED IS :" + "newWeapon" + newWeaponNameIncrement);
            Weapon newWeapon = Weapon.createWeapon("newWeapon" + newWeaponNameIncrement,
                    new RecoilPattern());
            newWeapon.setId(DbUtil.storeWeapon(newWeapon));

            CsgoRr.getModel().getWeaponCache().add(newWeapon);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 23505) {//duplicate name
                System.out.println("DEBUG :Duplicate name on add new weapon");
                newWeaponNameIncrement++;
                newWeaponOnAction();
            }

            Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnOpenPaintOnAction() {
        try {
            Runtime.getRuntime().exec("mspaint.exe");
        } catch (IOException ex) {
            Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void testPatternOnAction() {

        //test weapon form whats currently in recoilList,create new one from data
        if (isWeaponFieldsValid()) {
            try {
                CsgoRr.getModel().setActiveWeapon(Weapon.createWeapon("wt-" + selectedWeaponCopy.getName(),
                        new RecoilPattern(createMultiDimArrayFromRecoilRowList(), selectedWeaponCopy.getRecoilPattern().getSmoothnessFactor(),
                                selectedWeaponCopy.getRecoilPattern().getDeviationValue(), selectedWeaponCopy.getRecoilPattern().getDeviationPercentage())));
            } catch (AWTException ex) {
                MainViewController.showPaneNotification("Input not valid.", Info.Resource.IMG_ALERT_FAILURE);
                Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
    @FXML
    private Button exportBtn;
    @FXML
    private Button importBtn;

    @FXML
    private void exportOnAction() {
        //selected weapon will be exported

        if (listViewWeapons.getSelectionModel().getSelectedItem() == null) {
            MainViewController.showPaneNotification("No weapon selected", Info.Resource.IMG_ALERT_FAILURE);
            return;
        }

        File newFile = new File(Info.DIR_WEP_EX + listViewWeapons.getSelectionModel().getSelectedItem().getName() + ".json");//check validity of file name
        byte[] content = CsgoRr.objectToJsonString(listViewWeapons.getSelectionModel().getSelectedItem().getRecoilPattern()).getBytes();

        try {
            //check filename validity
            if (!newFile.exists()) {

                if (newFile.createNewFile()) {

                    FileOutputStream oFile = new FileOutputStream(newFile, false);
                    oFile.write(content);

                    MainViewController.showPaneNotification("Written to file.", Info.Resource.IMG_ALERT_INFO);
                } else {
                    MainViewController.showPaneNotification("Failed to create file (Weapon name has to be valid fileName)", Info.Resource.IMG_ALERT_ERROR);
                }
            } else {
                //ASK IF OVERWRITE FILE WITH THIS DATA
                // MainViewController.showPaneNotification("File already exists.", Info.Resource.IMG_ALERT_FAILURE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("File already exists");
                alert.setContentText("Do you wish to overwrite data?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        //newFile.createNewFile(); // if file already exists will do nothing 
                        FileOutputStream oFile = new FileOutputStream(newFile, false);
                        oFile.write(content);
                        MainViewController.showPaneNotification("Written to file.", Info.Resource.IMG_ALERT_INFO);
                    } catch (IOException ex) {
                        MainViewController.showPaneNotification("Failed to create file.", Info.Resource.IMG_ALERT_ERROR);
                        Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        } catch (IOException ex) {
            MainViewController.showPaneNotification("Failed to create file. IO ex", Info.Resource.IMG_ALERT_ERROR);
            Logger.getLogger(WeaponViewController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    private void importOnAction() {

        FileChooser fc = new FileChooser();

        fc.setInitialDirectory(new File(Info.DIR_WEP_EX));

        List<File> files = fc.showOpenMultipleDialog(CsgoRr.primStage);

        Charset charset = Charset.forName("US-ASCII");
        for (File file : files) {
            StringBuilder sbContent = new StringBuilder();
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    sbContent.append(line);
                }
                try {
                    Weapon newWep = Weapon.createWeapon(file.getName().split("\\.")[0], CsgoRr.jsonStringToObject(sbContent.toString(), RecoilPattern.class));
                    //put into database and cache
                    CsgoRr.getModel().getWeaponCache().add(newWep);
                    DbUtil.storeWeapon(newWep);

                } catch (Exception e) {
                    MainViewController.showPaneNotification("Failed to import weapon , file:" + file.getName(), Info.Resource.IMG_ALERT_ERROR);
                    e.printStackTrace();
                }
            } catch (IOException x) {
                MainViewController.showPaneNotification("Failed to read file , file:" + file.getName(), Info.Resource.IMG_ALERT_ERROR);
                x.printStackTrace();
            }
        }

    }

}
