/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr;

import com.google.gson.Gson;
import csgorr.controllers.MainViewController;
import csgorr.controls.TransparentWindowNotif;
import csgorr.db.DbUtil;
import csgorr.model.Loadout;
import csgorr.model.MainModel;
import csgorr.nativeHook.GlobalKeyListener;
import csgorr.nativeHook.GlobalMouseListener;
import csgorr.utils.Info;
import csgorr.weapon.Weapon;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 *
 * @author Kofola
 */
public class CsgoRr extends Application {

    private static GlobalMouseListener globalMouseListener;
    private static GlobalKeyListener globalKeyListener;

    private static MainModel model;

    //make sure only one instance of application is running on the machine/can be bypassed by params
    private static ServerSocket socket;
    private static Tray tray;

    private static MainViewController mainViewController;

    /**
     * Currently used Application port
     */
    private static int appPort;

    /**
     * Reusable primary stage for window
     */
    public static Stage primStage;

    public static MainViewController getMainViewController() {
        return mainViewController;
    }

    private static final Gson gson = new Gson();

    public static String objectToJsonString(Object object) {
        synchronized (gson) {
            return gson.toJson(object);
        }
    }

    public static <T> T jsonStringToObject(String jsonString, Class cls) {
        synchronized (gson) {
            return (T) gson.fromJson(jsonString, cls);
        }
    }

    private static volatile boolean isTWNShown;
    private static volatile boolean isPending;

    @SuppressWarnings("SleepWhileInLoop")
    public static void showTransparentWindowNotif(String text, int divx, int divy, String img, long delay) {

        new Thread(() -> {
            while (true) {
                if (isTWNShown) {
                    isPending = true;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    isTWNShown = true;
                    TransparentWindowNotif twn = new TransparentWindowNotif(text, divx, divy, img);
                    if ((delay % 200) != 0) {
                        for (int i = 0; i < delay / 200 && !isPending; i++) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        for (int i = 0; i < TransparentWindowNotif.DEFAULT_TIMEOUT / 200 && !isPending; i++) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    twn.setVisible(false);
                    isTWNShown = false;
                    isPending = false;
                    return;
                }

            }
        }).start();

    }

    private static volatile boolean isTWNShownW;
    private static volatile boolean isPendingW;

    @SuppressWarnings("SleepWhileInLoop")
    public static void showTransparentWindowNotifW(String text, int divx, int divy, String img, long delay) {

        new Thread(() -> {
            while (true) {
                if (isTWNShownW) {
                    isPendingW = true;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    isTWNShownW = true;
                    TransparentWindowNotif twn = new TransparentWindowNotif(text, divx, divy, img);
                    if ((delay % 200) != 0) {
                        for (int i = 0; i < delay / 200 && !isPending; i++) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        for (int i = 0; i < TransparentWindowNotif.DEFAULT_TIMEOUT / 200 && !isPending; i++) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    twn.setVisible(false);
                    isTWNShownW = false;
                    isPendingW = false;
                    return;
                }

            }
        }).start();

    }

    public static MainModel getModel() {
        return model;
    }

    //MAIN THREAD Logic executor. FIFO
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void runLater(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * @param stage
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {// JFXAT
        System.out.println("Start called");

        if (!model.getAppPrefs().getPreferences().getBoolean(AppPreferences.PREFS_SHOW_MAIN_WIN, false)) {
            showMainWindow();
        }
        tray = new Tray(primStage, Info.Resource.IMG_CSGORR_16x16);

        //logic thread afterwards
        Thread logicThread = new Thread(() -> {
            //TEST
            System.out.println("DEBUG:TEST");
            //  getModel().setActiveWeapon(DbUtil.getWeapons().get(1));
        });
        logicThread.setPriority(Thread.MAX_PRIORITY);
        logicThread.start();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        processArguments(args);
        launch(args);//must be last otherwise functions are not getting called after it 

    }

    private static BorderPane loadMainPane() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setController(mainViewController);
        BorderPane mainPane = (BorderPane) loader.load(
                CsgoRr.class
                .getResourceAsStream(Info.Resource.FXML_FILE_MAIN));
        mainPane.getStylesheets().add(CsgoRr.class.getResource("/csgorr/css/style.css").toString());

        return mainPane;
    }

    private static void initNativeHook() {
        Thread nativeHookThread = new Thread(() -> {
            globalMouseListener = new GlobalMouseListener();
            globalKeyListener = new GlobalKeyListener();
            try {
                GlobalScreen.registerNativeHook();
                System.out.println("Hook state: " + GlobalScreen.isNativeHookRegistered());
            } catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());
                System.exit(1);
            }

            // Construct the example object.
            // Add the appropriate listeners.
            GlobalScreen.addNativeMouseListener(getGlobalMouseListener());
            GlobalScreen.addNativeMouseMotionListener(getGlobalMouseListener());
            GlobalScreen.addNativeKeyListener(getGlobalKeyListener());

            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            System.out.println("Nativehook login disabled");
            System.out.println("Global hooks added.");
        });
        nativeHookThread.setName("Native Hook thread.");
        nativeHookThread.setDaemon(true);
        nativeHookThread.start();

    }

    public static Tray getTray() {
        return tray;
    }

    private static boolean isShown;

