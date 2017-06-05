/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.model;

import csgorr.controls.NamedComponent;
import csgorr.weapon.Weapon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User created or default loadouts of 10 weapon recoil patterns
 *
 * @author Kofola
 */
public class Loadout implements NamedComponent {

    //holds weapon id's , fixed size 10 , default null values dont use add() use set().
    public static final int LOADOUT_SIZE = 10;
    private List<Long> weaponIds;
    private String loadoutName;
    private long id;

    public Loadout(Long[] weaponIds, String loadoutName) {
        this.weaponIds = Arrays.asList(new Long[10]);
        setWeaponIds(weaponIds);
        this.loadoutName = loadoutName;
    }

    public Loadout(Long[] weaponIds, String loadoutName, long id) {
        this.weaponIds = Arrays.asList(new Long[10]);
        setWeaponIds(weaponIds);
        this.loadoutName = loadoutName;
        this.id=id;
    }

    public Loadout() {
        this.weaponIds = Arrays.asList(new Long[10]);
        loadoutName = "";
    }

    
    public List<Long> getWeaponIds() {
        return weaponIds;
    }

    
    public final void setWeaponIds(Long[] weaponIds) {
        for (int i = 0; i < this.weaponIds.size(); i++) {
            this.weaponIds.set(i, weaponIds[i]);
        }
    }

    public final void setWeaponIdsNoRef(Long[] weaponIds) {
        this.weaponIds=Arrays.asList(Arrays.copyOf(weaponIds, LOADOUT_SIZE));
    }

    /**
     * @return the loadoutName
     */
    public String getName() {
        return loadoutName;
    }

    /**
     * @param loadoutName the loadoutName to set
     */
    public void setLoadoutName(String loadoutName) {
        //TODO CREATE CONDITION THAT CHECKS IF LOADOUT NAME IS AVAILABLE 
        this.loadoutName = loadoutName;
    }

//    @Override
//    public String toString() {
//        return "Loadout name:"+loadoutName+"\t"
//    }
//    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Loadout " + loadoutName + "\n" + "slot1:" + getWeaponIds().get(0) + "\n"
                + "slot2:" + getWeaponIds().get(1) + "\n"
                + "slot3:" + getWeaponIds().get(2) + "\n"
                + "slot4:" + getWeaponIds().get(3) + "\n"
                + "slot5:" + getWeaponIds().get(4) + "\n"
                + "slot6:" + getWeaponIds().get(5) + "\n"
                + "slot7:" + getWeaponIds().get(6) + "\n"
                + "slot8:" + getWeaponIds().get(7) + "\n"
                + "slot9:" + getWeaponIds().get(8) + "\n"
                + "slot10:" + getWeaponIds().get(9) + "\n";
    }

    public Loadout copy() {
        String ln_bckp=loadoutName;//imutable string  so new instance created//points to another location.//not safe for regullar objects ofcourse.
        return new Loadout(Arrays.copyOf((Long[])weaponIds.toArray(), LOADOUT_SIZE),ln_bckp,id);
    }

}
