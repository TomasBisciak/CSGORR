/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controllers;

import csgorr.CsgoRr;
import csgorr.Tray;
import csgorr.controls.CustomNameOnlyListCell;
import csgorr.model.Loadout;
import csgorr.utils.Info;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.NotificationPane;
import org.jnativehook.NativeHookException;

/**
 *
 * @author Kofola
 */
public class MainViewController implements Initializable {

    @FXML
    private ScrollPane dynamicNode;
    @FXML
    private BorderPane contentPane;
    @FXML
    private ToggleButton incognitoToggleButton;
    @FXML
    private ToggleButton prefBtn;
    @FXML
    private ToggleButton loadoutBtn;
    @FXML
    private MenuItem miAbout;
    @FXML
    private MenuItem miExit;
    @FXML
    private ToggleButton toggleButtonActiveWeapon;
    @FXML
    private ToggleButton productPageToggleButton;
    @FXML
    private ToggleButton activateRrToggleButton;
    @FXML
    private ListView listViewLoadouts;
    @FXML
    private ToggleButton btnWeaponManager;
    @FXML
    private ToggleButton changelogPageToggleButton;
    @FXML
    private ToggleButton databaseBtn;

    private static NotificationPane notifPane;

    private final ToggleGroup toggleGroup;

    private String currentlyInDynamicPane = "none";

    @FXML
    private ToggleButton btnVersion;

    public MainViewController() {
        //
        toggleGroup = new ToggleGroup();
        System.out.println("debug: mainviewcontroller  constructor is fxat?:" + Platform.isFxApplicationThread());

    }

    public ToggleButton getActivateRrMenuBtn() {
        return activateRrToggleButton;
    }

    public void setActiveWeaponName(String weaponName) {
        toggleButtonActiveWeapon.setText("Active weapon:" + weaponName);
    }

