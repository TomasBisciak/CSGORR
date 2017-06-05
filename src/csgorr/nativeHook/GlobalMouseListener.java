/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.nativeHook;

import csgorr.CsgoRr;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

/**
 *
 * @author Kofola
 */
public class GlobalMouseListener implements NativeMouseInputListener {

    


    public GlobalMouseListener() {
        System.out.println("DEBUG: thread name:" + Thread.currentThread().getName());
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
    }
    
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
       
        if (e.getButton() == CsgoRr.getModel().getAppPrefs().getKeyshoot()&&CsgoRr.getModel().isRrEnabled()&&CsgoRr.getModel().getActiveWeapon()!=null) {
            System.out.println("DEBUG-KEY SHOT PRESSED");
            try {
                Thread.sleep(CsgoRr.getModel().getAppPrefs().getPreTriggerTimeout());
            } catch (InterruptedException ex) {
                Logger.getLogger(GlobalMouseListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("KEY SHOOT =TRUE");
            CsgoRr.getModel().getActiveWeapon().setTriggerPulled(true);
            CsgoRr.getModel().getActiveWeapon().shoot();
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
     
        if (e.getButton() == CsgoRr.getModel().getAppPrefs().getKeyshoot()&&CsgoRr.getModel().isRrEnabled()&&CsgoRr.getModel().getActiveWeapon()!=null) {
            System.out.println("KEY SHOOT =FALSE");
            CsgoRr.getModel().getActiveWeapon().setTriggerPulled(false);
        }
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
       //System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        
    }

}
