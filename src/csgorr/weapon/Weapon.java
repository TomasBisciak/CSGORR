/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.weapon;

import csgorr.controls.NamedComponent;
import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kofola
 */
public class Weapon implements NamedComponent {

    public static final String NAME_WEAPON_TEST1 = "test1";
    public static final String NAME_WEAPON_TEST2 = "test2";
    public static final String NAME_WEAPON_TEST3 = "test3";

    private long id;

    //TESTING
    public static final List<Weapon> defaultWeapons = new ArrayList();

    static {
        try {
            defaultWeapons.add(Weapon.createWeapon(NAME_WEAPON_TEST1, new RecoilPattern(RecoilPattern.defaultPatternsHolder.get(NAME_WEAPON_TEST1),
                    RecoilPattern.DEFAULT_SMOOTHNESS_FACTOR_PERCENTAGE, RecoilPattern.DEFAULT_DEVIATION_VALUE_ABSOLUTE, RecoilPattern.DEFAULT_DEVIATION_PERCENTAGE)));
            defaultWeapons.add(Weapon.createWeapon(NAME_WEAPON_TEST2, new RecoilPattern(RecoilPattern.defaultPatternsHolder.get(NAME_WEAPON_TEST2),
                    RecoilPattern.DEFAULT_SMOOTHNESS_FACTOR_PERCENTAGE, RecoilPattern.DEFAULT_DEVIATION_VALUE_ABSOLUTE, RecoilPattern.DEFAULT_DEVIATION_PERCENTAGE)));
            defaultWeapons.add(Weapon.createWeapon(NAME_WEAPON_TEST3, new RecoilPattern(RecoilPattern.defaultPatternsHolder.get(NAME_WEAPON_TEST3),
                    RecoilPattern.DEFAULT_SMOOTHNESS_FACTOR_PERCENTAGE, RecoilPattern.DEFAULT_DEVIATION_VALUE_ABSOLUTE, RecoilPattern.DEFAULT_DEVIATION_PERCENTAGE)));
        } catch (AWTException ex) {
            Logger.getLogger(Weapon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String weaponName;
    //[ms][->]
    //REFERENCE -
    //TODO might change
    private RecoilPattern recoilPattern;
        //TODO ATT THE SMOOTHING STUFF

    //multiple threads can share this variable when one clicks happen , have to be locked
    private volatile boolean triggerPulled;
    private boolean rReductionActive;

    private WeaponRobot weaponRobot;

    private int threadsActive = 0;

    private synchronized void incrementThreadsActive() {
        threadsActive++;
    }

    private synchronized void decrementThreadsActive() {
        threadsActive--;
    }

    //image
    //audio
    private Weapon(String weaponName, RecoilPattern recoilPattern) {
        this.weaponName = weaponName;
        this.recoilPattern = recoilPattern;
    }

    public void shoot() {
        //only one can exist at a time
        System.out.println("Threads active=" + threadsActive);
        if (!isrReductionActive()) {
            new Thread(() -> {
                setrReductionActive(true);
                incrementThreadsActive();
                
                synchronized (recoilPattern) {//noone should be able to edit it when its active//TODO might change
                    weaponRobot.reduceRecoil();
                }
                
                decrementThreadsActive();
                setrReductionActive(false);
            }).start();
        } else {
            System.out.println("DEBUG: RR ALREADY ACTIVE");
        }

    }

    private void init(Weapon weapon) throws AWTException {
        weaponRobot = new WeaponRobot(weapon);
    }

    //when creating weapon id is null sicne we dont have one
    public static final Weapon createWeapon(String weaponName, RecoilPattern recoilPattern) throws AWTException {
        Weapon weapon = new Weapon(weaponName, recoilPattern);
        weapon.init(weapon);
        return weapon;
    }

    //weapon loaded from database has ID.
    public static final Weapon createWeapon(String weaponName, RecoilPattern recoilPattern, long id) throws AWTException {
        Weapon weapon = new Weapon(weaponName, recoilPattern);
        weapon.init(weapon);
        weapon.setId(id);
        return weapon;
    }

    public boolean isTriggerPulled() {
        return triggerPulled;
    }

    public void setTriggerPulled(boolean triggerPulled) {
        this.triggerPulled = triggerPulled;
    }

    private synchronized boolean isrReductionActive() {
        return rReductionActive;
    }

    public synchronized void setrReductionActive(boolean rReductionActive) {
        this.rReductionActive = rReductionActive;
    }

    public final Weapon copy() {
        String wepName = weaponName;

        try {
            Weapon copy = Weapon.createWeapon(wepName, recoilPattern.copy(), id);
            return copy;
        } catch (AWTException ex) {

            Logger.getLogger(Weapon.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * @return the weaponName
     */
    @Override
    public String getName() {
        return weaponName;
    }

    public void setName(String weaponName) {
        this.weaponName = weaponName;
    }

    /**
     * @return the recoilPattern
     */
    public RecoilPattern getRecoilPattern() {
        return recoilPattern;
    }

    public void setRecoilPatternNoRef(RecoilPattern rp) {
        recoilPattern = rp.copy();
    }

    public void setRecoilPattern(RecoilPattern rp) {
        recoilPattern = rp;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    //TODO REMOVE ONLY FOR TESTING//not sure why i sayd remove this ffs
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "" + weaponName;
    }

}
