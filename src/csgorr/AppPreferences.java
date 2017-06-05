/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr;

import csgorr.utils.IntegerMutable;
import java.awt.MouseInfo;
import java.util.prefs.Preferences;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Kofola
 */
public class AppPreferences {

    public static final String PREFS_SHOW_MAIN_WIN = "showmainwindow";
    public static final String PREFS_TOGGLE_KEY = "maintogglekeycode";
    public static final String PREFS_SHOOT_MOUSE_KEY = "shootkey";
    public static final String PREFS_INGAME_SENS = "ingamesensitivity";
    public static final String PREFS_PRE_TRIGGER_TIMEOUT = "pretriggertimeout";

    //weapon hot key controls
    public static final String PREFS_WHK1 = "whk1";
    public static final String PREFS_WHK2 = "whk2";
    public static final String PREFS_WHK3 = "whk3";
    public static final String PREFS_WHK4 = "whk4";
    public static final String PREFS_WHK5 = "whk5";
    public static final String PREFS_WHK6 = "whk6";
    public static final String PREFS_WHK7 = "whk7";
    public static final String PREFS_WHK8 = "whk8";
    public static final String PREFS_WHK9 = "whk9";
    public static final String PREFS_WHK10 = "whk10";

    public static final int DEFAULT_KEY_WHK1 = NativeKeyEvent.VC_KP_1;
    public static final int DEFAULT_KEY_WHK2 = NativeKeyEvent.VC_KP_2;
    public static final int DEFAULT_KEY_WHK3 = NativeKeyEvent.VC_KP_3;
    public static final int DEFAULT_KEY_WHK4 = NativeKeyEvent.VC_KP_4;
    public static final int DEFAULT_KEY_WHK5 = NativeKeyEvent.VC_KP_5;
    public static final int DEFAULT_KEY_WHK6 = NativeKeyEvent.VC_KP_6;
    public static final int DEFAULT_KEY_WHK7 = NativeKeyEvent.VC_KP_7;
    public static final int DEFAULT_KEY_WHK8 = NativeKeyEvent.VC_KP_8;
    public static final int DEFAULT_KEY_WHK9 = NativeKeyEvent.VC_KP_9;
    public static final int DEFAULT_KEY_WHK10 = NativeKeyEvent.VC_KP_0;

    private final IntegerMutable whk1;
    private final IntegerMutable whk2;
    private final IntegerMutable whk3;
    private final IntegerMutable whk4;
    private final IntegerMutable whk5;
    private final IntegerMutable whk6;
    private final IntegerMutable whk7;
    private final IntegerMutable whk8;
    private final IntegerMutable whk9;
    private final IntegerMutable whk10;

    private final IntegerMutable[] weaponHotKeys;
    
    
    public static final int DEFAULT_PRE_TRIGGER_TIMEOUT = 100;
    private int preTriggerTimeout = DEFAULT_PRE_TRIGGER_TIMEOUT;

    public static final double DEFAULT_INGAME_SENSITIVITY = 2;
    public double ingameSensitivity;

    public static final int DEFAULT_SHOOT_KEY = 1;
    public int keyShoot = DEFAULT_SHOOT_KEY;

    public static final int DEFAULT_TOGGLE_KEY = NativeKeyEvent.VC_CAPS_LOCK;
    private int keyToggle;

    private final Preferences appPref = Preferences.userRoot().node("csgorr/app");

    public AppPreferences() {
        ingameSensitivity = getPreferences().getDouble(PREFS_INGAME_SENS, DEFAULT_INGAME_SENSITIVITY);
        keyToggle = getPreferences().getInt(PREFS_TOGGLE_KEY, DEFAULT_TOGGLE_KEY);

        whk1 = new IntegerMutable(getPreferences().getInt(PREFS_WHK1, DEFAULT_KEY_WHK1));
        whk2 = new IntegerMutable(getPreferences().getInt(PREFS_WHK2, DEFAULT_KEY_WHK2));
        whk3 = new IntegerMutable(getPreferences().getInt(PREFS_WHK3, DEFAULT_KEY_WHK3));
        whk4 = new IntegerMutable(getPreferences().getInt(PREFS_WHK4, DEFAULT_KEY_WHK4));
        whk5 = new IntegerMutable(getPreferences().getInt(PREFS_WHK5, DEFAULT_KEY_WHK5));
        whk6 = new IntegerMutable(getPreferences().getInt(PREFS_WHK6, DEFAULT_KEY_WHK6));
        whk7 = new IntegerMutable(getPreferences().getInt(PREFS_WHK7, DEFAULT_KEY_WHK7));
        whk8 = new IntegerMutable(getPreferences().getInt(PREFS_WHK8, DEFAULT_KEY_WHK8));
        whk9 = new IntegerMutable(getPreferences().getInt(PREFS_WHK9, DEFAULT_KEY_WHK9));
        whk10 = new IntegerMutable(getPreferences().getInt(PREFS_WHK10, DEFAULT_KEY_WHK10));

        weaponHotKeys = new IntegerMutable[]{
            whk1,
            whk2,
            whk3,
            whk4,
            whk5,
            whk6,
            whk7,
            whk8,
            whk9,
            whk10,};

    }

