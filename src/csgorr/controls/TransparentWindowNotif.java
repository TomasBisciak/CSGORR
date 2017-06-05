/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controls;

import csgorr.CsgoRr;
import csgorr.utils.Info;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Window;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class TransparentWindowNotif {

    private final Window w;
    private String notification;

    public static final int DEFAULT_TIMEOUT = 2000;

    public TransparentWindowNotif(String text, int divx, int divy, String img) {
        notification = text;

        w = new Window(null) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Font font = getFont().deriveFont(20.0F);
                g.setColor(Color.BLACK);

                FontMetrics metrics = g.getFontMetrics();
                g.setFont(font);
                g.drawString(notification, ShiftWest((getWidth() - metrics.stringWidth(notification)) / divx, 1), ShiftNorth((getHeight() - metrics.getHeight()) / divy, 1));
                g.drawString(notification, ShiftWest((getWidth() - metrics.stringWidth(notification)) / divx, 1), ShiftSouth((getHeight() - metrics.getHeight()) / divy, 1));
                g.drawString(notification, ShiftEast((getWidth() - metrics.stringWidth(notification)) / divx, 1), ShiftNorth((getHeight() - metrics.getHeight()) / divy, 1));
                g.drawString(notification, ShiftEast((getWidth() - metrics.stringWidth(notification)) / divx, 1), ShiftSouth((getHeight() - metrics.getHeight()) / divy, 1));
                g.setColor(Color.WHITE);
                g.drawString(notification, (getWidth() - metrics.stringWidth(notification)) / divx, (getHeight() - metrics.getHeight()) / divy);

                if (img != null) {
                    try {
                        g.drawImage(ImageIO.read(CsgoRr.class.getResourceAsStream(Info.Resource.IMG_CSGORR)),
                                (getWidth() + metrics.stringWidth(notification) + 10) / divx, (getHeight() + metrics.getHeight()) / divy, 100, 50, null);
                    } catch (IOException ex) {
                        Logger.getLogger(TransparentWindowNotif.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

            int ShiftNorth(int p, int distance) {
                return (p - distance);
            }

            int ShiftSouth(int p, int distance) {
                return (p + distance);
            }

            int ShiftEast(int p, int distance) {
                return (p + distance);
            }

            int ShiftWest(int p, int distance) {
                return (p - distance);
            }
        };

        w.setAlwaysOnTop(
                true);
        w.setBounds(w.getGraphicsConfiguration().getBounds());
        w.setBackground(
                new Color(0, true));
        w.setFocusable(false);
        w.setVisible(true);

    }

    public void setVisible(boolean visibility) {
        w.setVisible(visibility);
    }

}
