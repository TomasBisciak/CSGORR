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
public class Info {

    public static final String changeLog="no available changelog.";
    
    public static final double APP_VERSION = 0.5;
    public static final int DEFAULT_APP_PORT = 64878;

    public static final String DB_DEFAULT_USERNAME = "csgorr";
    public static final String DB_DEFAULT_PASSWORD = "csgorr_password";
    public static final String DIR_WEP_EX = "./weapon_exports/";

    public static class Resource {

        public static final String FXML_PREFIX = "/csgorr/view/fxml/";
        public static final String FXML_FILE_MAIN = FXML_PREFIX + "MainView.fxml";
        public static final String FXML_FILE_PREFERENCES = FXML_PREFIX + "PreferencesView.fxml";
        public static final String FXML_FILE_PRODUCT_PAGE = FXML_PREFIX + "ProductPageView.fxml";
        public static final String FXML_FILE_ABOUT = FXML_PREFIX + "AboutView.fxml";
        public static final String FXML_FILE_CHANGELOG = FXML_PREFIX + "ChangelogView.fxml";
        public static final String FXML_FILE_LOADOUT = FXML_PREFIX + "LoadoutView.fxml";
        public static final String FXML_FILE_WEAPON = FXML_PREFIX + "WeaponView.fxml";
        public static final String FXML_FILE_DATABASE = FXML_PREFIX + "DatabaseView.fxml";

        public static final String IMG_ALERT_INFO = "/csgorr/resources/images/ALERT_INFO.png";
        public static final String IMG_ALERT_ERROR = "/csgorr/resources/images/ALERT_ERROR.png";
        public static final String IMG_ALERT_FAILURE = "/csgorr/resources/images/ALERT_FAILURE.png";

        public static final String IMG_INCOGNITO_MODE = "/csgorr/resources/images/glasses.png";

        //images
        public static final String IMG_CSGORR = "/csgorr/resources/images/recoil.png";
        public static final String IMG_CSGORR_16x16 = "/csgorr/resources/images/recoil_16x16.png";
    }

    public static class PreferenceData {

    }

    public static class Db {

    }

}