    /**
     * Checks for availibility of vcKeyCode,and returns if key can be used in controls, Also checks if vcKeyCode is used in any of the cachedKeyCodes
     *
     * @param vcKeyCode keycode to check if available
     * @param cachedKeyCodes storage of cachedKeyCodes.Used before user hit confirmand writes into perferences
     * @return if available to be used true else false
     */
    public boolean isKeyAvailable(int vcKeyCode, IntegerMutable[] cachedKeyCodes) {
        //if any ofthe cached ones are selected , return not available
        for (IntegerMutable key : cachedKeyCodes) {
            System.out.println("DEBUG5 Key value:" + NativeKeyEvent.getKeyText(vcKeyCode) + "\tCached Value:" + NativeKeyEvent.getKeyText(key.getValue()));

            if (key.getValue() == vcKeyCode) {
                return false;
            }
        }
        return true;
//        //actually active
//        return vcKeyCode != whk1
//                && vcKeyCode != whk2
//                && vcKeyCode != whk3
//                && vcKeyCode != whk4
//                && vcKeyCode != whk5
//                && vcKeyCode != whk6
//                && vcKeyCode != whk7
//                && vcKeyCode != whk8
//                && vcKeyCode != whk9
//                && vcKeyCode != whk10
//                && vcKeyCode != keyShoot
//                && vcKeyCode != keyToggle;
    }

    public final Preferences getPreferences() {
        return appPref;
    }

    public double getIngameSensitivity() {
        return ingameSensitivity;
    }

    public void setIngameSensitivity(double aIngameSensitivity) {
        if (aIngameSensitivity >= 1.0 && aIngameSensitivity <= 5.0 && ((aIngameSensitivity % 0.5) == 0)) {
            ingameSensitivity = aIngameSensitivity;
            getPreferences().putDouble(PREFS_INGAME_SENS, aIngameSensitivity);
        }

    }

    public int getKeyshoot() {
        return keyShoot;
    }

    public void setKeyShoot(int akeyShoot) {
        if (keyShoot <= MouseInfo.getNumberOfButtons() && keyShoot >= 0) {
            keyShoot = akeyShoot;
        }
    }

    public int getKeyToggle() {
        return keyToggle;
    }

    public void setKeyToggle(int keyToggle) {
        this.keyToggle = keyToggle;
        getPreferences().putInt(AppPreferences.PREFS_TOGGLE_KEY, keyToggle);
    }

    public int getPreTriggerTimeout() {
        return preTriggerTimeout;
    }

    public void setPreTriggerTimeout(int preTriggerTimeout) {
        this.preTriggerTimeout = preTriggerTimeout;
        getPreferences().putInt(AppPreferences.PREFS_PRE_TRIGGER_TIMEOUT, preTriggerTimeout);
    }

    /**
     * @return the whk1
     */
    public int getWhk1() {
        return whk1.getValue();
    }

    /**
     * @param whk1 the whk1 to set
     */
    public void setWhk1(int whk1) {
        this.whk1.setValue(whk1);
        getPreferences().putInt(AppPreferences.PREFS_WHK1, whk1);
    }

    /**
     * @return the whk2
     */
    public int getWhk2() {
        return whk2.getValue();
    }

    /**
     * @param whk2 the whk2 to set
     */
    public void setWhk2(int whk2) {
        this.whk2.setValue(whk2);
        getPreferences().putInt(AppPreferences.PREFS_WHK2, whk2);
    }

    /**
     * @return the whk3
     */
    public int getWhk3() {
        return whk3.getValue();
    }

    /**
     * @param whk3 the whk3 to set
     */
    public void setWhk3(int whk3) {
        this.whk3.setValue(whk3);
        getPreferences().putInt(AppPreferences.PREFS_WHK3, whk3);
    }

    /**
     * @return the whk4
     */
    public int getWhk4() {
        return whk4.getValue();
    }

    /**
     * @param whk4 the whk4 to set
     */
    public void setWhk4(int whk4) {
        this.whk4.setValue(whk4);
        getPreferences().putInt(AppPreferences.PREFS_WHK4, whk4);
    }

    /**
     * @return the whk5
     */
    public int getWhk5() {
        return whk5.getValue();
    }

    /**
     * @param whk5 the whk5 to set
     */
    public void setWhk5(int whk5) {
        this.whk5.setValue(whk5);
        getPreferences().putInt(AppPreferences.PREFS_WHK5, whk5);
    }

    /**
     * @return the whk6
     */
    public int getWhk6() {
        return whk6.getValue();
    }

    /**
     * @param whk6 the whk6 to set
     */
    public void setWhk6(int whk6) {
        this.whk6.setValue(whk6);
        getPreferences().putInt(AppPreferences.PREFS_WHK6, whk6);
    }

    /**
     * @return the whk7
     */
    public int getWhk7() {
        return whk7.getValue();
    }

    /**
     * @param whk7 the whk7 to set
     */
    public void setWhk7(int whk7) {
        this.whk7.setValue(whk7);
        getPreferences().putInt(AppPreferences.PREFS_WHK7, whk7);
    }

    /**
     * @return the whk8
     */
    public int getWhk8() {
        return whk8.getValue();
    }

    /**
     * @param whk8 the whk8 to set
     */
    public void setWhk8(int whk8) {
        this.whk8.setValue(whk8);
        getPreferences().putInt(AppPreferences.PREFS_WHK8, whk8);
    }

    /**
     * @return the whk9
     */
    public int getWhk9() {
        return whk9.getValue();
    }

    /**
     * @param whk9 the whk9 to set
     */
    public void setWhk9(int whk9) {
        this.whk9.setValue(whk9);
        getPreferences().putInt(AppPreferences.PREFS_WHK9, whk9);
    }

    /**
     * @return the whk10
     */
    public int getWhk10() {
        return whk10.getValue();
    }

    /**
     * @param whk10 the whk10 to set
     */
    public void setWhk10(int whk10) {
        this.whk10.setValue(whk10);
        getPreferences().putInt(AppPreferences.PREFS_WHK10, whk10);
    }

    /**
     * @return the weaponHotKeys
     */
    public IntegerMutable[] getWeaponHotKeys() {
        return weaponHotKeys;
    }

}
