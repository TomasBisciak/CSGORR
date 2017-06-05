/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.nativeHook;

import csgorr.CsgoRr;
import csgorr.controllers.PreferencesViewController;
import csgorr.utils.IntegerMutable;
import csgorr.weapon.Weapon;
import java.awt.AWTException;
import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.application.Platform;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Kofola
 */
public class GlobalKeyListener implements NativeKeyListener {
    
    private boolean keyDetectionFlag;
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        
        if (keyDetectionFlag) {
            PreferencesViewController.latestKeyPressed = e.getKeyCode();
        } else {
            
            if (isWeaponChangeHotKey(e)) {
                System.out.println("DEBUG: change weapon hotkey pressed");
                changeWeapon(e.getKeyCode());
            }
            
        }
        
        if (CsgoRr.getModel().isRrEnabled() && e.getKeyCode() == CsgoRr.getModel().getAppPrefs().getKeyToggle()) {
            CsgoRr.getModel().setRrEnabled(false);
            Platform.runLater(() -> {
                CsgoRr.getMainViewController().getActivateRrMenuBtn().selectedProperty().set(false);
            });
            
        } else if (!(CsgoRr.getModel().isRrEnabled()) && e.getKeyCode() == CsgoRr.getModel().getAppPrefs().getKeyToggle()) {
            CsgoRr.getModel().setRrEnabled(true);
            Platform.runLater(() -> {
                CsgoRr.getMainViewController().getActivateRrMenuBtn().selectedProperty().set(true);
            });
            
        }

        //test
//        if (e.getKeyCode() == NativeKeyEvent.VC_N) {
//            System.out.println("DEBUG:SHOW NOTIFICATION");
//            MainViewController.showPaneNotification("Test", 2000);
//        }
        //testing
        if (e.getKeyCode() == NativeKeyEvent.VC_HOME) {
            
            new Thread(() -> {
                CsgoRr.getModel().setActiveWeapon(Weapon.defaultWeapons.get(0));
            }).start();
            
        }
        
        if (e.getKeyCode() == NativeKeyEvent.VC_PAGE_UP) {

            //CsgoRr.showTransparentWindowNotif(String.valueOf(System.currentTimeMillis()));
            new Thread(() -> {
                CsgoRr.getModel().setActiveWeapon(Weapon.defaultWeapons.get(1));
            }).start();
            
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_END) {

            //CsgoRr.showTransparentWindowNotif(String.valueOf(System.currentTimeMillis()));
            new Thread(() -> {
                CsgoRr.getModel().setActiveWeapon(Weapon.defaultWeapons.get(2));
            }).start();
            
        }
        
    }
    
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        
    }
    
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        
    }
    
    public void enable() {
        GlobalScreen.addNativeKeyListener(this);
    }
    
    public void disable() {
        GlobalScreen.removeNativeKeyListener(this);
    }

    /**
     * @param keyDetectionFlag the keyDetectionFlag to set
     */
    public void setKeyDetectionFlag(boolean keyDetectionFlag) {
        this.keyDetectionFlag = keyDetectionFlag;
    }

    /**
     * index of weapon hotkey
     *
     * @param e keyEvent from native global key listener
     * @return if is WeaponChangeHotKey
     */
    private boolean isWeaponChangeHotKey(NativeKeyEvent e) {
        
        for (IntegerMutable im : CsgoRr.getModel().getAppPrefs().getWeaponHotKeys()) {
            if (e.getKeyCode() == im.getValue()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isWeaponChangeHotKey(int keyCode) {
        
        for (IntegerMutable im : CsgoRr.getModel().getAppPrefs().getWeaponHotKeys()) {
            if (keyCode == im.getValue()) {
                return true;
            }
        }
        return false;
    }
    
    private void changeWeapon(int keyCode) {
        for (int i = 0; i < 10; i++) {
            if (keyCode == CsgoRr.getModel().getAppPrefs().getWeaponHotKeys()[i].getValue()) {
                CsgoRr.getModel().setActiveWeapon(
                        //bit of mindfuck
                        
                        CsgoRr.getModel().getWeaponById(//we want weapon
                                CsgoRr.getModel().getActiveLoadout().getWeaponIds().get(i))//at i position from wepaon ids , we get weapon id that is at i position in loadout
                );
            }
        }
    }
    
}
