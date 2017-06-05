/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controls;

import csgorr.CsgoRr;
import csgorr.weapon.RecoilPattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author Kofola
 */
public class CustomWeaponRecoilRowListCell<T> extends ListCell<T> {

    private final StringBuilder sb = new StringBuilder();
    
    private int[] cachedT;

    public CustomWeaponRecoilRowListCell() {

        setOnDragDetected((MouseEvent event) -> {
            if (getItem() == null) {
                return;
            }
            ObservableList<T> items = (ObservableList<T>) getListView().getItems();

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            content.putString(CsgoRr.objectToJsonString(getItem()));
            cachedT=(int[]) getItem();
            System.out.println("cachedT :::"+cachedT[0]+" "+cachedT[1]+" "+cachedT[2]);

            dragboard.setDragView(textToImage(CsgoRr.objectToJsonString(getItem())));

            dragboard.setContent(content);
            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<T> items = (ObservableList<T>) getListView().getItems();

                System.out.println("debug db.getString(): "+db.getString());
                int draggedIdx = getListView().getSelectionModel().getSelectedIndex();
             
                //int draggedIdx = items.indexOf(jsonRowStringToT(db.getString()));
                System.out.println("DEBUG draggedIdx: "+draggedIdx);
                int thisIdx = items.indexOf(getItem());
                  System.out.println("DEBUG thisIdx: "+thisIdx);

                items.set(draggedIdx, getItem());
                items.set(thisIdx, jsonRowStringToT(db.getString()));
                
                

                List<T> itemscopy = new ArrayList<>(getListView().getItems());
                getListView().getItems().setAll(itemscopy);

                success = true;
                cachedT=null;
            }
            event.setDropCompleted(success);

            event.consume();
        });
        setOnDragDone((DragEvent event) -> {
            event.consume();
        });
        // setOnDragDone(DragEvent::consume);
    }

    private T jsonRowStringToT(String jsonRow) {//take care of possible exceptions

        String[] row = jsonRow.replace("]", "").replace("[", "").split(",");
        int[] parsedRow = new int[RecoilPattern.PATTERN_WIDTH];
        for (int i = 0; i < RecoilPattern.PATTERN_WIDTH; i++) {
            parsedRow[i] = Integer.valueOf(row[i]);
            System.out.println("debug parsed row:" + parsedRow[i]);
        }
        return (T) parsedRow;
    }

    @Override
    public void updateItem(T item, boolean empty) {
        int[] recoil = (int[]) item;
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            sb.append("[Number of steps:").append(recoil[0]).append(", Pixels per step:").
                    append(recoil[1]).append(", Direction:").append(RecoilPattern.directionToString(recoil[2])).append("]");

            setText(sb.toString());
            sb.delete(0, sb.length());
            sb.trimToSize();
        }
    }

    private static Image textToImage(String text) {
        Label label = new Label(text);

        label.setMinSize(80, 20);
        label.setMaxSize(80, 20);
        label.setPrefSize(80, 20);
        label.setStyle("-fx-background-color: white; -fx-text-fill:black;");
        label.setWrapText(true);
        Scene scene = new Scene(new Group(label));
        WritableImage img = new WritableImage(80, 20);
        scene.snapshot(img);
        return img;
    }

}
