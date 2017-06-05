/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controllers;

import csgorr.AppPreferences;
import csgorr.CsgoRr;
import csgorr.utils.Info;
import csgorr.utils.IntegerMutable;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Kofola
 */
public class PreferencesViewController implements Initializable {

    @FXML
    private Button btnToggleKeyDetect;
    @FXML
    private TextField txtFieldToggleKey;
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField txtFieldTriggerTimeout;

    //wepaon HOTKEYS
    @FXML
    private TextField txtFieldHK1;
    @FXML
    private TextField txtFieldHK2;
    @FXML
    private TextField txtFieldHK3;
    @FXML
    private TextField txtFieldHK4;
    @FXML
    private TextField txtFieldHK5;
    @FXML
    private TextField txtFieldHK6;
    @FXML
    private TextField txtFieldHK7;
    @FXML
    private TextField txtFieldHK8;
    @FXML
    private TextField txtFieldHK9;
    @FXML
    private TextField txtFieldHK10;

    @FXML
    private Button btnDetectHK1;
    @FXML
    private Button btnDetectHK2;
    @FXML
    private Button btnDetectHK3;
    @FXML
    private Button btnDetectHK4;
    @FXML
    private Button btnDetectHK5;
    @FXML
    private Button btnDetectHK6;
    @FXML
    private Button btnDetectHK7;
    @FXML
    private Button btnDetectHK8;
    @FXML
    private Button btnDetectHK9;
    @FXML
    private Button btnDetectHK10;

    //private final Button[] hkButtons = {btnDetectHK1, btnDetectHK2, btnDetectHK3, btnDetectHK4, btnDetectHK5, btnDetectHK6, btnDetectHK7, btnDetectHK8, btnDetectHK9, btnDetectHK10};
    //to store value from nativeKeyListener
    public static volatile int latestKeyPressed = Integer.MIN_VALUE;

