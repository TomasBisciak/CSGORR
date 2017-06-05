/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.weapon;

import csgorr.CsgoRr;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

/**
 *
 * @author Kofola
 */
public class WeaponRobot extends Robot {

    //reference 
    private final Weapon weapon;

    public WeaponRobot(Weapon weapon) throws AWTException {
        this.weapon = weapon;
    }

 
    @SuppressWarnings("CallToPrintStackTrace")
    public void reduceRecoil() {

        @SuppressWarnings("UnusedAssignment")
        int directionFlag = 0;
        int nextDirectionFlag = RecoilPattern.NONE;

        for (int row = 0; row < weapon.getRecoilPattern().getPattern().length; row++) {

            if (weapon.isTriggerPulled()) {

                directionFlag = weapon.getRecoilPattern().getPattern()[row][2];
                try {
                    nextDirectionFlag = weapon.getRecoilPattern().getPattern()[row + 1][2];
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                //time taken gonna be divideed by this 
                int pixelsPerStep = weapon.getRecoilPattern().getPattern()[row][1];//move by pixels //possibly fixed
                System.out.println("DEBUG:Moving by:"+pixelsPerStep);
                
                //taken care of sensitivity
                int numberOfPixels = (int)(weapon.getRecoilPattern().getPattern()[row][0]/CsgoRr.getModel().getAppPrefs().getIngameSensitivity());
                int delay =(int) (2 * CsgoRr.getModel().getAppPrefs().getIngameSensitivity()); // increments of 1 1.5 2 2.5 etc
                

                //put to function if needed
                switch (directionFlag) {
                    case (RecoilPattern.DOWN): {
                         for (int i = 0; i < numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x,
                                            MouseInfo.getPointerInfo().getLocation().y + pixelsPerStep, RecoilPattern.DOWN, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x,
                                            MouseInfo.getPointerInfo().getLocation().y + pixelsPerStep);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }
                    case (RecoilPattern.DOWN | RecoilPattern.LEFT): {
                         for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x - pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y + pixelsPerStep, RecoilPattern.DOWN | RecoilPattern.LEFT, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x - pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y + pixelsPerStep);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }
                    case (RecoilPattern.DOWN | RecoilPattern.RIGHT): {
                        for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x + pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y + pixelsPerStep, RecoilPattern.DOWN | RecoilPattern.RIGHT, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x + pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y + pixelsPerStep);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }
                    case (RecoilPattern.LEFT): {
                         for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x - pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y, RecoilPattern.LEFT, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x - pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }
                    case (RecoilPattern.RIGHT): {
                         for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x + pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y, RecoilPattern.RIGHT, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x + pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }

                    case (RecoilPattern.UP): {
                         for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x,
                                            MouseInfo.getPointerInfo().getLocation().y - pixelsPerStep, RecoilPattern.UP, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x,
                                            MouseInfo.getPointerInfo().getLocation().y - pixelsPerStep);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }
                    case (RecoilPattern.UP | RecoilPattern.LEFT): {
                        for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x - pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y - pixelsPerStep, RecoilPattern.UP | RecoilPattern.LEFT, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x - pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y - pixelsPerStep);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }
                    case (RecoilPattern.UP | RecoilPattern.RIGHT): {
                         for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                if (weapon.getRecoilPattern().getDeviationValue() != 0) {
                                    smoothMouseMove(MouseInfo.getPointerInfo().getLocation().x + pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y - pixelsPerStep, RecoilPattern.UP | RecoilPattern.RIGHT, nextDirectionFlag, i, numberOfPixels);
                                } else {
                                    mouseMove(MouseInfo.getPointerInfo().getLocation().x + pixelsPerStep,
                                            MouseInfo.getPointerInfo().getLocation().y - pixelsPerStep);
                                }

                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                        break;
                    }
                    default: {//RecoilPatternInfo.NONE
                        for (int i = 0; i <= numberOfPixels; i+=pixelsPerStep) {
                            if (weapon.isTriggerPulled()) {
                                mouseMove(MouseInfo.getPointerInfo().getLocation().x,
                                        MouseInfo.getPointerInfo().getLocation().y);
                            } else {
                                flushMove();
                                return;
                            }
                            this.delay(delay);
                        }
                    }
                }
            }
        }
    }

    private final int LINE_SMOOTHNESS_END = 0;
    private final int LINE_SMOOTHNESS_NONE = -1;

    private int diviateAtStep = 0;
    private boolean diviationPending;

    private int xdiviation = 0;
    private int ydiviation = 0;

    public synchronized void smoothMouseMove(int x, int y, int currentMovement, int nextMovement, int currentPixelMove, int numOfPixels) {

        int stepsLeft = numOfPixels - currentPixelMove;//100-80
        int smoothoutPoint = (int) (numOfPixels * (weapon.getRecoilPattern().getSmoothnessFactor() / 100.0f));//start and end smoothout points based on percentage
        System.out.println("DEBUG: current iteration :" + currentPixelMove + " out of :" + numOfPixels + "  SMOOTHING POINT:" + smoothoutPoint);
        int smoothnessFlag;

        if (currentPixelMove >= (numOfPixels - smoothoutPoint)) {
            smoothnessFlag = LINE_SMOOTHNESS_END;
            System.out.println("DEBUG: SMOOTHNESS FLAG:" + smoothnessFlag);
        } else {
            smoothnessFlag = LINE_SMOOTHNESS_NONE;
            System.out.println("DEBUG: SMOOTHNESS FLAG:" + smoothnessFlag);
        }

        if ((smoothnessFlag == LINE_SMOOTHNESS_END) && (nextMovement != currentMovement)) {
            if ((nextMovement & RecoilPattern.LEFT) == RecoilPattern.LEFT) {//contains left
                xdiviation = (weapon.getRecoilPattern().getDeviationValue() * -1);
            } else if ((nextMovement & RecoilPattern.RIGHT) == RecoilPattern.RIGHT) {//contains right
                xdiviation = weapon.getRecoilPattern().getDeviationValue();
            }

            if ((nextMovement & RecoilPattern.UP) == RecoilPattern.UP) {//contains UP
                ydiviation = (weapon.getRecoilPattern().getDeviationValue() * -1);
            } else if ((nextMovement & RecoilPattern.DOWN) == RecoilPattern.DOWN) {//contains DOWN
                ydiviation = weapon.getRecoilPattern().getDeviationValue();
            }
        }

        if (!diviationPending) {
            diviateAtStep = (int) (numOfPixels - (stepsLeft * (weapon.getRecoilPattern().getDeviationPercentage() / 100.0f)));//60 percent // deviation percentage
            System.out.println("DEBUG2: DIVIATE AT STEP:" + diviateAtStep);
            diviationPending = true;
            mouseMove(x, y);
        }

        if (diviationPending && currentPixelMove >= diviateAtStep) {
            System.out.println("DEBUG2: DIVIATING");
            mouseMove(x + xdiviation, y + ydiviation);
            flushMove();
        } else {
            mouseMove(x, y);
        }

    }

    private synchronized void flushMove() {
        diviateAtStep = 0;
        diviationPending = false;
        xdiviation = 0;
        ydiviation = 0;
    }

}
