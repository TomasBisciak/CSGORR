/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.utils;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.ListView;

/**
 *
 * @author Kofola
 */
public class Utils {

    /**
     * Clones the provided array
     *
     * @param src
     * @return a new clone of the provided array
     */
    public static int[][] cloneArray(int[][] src) {

        int length = src.length;
        System.out.println("debug:LENGTH: " + length + " ,SRC[0] length:");
        int[][] target;
        if (length != 0) {
            target = new int[length][src[0].length];
            for (int i = 0; i < length; i++) {
                System.arraycopy(src[i], 0, target[i], 0, src[i].length);
            }
        } else {
            target=new int[0][0];
        }

        return target;
    }
    
        /**
     * Informs the ListView that one of its items has been modified.
     *
     * @param listView The ListView to trigger.
     * @param newValue The new value of the list item that changed.
     * @param i The index of the list item that changed.
     */    
    public static <T> void triggerUpdate(ListView<T> listView, T newValue, int i) {
        EventType<? extends ListView.EditEvent<T>> type = ListView.editCommitEvent();
        Event event = new ListView.EditEvent<>(listView, type, newValue, i);
        listView.fireEvent(event);
    }
    

}
