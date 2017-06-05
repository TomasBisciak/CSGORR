/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgorr.db;

import csgorr.CsgoRr;
import csgorr.model.Loadout;
import csgorr.weapon.RecoilPattern;
import csgorr.weapon.Weapon;
import java.awt.AWTException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Static utility methods here for database communication and data retrieval
 *
 * @author Kofola
 */
public class DbUtil {

    public static void createTables() {
        new H2DatabaseConnector() {

            @Override
            public void execute() {//NO NEED TO THROW PROLLY.

                try {
                    if (getStatement().execute("CREATE TABLE IF NOT EXISTS weapons(weapon_id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,weapon_name VARCHAR(255) NOT NULL UNIQUE,pattern VARCHAR(10000) NOT NULL);"
                            + "CREATE TABLE IF NOT EXISTS loadouts(loadout_id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,loadout_name VARCHAR(255) NOT NULL UNIQUE,"//create table loadouts with fk of weapons 10 loadout slots
                            + "fk_weapon_1 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_2 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_3 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_4 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_5 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_6 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_7 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_8 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_9 BIGINT REFERENCES weapons(weapon_id),"
                            + "fk_weapon_10 BIGINT REFERENCES weapons(weapon_id));")) {
                        System.out.println("Table created");
                    } else {
                        System.out.println("Table is not created");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                closeConnection();
            }

        }.execute();
    }

    /**
     * Stores loadout into database and returns generated id for this entry
     *
     * @param loadout
     * @return generated id value for entry.
     */
    public static long storeLoadout(Loadout loadout) throws SQLException {

        return new H2DatabaseConnector() {

            @Override
            public <T> T executeRetrieve() throws SQLException {

                System.out.println("DEBUG DB:Going to store loadout:" + loadout + "\n");
                prepStat = getConnection().prepareStatement("INSERT INTO loadouts("
                        + "loadout_name,"
                        + "fk_weapon_1,"
                        + "fk_weapon_2,"
                        + "fk_weapon_3,"
                        + "fk_weapon_4,"
                        + "fk_weapon_5,"
                        + "fk_weapon_6,"
                        + "fk_weapon_7,"
                        + "fk_weapon_8,"
                        + "fk_weapon_9,"
                        + "fk_weapon_10,"
                        + ") VALUES(?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                prepStat.setString(1, loadout.getName());
                for (int i = 0; i < loadout.getWeaponIds().size(); i++) {
                    try {
                        System.out.println("Position prep stat :" + (i + 2) + " id:" + loadout.getWeaponIds().get(i));
                        prepStat.setLong(i + 2, loadout.getWeaponIds().get(i));
                    } catch (NullPointerException e) {
                        prepStat.setNull(i + 2, Types.NULL);
                    }
                }
                prepStat.executeUpdate();
                resultSet = prepStat.getGeneratedKeys();
                // prepStat.close();//gc colelcted afterwards anyway just not sure when exactly.
                //return id
                if (this.resultSet.next()) {
                    return (T) Long.valueOf(resultSet.getLong(1));
                }
                prepStat.close();

                return (T) new Long(-1);
            }

        }.executeRetrieve();

    }

    private static final int DB_INDEX_WEAPON_ID = 1;
    private static final int DB_INDEX_WEAPON_WEAPON_NAME = 2;
    private static final int DB_INDEX_WEAPON_PATTERN = 3;

    public static long storeWeapon(Weapon weapon) throws SQLException {
        return new H2DatabaseConnector() {

            @Override
            public <T> T executeRetrieve() throws SQLException {

                System.out.println("DEBUG DB:Going to store weapon:" + weapon + "\n");
                prepStat = getConnection().prepareStatement("INSERT INTO weapons("
                        + "weapon_name,"
                        + "pattern"
                        + ") VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
                prepStat.setString(1, weapon.getName());
                prepStat.setString(2, CsgoRr.objectToJsonString(weapon.getRecoilPattern()));
                prepStat.executeUpdate();
                resultSet = prepStat.getGeneratedKeys();

                //return id
                if (this.resultSet.next()) {
                    return (T) Long.valueOf(resultSet.getLong(1));
                }
                prepStat.close();
                return (T) new Long(-1);
            }

        }.executeRetrieve();

    }

    public static void updateWeapon(Weapon weapon) throws SQLException {
        System.out.println("Gonna update :" + weapon + "\n");

        new H2DatabaseConnector() {

            @Override
            public void execute() throws SQLException {

                //(weapon_id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,weapon_name VARCHAR(255) NOT NULL UNIQUE,pattern VARCHAR(255) NOT NULL);"
                prepStat = getConnection().prepareStatement("UPDATE weapons SET weapon_name=?,pattern=?"
                        + " WHERE weapon_id=?;");

                synchronized (weapon) {
                    prepStat.setString(1, weapon.getName());
                    prepStat.setString(2, CsgoRr.objectToJsonString(weapon.getRecoilPattern()));
                    prepStat.setLong(3, weapon.getId());
                }
                prepStat.execute();
                prepStat.close();
            }

        }.execute();

    }

    public static void updateLoadout(Loadout loadout) throws SQLException {
        System.out.println("Gonna update :" + loadout + "\n");

        new H2DatabaseConnector() {

            @Override
            public void execute() throws SQLException {

                /*
                 + "CREATE TABLE IF NOT EXISTS loadouts(loadout_id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,loadout_name VARCHAR(255) NOT NULL UNIQUE,"//create table loadouts with fk of weapons 10 loadout slots
                 + "fk_weapon_1 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_2 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_3 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_4 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_5 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_6 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_7 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_8 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_9 BIGINT REFERENCES weapons(weapon_id),"
                 + "fk_weapon_10 BIGINT REFERENCES weapons(weapon_id));")) {
                 */
                prepStat = getConnection().prepareStatement("UPDATE loadouts SET loadout_name=?,"
                        + "fk_weapon_1=?,"
                        + "fk_weapon_2=?,"
                        + "fk_weapon_3=?,"
                        + "fk_weapon_4=?,"
                        + "fk_weapon_5=?,"
                        + "fk_weapon_6=?,"
                        + "fk_weapon_7=?,"
                        + "fk_weapon_8=?,"
                        + "fk_weapon_9=?,"
                        + "fk_weapon_10=?"
                        + " WHERE loadout_id=?;");

                synchronized (loadout) {
                    prepStat.setString(1, loadout.getName());
//                        for(int i=2;i<12;i++){
//                            prepStat.setLong(i, loadout.getWeaponIds().ge);
//                        }
//                        prepStat.setLong(2, loadout.getWeaponIds().get(0));
//                        prepStat.setLong(3, loadout.getWeaponIds().get(1));
//                        prepStat.setLong(4, loadout.getWeaponIds().get(2));
//                        prepStat.setLong(5, loadout.getWeaponIds().get(3));
//                        prepStat.setLong(6, loadout.getWeaponIds().get(4));
//                        prepStat.setLong(7, loadout.getWeaponIds().get(5));
//                        prepStat.setLong(8, loadout.getWeaponIds().get(6));
//                        prepStat.setLong(9, loadout.getWeaponIds().get(7));
//                        prepStat.setLong(10, loadout.getWeaponIds().get(8));
//                        prepStat.setLong(11, loadout.getWeaponIds().get(9));

                    for (int i = 2; i < 12; i++) {
                        try {
                            System.out.println("Position prep stat :" + (i) + " id:" + loadout.getWeaponIds().get(i - 2));
                            prepStat.setLong(i, loadout.getWeaponIds().get(i - 2));
                        } catch (NullPointerException e) {
                            prepStat.setNull(i, Types.NULL);
                        }
                    }

                    prepStat.setLong(12, loadout.getId());
                }
                prepStat.execute();
                prepStat.close();
            }

        }.execute();

    }

    public static List<Weapon> getWeapons() {
        return new H2DatabaseConnector() {

            List<Weapon> ret = new ArrayList<>();

            @Override
            public <T extends Iterable> T executeRetrieveIterable() {
                try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM weapons;")) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Weapon weapon;
                            try {
                                weapon = Weapon.createWeapon(rs.getString(DB_INDEX_WEAPON_WEAPON_NAME),
                                        CsgoRr.jsonStringToObject(rs.getString(DB_INDEX_WEAPON_PATTERN), RecoilPattern.class), rs.getLong(DB_INDEX_WEAPON_ID));
                                ret.add(weapon);
                            } catch (AWTException ex) {
                                Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            //System.out.println("DEBUG getDailyStats DailyStat: " + ds);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                closeConnection();
                return (T) ret;
            }

        }.executeRetrieveIterable();
    }

    private static final int DB_INDEX_LOADOUT_ID = 1;
    private static final int DB_INDEX_LOADOUT_NAME = 2;
    private static final int DB_INDEX_LOADOUT_SLOT1 = 3;
    private static final int DB_INDEX_LOADOUT_SLOT2 = 4;
    private static final int DB_INDEX_LOADOUT_SLOT3 = 5;
    private static final int DB_INDEX_LOADOUT_SLOT4 = 6;
    private static final int DB_INDEX_LOADOUT_SLOT5 = 7;
    private static final int DB_INDEX_LOADOUT_SLOT6 = 8;
    private static final int DB_INDEX_LOADOUT_SLOT7 = 9;
    private static final int DB_INDEX_LOADOUT_SLOT8 = 10;
    private static final int DB_INDEX_LOADOUT_SLOT9 = 11;
    private static final int DB_INDEX_LOADOUT_SLOT10 = 12;

    public static List<Loadout> getLoadouts() {
        return new H2DatabaseConnector() {

            List<Loadout> ret = new ArrayList<>();

            @Override
            public <T extends Iterable> T executeRetrieveIterable() {
                try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM loadouts;")) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Loadout loadout;
                            loadout = new Loadout(new Long[]{
                                rs.getLong(DB_INDEX_LOADOUT_SLOT1),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT2),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT3),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT4),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT5),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT6),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT7),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT8),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT9),
                                rs.getLong(DB_INDEX_LOADOUT_SLOT10),},
                                    rs.getString(DB_INDEX_LOADOUT_NAME), rs.getLong(DB_INDEX_LOADOUT_ID));

                            System.out.println("Loadout in db id :" + loadout.getId());
                            ret.add(loadout);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                closeConnection();
                return (T) ret;
            }

        }.executeRetrieveIterable();
    }

    public static final void removeLoadout(Loadout loadout) throws SQLException {

        new H2DatabaseConnector() {

            @Override
            public void execute() throws SQLException {
                try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM loadouts WHERE loadout_id=?")) {
                    stmt.setLong(1, loadout.getId());
                    stmt.executeUpdate();
                    stmt.close();
                } catch (SQLException e) {
                    throw e;
                }

            }

        }.execute();

    }

    public static final void removeWeapon(Weapon loadout) throws SQLException {

        new H2DatabaseConnector() {

            @Override
            public void execute() throws SQLException {
                try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM weapons WHERE weapon_id=?")) {
                    stmt.setLong(1, loadout.getId());
                    stmt.executeUpdate();
                    stmt.close();
                } catch (SQLException e) {
                    throw e;
                }

            }

        }.execute();

    }

}
