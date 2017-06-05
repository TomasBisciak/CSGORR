/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.utils;

/**
 *
 * @author Kofola
 */
public class IntegerMutable {
    
    private int value;
    
    public IntegerMutable(int value){
        this.value=value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
    
     public void setValue(IntegerMutable value) {
        this.value = value.getValue();
    }
    
}