    public static void showMainWindow() {
        if (isShown) {

        } else {

            primStage = new Stage();
            primStage.setTitle("CSMP - v" + Info.APP_VERSION + " beta");
            primStage
                    .getIcons().add(new Image(CsgoRr.class
                                    .getResourceAsStream(Info.Resource.IMG_CSGORR)));

            try {

                Scene scene = new Scene(loadMainPane());
                primStage.setScene(scene);
                //primStage.setScene(new Scene((BorderPane) FXMLLoader.load(CsgoRr.class.getResource(Info.Resource.FXML_FILE_MAIN))));

            } catch (IOException ex) {
                System.out.println("Problem at show main window");
            }

            primStage.setOnCloseRequest((WindowEvent event) -> {
                tray.showMessage("Application still active.", TrayIcon.MessageType.INFO);
                isShown = false;
            });

            primStage.show();
            isShown = true;

        }

    }

    public static final void removeNativeListeners() throws NativeHookException {
        GlobalScreen.removeNativeKeyListener(getGlobalKeyListener());
        GlobalScreen.removeNativeMouseListener(getGlobalMouseListener());
        GlobalScreen.unregisterNativeHook();
    }

    /**
     * @return the globalMouseListener
     */
    public static GlobalMouseListener getGlobalMouseListener() {
        return globalMouseListener;
    }

    /**
     * @return the globalKeyListener
     */
    public static GlobalKeyListener getGlobalKeyListener() {
        return globalKeyListener;
    }

    @Override
    public void init() throws Exception {
        System.out.println("init called");
        super.init(); //To change body of generated methods, choose Tools | Templates.
        initNativeHook();
        shutdownHook();

        DbUtil.createTables();//if exists is applied in a query
        model = new MainModel();

        Files.createDirectories(Paths.get(Info.DIR_WEP_EX));

        //TESTING
        //insert default
        try {
            DbUtil.storeWeapon(Weapon.defaultWeapons.get(0));
            DbUtil.storeWeapon(Weapon.defaultWeapons.get(1));
            DbUtil.storeWeapon(Weapon.defaultWeapons.get(2));

            for (Weapon weapon : DbUtil.getWeapons()) {
                System.out.println("DEBUG :DB LOAD weapons:" + weapon.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DbUtil.storeLoadout(new Loadout(new Long[]{1l,
                2l,
                3l,
                null,
                null,
                null,
                null,
                null,
                null,
                null}, "default_CSGO_sens2"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        getModel().loadLoadoutCache();
        getModel().loadWeaponCache();
        //END TESTING----------

        mainViewController = new MainViewController();

//        if (!model.getAppPrefs().getPreferences().getBoolean(AppPreferences.PREFS_SHOW_MAIN_WIN, false)) {
//            //TODO  check is its needed to be on JFXAT
//            Platform.runLater(() -> {
//                showMainWindow();
//            });
//
//        }
//        tray = new Tray(primStage, Info.Resource.IMG_CSGORRL_16x16);
        // mainViewController=new MainViewController();
    }

    private static void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                //execute before shutdown
                System.out.println("ShutdownHook execution:");
                removeNativeListeners();
                System.out.println("ShutdownHook executed.");
            } catch (NativeHookException ex) {
                Logger.getLogger(CsgoRr.class.getName()).log(Level.SEVERE, null, ex);
            }

        }));
    }

    /**
     * Opens a ServerSocket for OpenChannel aplication making sure that only one instance is running at the time, if not specified otherwise by providing specified ports that differ from each other and fall within range.
     *
     * @param port port to be occupied by application valid range 49152-65535
     */
    private static boolean occupyPort(int port) {

        if (!(port >= 49152 && port <= 65535)) { //(IANA) suggested range
            System.err.println("Not a valid port number.");
            return false;
        }
        try {
            socket = new ServerSocket(port, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
            appPort = port;
        } catch (IOException ex) {
            System.err.println("Application already running/Port occupied by a process.");
            ex.printStackTrace();
            //TODO popup inform user
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }
            System.exit(1);
        }
        return false;
    }

    /**
     *
     * @param args passed into application
     */
    private static void processArguments(String[] args) {
        //holds which values were sucessfully parsed/used
        boolean[] paramFlags = new boolean[2];
        if (args.length != 0) {

            //switch case to check parameters
            for (int i = 0; i < args.length; i += 2) {

                switch (args[i]) {
                    case "-p": {
                        try {
                            if (!occupyPort(Integer.valueOf(args[i + 1]))) {
                                System.out.println("Port" + Integer.valueOf(args[i + 1]) + " cannot be occupied by application");
                                continue;
                            }
                            paramFlags[0] = true;
                        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                            System.err.println("Argument is not a valid number/argument not found");
                        }
                        break;
                    }
                    case "": {

                        break;
                    }
                    default: {
                        System.out.println("Invalid parameter \" " + args[i] + " \",use default execution? Y/N:");
                        if (new Scanner(System.in).nextLine().equalsIgnoreCase("y")) {

                        }
                    }

                }

            }

        } else {
            //use default values
            for (int i = 0; i < paramFlags.length; i++) {
                switch (i) {
                    case 0: {
                        if (!paramFlags[i]) {
                            occupyPort(Info.DEFAULT_APP_PORT);
                        }
                        break;
                    }
                    case 1: {

                        break;
                    }

                }

            }

        }

    }

}
