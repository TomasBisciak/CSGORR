/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.weapon;

import csgorr.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Immutable
 *
 * @author Kofola
 */
public class RecoilPattern {

    public static final int NONE = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;

    public static final int DEFAULT_SMOOTHNESS_FACTOR_PERCENTAGE = 30;
    public static final int DEFAULT_DEVIATION_VALUE_ABSOLUTE = 1;// turning deviation value to 0 turns off smoothing completely
    public static final int DEFAULT_DEVIATION_PERCENTAGE = 60;

    //[name,[[steps][direction]]]
    //TODO UPDATE TO BETTER VERSION WHERE SMOOTHING IS TAKEN INTO ACCOUNT ETC
    public static final HashMap<String, int[][]> defaultPatternsHolder = new HashMap<>();

    static {
        //todo rework
        //[number of pixels to go][pixelsPerStep//speed multyplier][direction]
        defaultPatternsHolder.put(Weapon.NAME_WEAPON_TEST3, new int[][]{
            {200, 1, DOWN},
            {100, 1, RIGHT | DOWN},
            {50, 1, LEFT},
            {50, 1, NONE},
            {50, 1, RIGHT},
            {50, 1, LEFT}
        }
        );

        defaultPatternsHolder.put(Weapon.NAME_WEAPON_TEST1, new int[][]{ //sens: 1.0 steps:5,10ms delay
            {300, 10, DOWN},
            {300, 1, DOWN}

        });

        defaultPatternsHolder.put(Weapon.NAME_WEAPON_TEST2, new int[][]{ //sens: 1.0 steps:5,10ms delay
            {300, 1, DOWN},
            {50, 1, RIGHT | DOWN},
            {200, 1, LEFT | DOWN},
            {50, 1, LEFT},
            {100, 1, NONE},
            {300, 1, RIGHT},
            {200, 1, NONE},
            {200, 1, LEFT}
        });

    }

    private int smoothnessFactor = RecoilPattern.DEFAULT_SMOOTHNESS_FACTOR_PERCENTAGE;//percentage
    private int deviationValue = RecoilPattern.DEFAULT_DEVIATION_VALUE_ABSOLUTE;
    private int deviationPercentage = RecoilPattern.DEFAULT_DEVIATION_PERCENTAGE;

    
    public static final int PATTERN_WIDTH=3;
    private int[][] pattern;

    public RecoilPattern(int[][] pattern, int smoothnessFactor, int deviationValue, int deviationPercentage) {
        this.pattern = pattern;
        if (smoothnessFactor >= 0 && smoothnessFactor <= 100) {
            this.smoothnessFactor = smoothnessFactor;
        }
        this.deviationValue = deviationValue;
        if (deviationPercentage >= 0 && deviationPercentage <= 100) {
            this.deviationPercentage = deviationPercentage;
        }
        this.pattern = pattern;

    }
    public RecoilPattern(){
        pattern=new int[0][0];
    }// NOT SURE WHAT HAPPENS WHEN ARRAY IS NULL

    /**
     * @return the smoothnessFactor
     */
    public int getSmoothnessFactor() {
        return smoothnessFactor;
    }

    /**
     * @return the deviationValue
     */
    public int getDeviationValue() {
        return deviationValue;
    }

    /**
     * @return the deviationPercentage
     */
    public int getDeviationPercentage() {
        return deviationPercentage;
    }

    /**
     * @return the pattern
     */
    public int[][] getPattern() {
        return pattern;
    }

    //deep copy
    public final RecoilPattern copy() {
        return new RecoilPattern(Utils.cloneArray(pattern), smoothnessFactor, deviationValue, deviationPercentage);
    }

    public static final String directionToString(int recoil) {
        switch (recoil) {
            case 0: {
                return "NONE";
            }
            case 1: {
                return "UP";
            }
            case 2: {
                return "DOWN";
            }
            case 4: {
                return "LEFT";
            }
            case 8: {
                return "RIGHT";
            }
            //COMBINATIONS
            case 3: {//NONE AFTER CALCULATION
                return "UP&DOWN";
            }
            case 5: {
                return "UP&LEFT";
            }
            case 9: {
                return "UP&RIGHT";
            }
            case 6: {
                return "DOWN&LEFT";
            }
            case 10: {
                return "DOWN&RIGHT";
            }
            case 12: {
                return "LEFT&RIGHT";
            }

            default: {
                return "UNKNOWN";
            }
        }
    }

    public static final Integer[] splitBitDataIntoDirections(int data) {
        List<Integer> directions = new ArrayList<>();
        int k = 0;
        int index1 = -1, index2 = -1;
        //how many bits 1
        for (int i = 0; i < 6; i++) {
            if ((data & (1 << i)) != 0) {
                k++;
                if (k == 1) {
                    index1 = i;
                } else {
                    index2 = i;
                }
            }
        }

        if (k == 2) {
            //create integers
            //flip bits
            directions.add(data ^ (1 << index2));//FIRST
            directions.add(data ^ (1 << index1));//SECOND
        } else {
            directions.add(data);
        }
        return directions.toArray(new Integer[directions.size()]);
    }

    /**
     * @param smoothnessFactor the smoothnessFactor to set
     */
    public synchronized void setSmoothnessFactor(int smoothnessFactor) {
        this.smoothnessFactor = smoothnessFactor;
    }

    /**
     * @param deviationValue the deviationValue to set
     */
    public synchronized void setDeviationValue(int deviationValue) {
        this.deviationValue = deviationValue;
    }

    /**
     * @param deviationPercentage the deviationPercentage to set
     */
    public synchronized void setDeviationPercentage(int deviationPercentage) {
        this.deviationPercentage = deviationPercentage;
    }

    /**
     * @param pattern the pattern to set
     */
    public synchronized void setPattern(int[][] pattern) {
        this.pattern = pattern;
    }
    

}
