/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.controllers;

import csgorr.utils.Info;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Kofola
 */
public class ProductPageController implements Initializable {

    @FXML
    private WebView webViewProduct;
    @FXML
    private Button btnOpenInBrowser;

    
    public ProductPageController(){
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = webViewProduct.getEngine();
        webEngine.load("https://www.google.sk/");
    }

    @FXML
    private void openInBrowserOnAction() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://google.com"));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(ProductPageController.class.getName()).log(Level.SEVERE, null, ex);
                MainViewController.showPaneNotification("Error.",  Info.Resource.IMG_ALERT_ERROR);
            }
        }
    }

}