    @FXML
    private void removeWeaponOnAction() {
        CsgoRr.getModel().setActiveWeapon(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("debug:initialized");

        notifPane = new NotificationPane(dynamicNode);
        notifPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
        notifPane.setShowFromTop(true);

        notifPane.setOnMouseClicked((MouseEvent event) -> {
            notifPane.hide();
            //animateMove(false, 0);
        });

        contentPane.setCenter(notifPane);

        prefBtn.setToggleGroup(toggleGroup);
        loadoutBtn.setToggleGroup(toggleGroup);
        btnWeaponManager.setToggleGroup(toggleGroup);
        productPageToggleButton.setToggleGroup(toggleGroup);
        databaseBtn.setToggleGroup(toggleGroup);

        productPageToggleButton.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                productPageToggleButton.setEffect(new InnerShadow(5.0, Color.BLACK));
            } else {
                productPageToggleButton.setEffect(null);
            }
        });

        changelogPageToggleButton.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                changelogPageToggleButton.setEffect(new InnerShadow(5.0, Color.BLACK));
            } else {
                changelogPageToggleButton.setEffect(null);
            }
        });

        System.out.println("debug: mainviewcontroller  initialize is fxat?:" + Platform.isFxApplicationThread());

        incognitoToggleButton.setStyle(
                "-fx-background-radius:0;"
                + "-fx-background-image:url(\"" + Info.Resource.IMG_INCOGNITO_MODE + "\");"
                + "-fx-background-repeat:no-repeat;"
                + "-fx-background-position:10 center;"
                + "-fx-background-size:40;");

        incognitoToggleButton.selectedProperty().addListener((v, oldVal, newVal) -> {
            if ((boolean) newVal) {
                //TODO  change it to reasourcen ot URL resource
                incognitoToggleButton.setStyle(
                        "-fx-background-radius:0;"
                        + "-fx-background-image:url(\"" + Info.Resource.IMG_INCOGNITO_MODE + "\");"
                        + "-fx-background-repeat:no-repeat;"
                        + "-fx-background-position:10 center;"
                        + "-fx-background-size:40;"
                        + "-fx-background-color:#1abc9c;");
            } else {
                incognitoToggleButton.setStyle(
                        "-fx-background-radius:0;"
                        + "-fx-background-image:url(\"" + Info.Resource.IMG_INCOGNITO_MODE + "\");"
                        + "-fx-background-repeat:no-repeat;"
                        + "-fx-background-position:10 center;"
                        + "-fx-background-size:40;");
            }
        });
        incognitoToggleButton.setSelected(false);
        activateRrToggleButton.selectedProperty().addListener((v, oldVal, newVal) -> {

            if (activateRrToggleButton.isSelected()) {
                activateRrToggleButton.setStyle("-fx-background-radius:0;-fx-background-color:#1abc9c;");
                activateRrToggleButton.setText("Activated");
                activateRrToggleButton.setTextFill(Paint.valueOf("white"));

            } else {
                activateRrToggleButton.setStyle("-fx-background-radius:0;");
                activateRrToggleButton.setText("Activate");
                activateRrToggleButton.setTextFill(Paint.valueOf("black"));

            }
        });
        activateRrToggleButton.selectedProperty().setValue(CsgoRr.getModel().isRrEnabled());

        listViewLoadouts.setCellFactory(lv -> new CustomNameOnlyListCell<>());
        listViewLoadouts.setItems(CsgoRr.getModel().getLoadoutCache());
        listViewLoadouts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                CsgoRr.getModel().setActiveLoadout((Loadout) newValue);
            }
        });

        clearViewInit();
        clearView();

        btnVersion.setTooltip(new Tooltip("Check product page of vendor you bought product from."));
        try {
            if (isNewVersionAvailable()) {
                System.out.println("DEBUG:Application is out of date.");
                btnVersion.setVisible(true);
                btnVersion.setText("Application is out of date.");
                btnVersion.setStyle("-fx-background-radius:0;-fx-background-color:#f39c12");
            } else {
                System.out.println("DEBUG:Application is up to date");
                btnVersion.setVisible(false);
               // btnVersion.setText("Application is up to date.");
                // btnVersion.setStyle("-fx-background-radius:0;-fx-background-color:#16a085");
            }

        } catch (IOException ex) {
            System.out.println("DEBUG:Failed to check version");
            btnVersion.setVisible(true);
            btnVersion.setText("Failed to check version");
            btnVersion.setStyle("-fx-background-radius:0;-fx-background-color:red");
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //
    private boolean isNewVersionAvailable() throws IOException {
        URL oracle = new URL("https://raw.githubusercontent.com/TomasBisciak/VersionCheck/master/README.md");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("csgorr")) {
                System.out.println("Application version:" + Info.APP_VERSION);
                double lv = Double.valueOf(inputLine.split(",")[1]);
                System.out.println("Latest version version:" + lv);
                return lv > Info.APP_VERSION;
            }
            System.out.println(inputLine);
        }
        in.close();
        return false;
    }

    public void setActiveLoadoutInMainView(Loadout loadout) {
        //  if(loadout!=null){
        listViewLoadouts.getSelectionModel().select(loadout);
        // }else{
        //       listViewLoadouts.getSelectionModel().select(null);
        // }

    }

    @FXML
    private void enableDisableRrOnEvent() {
        CsgoRr.getModel().setRrEnabled(!CsgoRr.getModel().isRrEnabled());
    }

    //CLEAR VIEW 
    Label cv_csgolbl;
    Label cv_csgolbl2;
    VBox cv_vbox;

    private void clearViewInit() {

        cv_csgolbl = new Label("CSMP");
        cv_csgolbl2 = new Label("Macro platform by Tomas Bisciak");

        cv_vbox = new VBox();

        cv_vbox.setAlignment(Pos.CENTER);
        cv_csgolbl.setFont(Font.font("System", FontWeight.BOLD, 96));
        cv_csgolbl2.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 18));

        cv_vbox.getChildren().add(cv_csgolbl);
        cv_vbox.getChildren().add(cv_csgolbl2);

    }

    private void clearView() {
        dynamicNode.setContent(cv_vbox);//placeholder
        currentlyInDynamicPane = "none";
    }

    @FXML
    private void setViewPreferences() {
        if (!currentlyInDynamicPane.equals(Info.Resource.FXML_FILE_PREFERENCES)) {
            System.out.println("Here");
            setView(Info.Resource.FXML_FILE_PREFERENCES);
        } else {
            clearView();
        }

    }

    @FXML
    private void setViewProductPage() {
        if (!currentlyInDynamicPane.equals(Info.Resource.FXML_FILE_PRODUCT_PAGE)) {
            setView(Info.Resource.FXML_FILE_PRODUCT_PAGE);
        } else {
            clearView();
        }
    }

    @FXML
    private void setViewLoadout() {
        if (!currentlyInDynamicPane.equals(Info.Resource.FXML_FILE_LOADOUT)) {
            setView(Info.Resource.FXML_FILE_LOADOUT);
        } else {
            clearView();
        }
    }

    @FXML
    private void setViewWeapons() {
        if (!currentlyInDynamicPane.equals(Info.Resource.FXML_FILE_WEAPON)) {
            setView(Info.Resource.FXML_FILE_WEAPON);
        } else {
            clearView();
        }
    }

    @FXML
    private void setViewDatabase() {
        if (!currentlyInDynamicPane.equals(Info.Resource.FXML_FILE_DATABASE)) {
            setView(Info.Resource.FXML_FILE_DATABASE);
        } else {
            clearView();
        }
    }

    @FXML
    private void incognitoOnEventAction() {
        CsgoRr.getModel().setIncognitoMode(!CsgoRr.getModel().isIncognitoMode());
    }

    public void setView(String fxmlPath) {
        if (currentlyInDynamicPane.equals("none")) {//TODO  change to be default at some welcome screen, DO NOT CHECK FOR NULL THEN , NOT NEEDED OR KEEP THSI WAY . NOT SURE
            if (!(currentlyInDynamicPane.equals(Info.Resource.FXML_FILE_PREFERENCES) && PreferencesViewController.isDataChanged())) {
                dynamicNode.setContent(getView(fxmlPath));
                currentlyInDynamicPane = fxmlPath;
            } else {
                String[] options = {"Leave",
                    "Stay"};

                ChoiceDialog<String> cd = new ChoiceDialog<>(options[1], options);

                cd.setTitle("Changed data not saved.");
                Platform.runLater(() -> {
                    Optional<String> response = cd.showAndWait();
                    if (response.get().equals(options[0])) {
                        dynamicNode.setContent(getView(fxmlPath));
                        currentlyInDynamicPane = fxmlPath;
                    } else {
                        prefBtn.setSelected(true);
                    }
                });

            }

        } else {
            dynamicNode.setContent(getView(fxmlPath));
            currentlyInDynamicPane = fxmlPath;
        }

    }

    public Node getView(String fxmlPath) {
        try {
            return new FXMLLoader(getClass().getResource(fxmlPath)).load();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static final long DEFAULT_MAIN_NOTIF_TIMEOUT = 4000l;
    private static boolean isShownNotif;
    private static volatile boolean isNotifPending;

    public static final void showPaneNotification(String text, String graphics) {

        new Thread(() -> {
            while (true) {
                if (isShownNotif) {
                    isNotifPending = true;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    isShownNotif = true;

                    Platform.runLater(() -> {
                        notifPane.setGraphic(new ImageView(graphics));
                        if (notifPane.isShowing()) {
                            notifPane.hide();
                            notifPane.show(text);
                        } else {
                            notifPane.show(text);
                        }
                    });

                    for (int i = 0; i < DEFAULT_MAIN_NOTIF_TIMEOUT / 200 && !isNotifPending; i++) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    notifPane.hide();
                    isShownNotif = false;
                    isNotifPending = false;
                    return;
                }
            }
        }).start();
    }

    //REF : https://community.oracle.com/thread/3677813?start=0&tstart=0 //TODO delete later
    @FXML
    private void showAboutView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Info.Resource.FXML_FILE_ABOUT));
            BorderPane root = (BorderPane) loader.load(
                    MainViewController.class
                    .getResourceAsStream(Info.Resource.FXML_FILE_ABOUT));
            //Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);

            stage.setTitle(
                    "CSGO:RR About");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void showChangelogView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Info.Resource.FXML_FILE_CHANGELOG));
            BorderPane root = (BorderPane) loader.load(
                    MainViewController.class
                    .getResourceAsStream(Info.Resource.FXML_FILE_ABOUT));
            //Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);

            stage.setTitle(
                    "CSGO:RR About");
            stage.setOnCloseRequest((WindowEvent event) -> {
                changelogPageToggleButton.setSelected(false);
            });

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void exitApplication() {
        try {

            CsgoRr.removeNativeListeners();
            System.exit(0);
        } catch (NativeHookException ex) {
            System.out.println("Failed to cleanup.");
            Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private MenuItem miExportFolder;

    @FXML
    private void openExportFolder() {
        try {
            Desktop.getDesktop().open(Paths.get("./weapon_exports").toFile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