    private static final IntegerMutable tempKpToggleKey = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getKeyToggle());

    private static final IntegerMutable tempkphk1 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk1());
    private static final IntegerMutable tempkphk2 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk2());
    private static final IntegerMutable tempkphk3 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk3());
    private static final IntegerMutable tempkphk4 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk4());
    private static final IntegerMutable tempkphk5 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk5());
    private static final IntegerMutable tempkphk6 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk6());
    private static final IntegerMutable tempkphk7 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk7());
    private static final IntegerMutable tempkphk8 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk8());
    private static final IntegerMutable tempkphk9 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk9());
    private static final IntegerMutable tempkphk10 = new IntegerMutable(CsgoRr.getModel().getAppPrefs().getWhk10());

    @FXML
    private HBox hboxBottom;

    private static final IntegerMutable[] keyCache = {
        tempKpToggleKey,
        tempkphk1,
        tempkphk2,
        tempkphk3,
        tempkphk4,
        tempkphk5,
        tempkphk6,
        tempkphk7,
        tempkphk8,
        tempkphk9,
        tempkphk10,};

    //////////////////////////////////////////////////
    @FXML
    private Slider hsliderIngameSensitivity;
    @FXML
    private Button btnResetToDefault;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hsliderIngameSensitivity.setValue(CsgoRr.getModel().getAppPrefs().getIngameSensitivity());
        txtFieldToggleKey.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getKeyToggle()));
        txtFieldTriggerTimeout.setText(String.valueOf(CsgoRr.getModel().getAppPrefs().getPreTriggerTimeout()));

        txtFieldTriggerTimeout.textProperty().addListener((ObservableValue<? extends String> v, String oldVal, String newVal) -> {
            //todo cahnge
            try {
                if (Integer.valueOf(newVal) < 0) {
                    throw new NumberFormatException("negative number is not alloved.");
                } else {
                    txtFieldTriggerTimeout.setStyle("");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                txtFieldTriggerTimeout.setStyle("-fx-border-color:red");
            }
            dataChanged();
        });

        txtFieldHK1.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk1()));
        txtFieldHK2.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk2()));
        txtFieldHK3.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk3()));
        txtFieldHK4.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk4()));
        txtFieldHK5.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk5()));
        txtFieldHK6.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk6()));
        txtFieldHK7.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk7()));
        txtFieldHK8.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk8()));
        txtFieldHK9.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk9()));
        txtFieldHK10.setText(NativeKeyEvent.getKeyText(CsgoRr.getModel().getAppPrefs().getWhk10()));

        dataChanged.addListener((v, oldVal, newVal) -> {
            if (newVal) {
                hboxBottom.getChildren().add(0, new Label("Data changed"));
            } else {
                hboxBottom.getChildren().remove(0);
            }
        });

    }

    //make sure only one detection THREAD is enabled at a time.
    private static volatile boolean detectMode;

    //used for latest valid keypressed value (toggleKey).
    @FXML
    @SuppressWarnings("SleepWhileInLoop")
    private void toggleKeyDetectOnAction() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempKpToggleKey.setValue(keyCode);
                    dataChanged();
                }
                Platform.runLater(() -> {
                    txtFieldToggleKey.setText(NativeKeyEvent.getKeyText(tempKpToggleKey.getValue()));
                });
            }).start();
        }
    }

    public static int keyDetection() {

        if (!detectMode) {
            detectMode = true;
            int x = -1;
            ExecutorService es = Executors.newSingleThreadExecutor();
            @SuppressWarnings("SleepWhileInLoop")
            Future<Integer> result = es.submit(() -> {
                int tempLatestKeyPress = -1;
                while (latestKeyPressed == Integer.MIN_VALUE) {
                    CsgoRr.getGlobalKeyListener().setKeyDetectionFlag(true);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PreferencesViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (!NativeKeyEvent.getKeyText(latestKeyPressed).equals("Unknown keyCode: 0x-80000000") && CsgoRr.getModel().getAppPrefs().isKeyAvailable(latestKeyPressed, keyCache)) {
                        tempLatestKeyPress = latestKeyPressed;
                        break;
                    }
                }
                CsgoRr.getGlobalKeyListener().setKeyDetectionFlag(false);
                latestKeyPressed = Integer.MIN_VALUE;
                detectMode = false;
                return tempLatestKeyPress;
            });
            try {
                x = result.get();
            } catch (InterruptedException | ExecutionException e) {
                // failed
            }
            es.shutdown();
            return x;
        } else {
            return -1;
        }

    }

    @FXML
    private void preTriggerTimeoutDefaultOnAction() {
        txtFieldTriggerTimeout.setText(String.valueOf(CsgoRr.getModel().getAppPrefs().getPreTriggerTimeout()));
        dataChanged();
    }

    private void dataChanged() {

        dataChanged.setValue(!(CsgoRr.getModel().getAppPrefs().getKeyToggle() == keyCache[0].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk1() == keyCache[1].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk2() == keyCache[2].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk3() == keyCache[3].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk4() == keyCache[4].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk5() == keyCache[5].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk6() == keyCache[6].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk7() == keyCache[7].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk8() == keyCache[8].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk9() == keyCache[9].getValue()
                && CsgoRr.getModel().getAppPrefs().getWhk10() == keyCache[10].getValue()
                && CsgoRr.getModel().getAppPrefs().getIngameSensitivity() == hsliderIngameSensitivity.getValue()
                && CsgoRr.getModel().getAppPrefs().getPreTriggerTimeout() == Integer.valueOf(txtFieldTriggerTimeout.getText())));

    }

    private static final SimpleBooleanProperty dataChanged = new SimpleBooleanProperty(false);

    public static boolean isDataChanged() {
        return dataChanged.get();
    }

    @FXML
    @SuppressWarnings("CallToPrintStackTrace")
    private void confirmChanges() {

        if (!txtFieldTriggerTimeout.getStyle().contains("-fx-border-color:red")) {
            CsgoRr.getModel().getAppPrefs().setKeyToggle(tempKpToggleKey.getValue());
            CsgoRr.getModel().getAppPrefs().setIngameSensitivity(hsliderIngameSensitivity.getValue());

            //hotkey controls
            CsgoRr.getModel().getAppPrefs().setWhk1(tempkphk1.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk2(tempkphk2.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk3(tempkphk3.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk4(tempkphk4.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk5(tempkphk5.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk6(tempkphk6.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk7(tempkphk7.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk8(tempkphk8.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk9(tempkphk9.getValue());
            CsgoRr.getModel().getAppPrefs().setWhk10(tempkphk10.getValue());

            try {
                CsgoRr.getModel().getAppPrefs().setPreTriggerTimeout(Integer.valueOf(txtFieldTriggerTimeout.getText()));
            } catch (NumberFormatException ex) {
                CsgoRr.getModel().getAppPrefs().setPreTriggerTimeout(AppPreferences.DEFAULT_PRE_TRIGGER_TIMEOUT);
                ex.printStackTrace();
            }
            dataChanged.setValue(false);
            MainViewController.showPaneNotification("Preferences updated.",  Info.Resource.IMG_ALERT_INFO);
        }else{
             MainViewController.showPaneNotification("Some field/fields have incorrect input.",  Info.Resource.IMG_ALERT_FAILURE);
        }

    }

    @FXML
    private void resetToDefaultOnAction() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("All data will be reset to default values.");
        alert.setContentText("Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Setting to default ");
            //set text fields values and change cache to default values
            hsliderIngameSensitivity.setValue(2.0d);

            tempKpToggleKey.setValue(AppPreferences.DEFAULT_TOGGLE_KEY);
            tempkphk1.setValue(AppPreferences.DEFAULT_KEY_WHK1);
            tempkphk2.setValue(AppPreferences.DEFAULT_KEY_WHK2);
            tempkphk3.setValue(AppPreferences.DEFAULT_KEY_WHK3);
            tempkphk4.setValue(AppPreferences.DEFAULT_KEY_WHK4);
            tempkphk5.setValue(AppPreferences.DEFAULT_KEY_WHK5);
            tempkphk6.setValue(AppPreferences.DEFAULT_KEY_WHK6);
            tempkphk7.setValue(AppPreferences.DEFAULT_KEY_WHK7);
            tempkphk8.setValue(AppPreferences.DEFAULT_KEY_WHK8);
            tempkphk9.setValue(AppPreferences.DEFAULT_KEY_WHK9);
            tempkphk10.setValue(AppPreferences.DEFAULT_KEY_WHK10);

            //data changed?
            dataChanged();

            Platform.runLater(() -> {
                txtFieldToggleKey.setText(NativeKeyEvent.getKeyText(tempKpToggleKey.getValue()));
                txtFieldHK1.setText(NativeKeyEvent.getKeyText(tempkphk1.getValue()));
                txtFieldHK2.setText(NativeKeyEvent.getKeyText(tempkphk2.getValue()));
                txtFieldHK3.setText(NativeKeyEvent.getKeyText(tempkphk3.getValue()));
                txtFieldHK4.setText(NativeKeyEvent.getKeyText(tempkphk4.getValue()));
                txtFieldHK5.setText(NativeKeyEvent.getKeyText(tempkphk5.getValue()));
                txtFieldHK6.setText(NativeKeyEvent.getKeyText(tempkphk6.getValue()));
                txtFieldHK7.setText(NativeKeyEvent.getKeyText(tempkphk7.getValue()));
                txtFieldHK8.setText(NativeKeyEvent.getKeyText(tempkphk8.getValue()));
                txtFieldHK9.setText(NativeKeyEvent.getKeyText(tempkphk9.getValue()));
                txtFieldHK10.setText(NativeKeyEvent.getKeyText(tempkphk10.getValue()));
                txtFieldTriggerTimeout.setText(String.valueOf(CsgoRr.getModel().getAppPrefs().getPreTriggerTimeout()));
            });
        }

    }

    @FXML
    private void hkOnDetect1() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk1.setValue(keyCode);

                }
                Platform.runLater(() -> {
                    txtFieldHK1.setText(NativeKeyEvent.getKeyText(tempkphk1.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect2() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk2.setValue(keyCode);
                }
                Platform.runLater(() -> {
                    txtFieldHK2.setText(NativeKeyEvent.getKeyText(tempkphk2.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect3() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk3.setValue(keyCode);

                }
                Platform.runLater(() -> {
                    txtFieldHK3.setText(NativeKeyEvent.getKeyText(tempkphk3.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect4() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk4.setValue(keyCode);

                }
                Platform.runLater(() -> {
                    txtFieldHK4.setText(NativeKeyEvent.getKeyText(tempkphk4.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect5() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk5.setValue(keyCode);

                }
                Platform.runLater(() -> {
                    txtFieldHK5.setText(NativeKeyEvent.getKeyText(tempkphk5.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect6() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk6.setValue(keyCode);

                }
                Platform.runLater(() -> {
                    txtFieldHK6.setText(NativeKeyEvent.getKeyText(tempkphk6.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect7() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk7.setValue(keyCode);

                }
                Platform.runLater(() -> {
                    txtFieldHK7.setText(NativeKeyEvent.getKeyText(tempkphk7.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect8() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk8.setValue(keyCode);

                }
                Platform.runLater(() -> {
                    txtFieldHK8.setText(NativeKeyEvent.getKeyText(tempkphk8.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect9() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk9.setValue(keyCode);
                }
                Platform.runLater(() -> {
                    txtFieldHK9.setText(NativeKeyEvent.getKeyText(tempkphk9.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

    @FXML
    private void hkOnDetect10() {
        if (!detectMode) {
            new Thread(() -> {
                int keyCode;
                if ((keyCode = PreferencesViewController.keyDetection()) != -1) {
                    tempkphk10.setValue(keyCode);
                }
                Platform.runLater(() -> {
                    txtFieldHK10.setText(NativeKeyEvent.getKeyText(tempkphk10.getValue()));
                    dataChanged();
                });
            }).start();
        }
    }

}
