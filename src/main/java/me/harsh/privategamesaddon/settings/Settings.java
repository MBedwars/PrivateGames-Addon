package me.harsh.privategamesaddon.settings;


import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

    // Menu title, Prefix, Booleans
    public static String PREFIX;
    public static String MENU_TITLE;
    public static Boolean SHOULD_SAVE_STATS;

    // Sub menus
    public static String HEALTH_BUFF_MENU;
    public static String SPEED_BUFF_MENU;
    public static String RESPAWN_BUFF_MENU;

    // Buttons
    public static String ONE_HIT_BUFF;
    public static String HEALTH_BUFF;
    public static String LOW_GRAVITY_BUFF;
    public static String SPEED_BUFF;
    public static String DISABLE_BLOCK_PROTECTION_BUFF;
    public static String RESPAWN_TIME_BUFF;
    public static String SPAWN_RATE_MUTIPLIER_BUFF;

    // Permisions
    public static String CREATE_PERM;
    public static String GLOBAL_PERM;

    // Placeholders
    public static String IS_PRIVATE_GAME;
    @Override
    protected int getConfigVersion() {
        return 1;
    }

    private static void init(){
        PREFIX = getString("Prefix");
        SHOULD_SAVE_STATS = getBoolean("Save_Stats");
        pathPrefix("Menu");
        MENU_TITLE = getString("Title");
        pathPrefix("Menu.Sub_Menu");
        HEALTH_BUFF_MENU = getString("Health");
        SPEED_BUFF_MENU = getString("Speed");
        RESPAWN_BUFF_MENU = getString("Respawn");
        pathPrefix("Menu.Buffs");
        ONE_HIT_BUFF = getString("One_Hit_Buff");
        HEALTH_BUFF = getString("Health_Buff");
        LOW_GRAVITY_BUFF = getString("Low_Gravity_Buff");
        SPEED_BUFF = getString("Speed_Buff");
        DISABLE_BLOCK_PROTECTION_BUFF = getString("Disable_Block_Protection_Buff");
        RESPAWN_TIME_BUFF = getString("Respawn_Time_Buff");
        SPAWN_RATE_MUTIPLIER_BUFF = getString("Spawn_Rate_Mutiplier_Buff");
        pathPrefix("Perms");
        CREATE_PERM = getString("create_perm");
        GLOBAL_PERM = getString("global_perm");
        pathPrefix("Placeholders");
        IS_PRIVATE_GAME = getString("is_private_game");
    }
}
