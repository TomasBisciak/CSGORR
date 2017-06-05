/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import csgorr.AppPreferences;
import csgorr.CsgoRr;
import csgorr.controls.TransparentWindowNotif;
import csgorr.db.DbUtil;
import csgorr.weapon.Weapon;
import java.awt.Toolkit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kofola
 */
public class MainModel {

    private final AppPreferences appPrefs;
    private volatile boolean rrEnabled;

    private Weapon activeWeapon;
    private Loadout activeLoadout;

    private boolean incognitoMode;

    //holds all the weapons //up to date
    //private final List<Weapon> weaponCache;
    private final ObservableList<Weapon> weaponCache;
    //holds all loadouts
    // private final List<Loadout> loadoutCache;
    private final ObservableList<Loadout> loadoutCache;

    public MainModel() {

        appPrefs = new AppPreferences();

        //weaponCache = Collections.synchronizedList(DbUtil.getWeapons());
        //loadoutCache = Collections.synchronizedList(DbUtil.getLoadouts());
        weaponCache = FXCollections.observableList(DbUtil.getWeapons());
        loadoutCache = FXCollections.observableList(DbUtil.getLoadouts());

    }

    public void loadWeaponCache() {
        weaponCache.removeAll(weaponCache);
        weaponCache.addAll(DbUtil.getWeapons());
    }

    public void loadLoadoutCache() {
        loadoutCache.removeAll(loadoutCache);
        loadoutCache.addAll(DbUtil.getLoadouts());
    }

    public Weapon getWeaponById(long id) {
        for (Weapon wep : weaponCache) {
            if (wep.getId() == id) {
                return wep;
            }
        }
        return null;
    }

    public Loadout getLoadoutById(long id) {
        for (Loadout loadout : loadoutCache) {
            if (loadout.getId() == id) {
                return loadout;
            }
        }
        return null;
    }

    public AppPreferences getAppPrefs() {
        return appPrefs;
    }

    public Weapon getActiveWeapon() {
        return activeWeapon;
    }

    public void setActiveWeapon(Weapon activeWeapon) {
        this.activeWeapon = activeWeapon;
        Platform.runLater(() -> {
            if (activeWeapon != null) {
                CsgoRr.getMainViewController().setActiveWeaponName(activeWeapon.getName());

            } else {
                CsgoRr.getMainViewController().setActiveWeaponName("None.");
            }
            if (!incognitoMode) {
                CsgoRr.showTransparentWindowNotif("Active Weapon:" + (activeWeapon != null ? activeWeapon.getName() : "None."), 5, 2, null, TransparentWindowNotif.DEFAULT_TIMEOUT);
            }

        });

    }

    public boolean isRrEnabled() {
        return rrEnabled;
    }

    //called from NativeKeyListener
    public void setRrEnabled(boolean enabled) {
        this.rrEnabled = enabled;
        if (enabled) {
            CsgoRr.getTray().getEnabledRrChckBox().setState(true);
//            if (!incognitoMode) {
//                CsgoRr.showTransparentWindowNotif("Recoil reduction: Enabled",3,2,null,TransparentWindowNotif.DEFAULT_TIMEOUT);
//            }
        } else {
            CsgoRr.getTray().getEnabledRrChckBox().setState(false);
//            if (!incognitoMode) {
//                CsgoRr.showTransparentWindowNotif("Recoil reduction: Disabled",3,2,null,TransparentWindowNotif.DEFAULT_TIMEOUT);
//            }

        }
        if (!incognitoMode) {
            CsgoRr.showTransparentWindowNotif("Recoil reduction:" + (enabled ? "Enabled" : "Disabled"), 3, 2, null, TransparentWindowNotif.DEFAULT_TIMEOUT);
        }

    }

    /**
     * @return the incognitoMode
     */
    public boolean isIncognitoMode() {
        return incognitoMode;
    }

    /**
     * @param incognitoMode the incognitoMode to set
     */
    public void setIncognitoMode(boolean incognitoMode) {
        this.incognitoMode = incognitoMode;
    }

    /**
     * @return the activeLoadout
     */
    public Loadout getActiveLoadout() {
        return activeLoadout;
    }

    /**
     * @param activeLoadout the activeLoadout to set
     */
    public void setActiveLoadout(Loadout activeLoadout) {
        setActiveWeapon(null);//on loadout change deselect any selected weapon
        this.activeLoadout = activeLoadout;
        if (activeLoadout != null) {
            System.out.println("DEBUG: active loadout" + this.activeLoadout.getName());

        } else {
            System.out.println("DEBUG: active loadout set to NULL");
        }
        CsgoRr.getMainViewController().setActiveLoadoutInMainView(activeLoadout);
    }

    /**
     * @return the weaponCache
     */
//    public List<Weapon> getWeaponCache() {
//        return weaponCache;
//    }
    public ObservableList<Weapon> getWeaponCache() {
        return weaponCache;
    }

    /**
     * @return the loadoutCache
     */
//    public List<Loadout> getLoadoutCache() {
//        return loadoutCache;
//    }
    public ObservableList<Loadout> getLoadoutCache() {
        return loadoutCache;
    }

}
