/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.HeadlessException;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 *
 * @author Kofola
 */
public class Tray {

    private final Stage stage;
    private TrayIcon icon;
    private CheckboxMenuItem enabledRrChckBox;



    public CheckboxMenuItem getEnabledRrChckBox() {
        return enabledRrChckBox;
    }

    public Tray(Stage stage, String imagePath) {

        this.stage = stage;
        hookTray(imagePath);
    }

    public void showMessage(String message, TrayIcon.MessageType TYPE) {
        icon.displayMessage("CS:GO Recoil reducer", message, TYPE);
    }

    private void hookTray(String image) {

        try {
            icon = new TrayIcon(ImageIO.read(getClass().getResourceAsStream(image)), "CS:GO Recoil reducer",
                    createPopupMenu());

            icon.addActionListener((ActionEvent e) -> {
                Platform.runLater(() -> {
                    stage.setIconified(false);//nevim co presne naco som t osem dal
                });

            });
            SystemTray.getSystemTray().add(icon);

            icon.displayMessage("CSMP", "Ready to be used.",
                    TrayIcon.MessageType.INFO);

            Platform.setImplicitExit(false);

        } catch (AWTException | IOException ex) {
        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    public PopupMenu createPopupMenu() throws HeadlessException {
        PopupMenu menu = new PopupMenu();

        MenuItem exit = new MenuItem("Exit");
        MenuItem showWindow = new MenuItem("Show window");
        enabledRrChckBox = new CheckboxMenuItem("Recoil reduction");
        enabledRrChckBox.setEnabled(false);
        enabledRrChckBox.setState(false);
        showWindow.addActionListener((ActionEvent e) -> {
            Platform.runLater(() -> {
                try {
                    CsgoRr.showMainWindow();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        });

        exit.addActionListener((ActionEvent e) -> {
            try {
                
                CsgoRr.removeNativeListeners();
                System.exit(0);
            } catch (NativeHookException ex) {
                System.out.println("Failed to cleanup.");
                Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
            }
        });


        menu.add(showWindow);
        menu.add(enabledRrChckBox);
        menu.add(exit);

        return menu;
    }

 

    public void setTrayImage(String imagePath) {
        try {
            icon.setImage(ImageIO.read(getClass().getResourceAsStream(imagePath)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
