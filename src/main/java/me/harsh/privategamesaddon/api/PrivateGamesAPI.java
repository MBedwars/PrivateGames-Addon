package me.harsh.privategamesaddon.api;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.hook.PartiesHook.Party;
import de.marcely.bedwars.api.player.PlayerDataAPI;
import de.marcely.bedwars.api.player.PlayerProperties;
import de.marcely.bedwars.api.remote.RemoteArena;
import de.marcely.bedwars.tools.Validate;
import java.util.function.Consumer;
import me.harsh.privategamesaddon.PrivateGamesPlugin;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.Nullable;

/**
 * Base API class for the Private Games Addon.
 */
public class PrivateGamesAPI {

    private static final PrivateGamesAPI INSTANCE = new PrivateGamesAPI();

    private PrivateGamesAPI() { }

    /**
     * Get whether the arena is set to private.
     * <p>
     *   A private arena only allows players from a specific party to join.
     * </p>
     *
     * @param arena The arena to check
     * @return <code>true</code> if the arena is private, <code>false</code> otherwise
     * @see #isPrivateArena(RemoteArena)
     */
    public boolean isPrivateArena(Arena arena) {
        Validate.notNull(arena, "arena");

        return Utility.getManager().isPrivateArena(arena);
    }

    /**
     * Get whether the arena is set to private.
     * <p>
     *   A private arena only allows players from a specific party to join.
     * </p>
     * <p>
     *   Due to the nature of RemoteArenas, it may be slightly desynced compared
     *   to the local Arena instance ({@link #isPrivateArena(Arena)}).
     * </p>
     *
     * @param arena The arena to check
     * @return <code>true</code> if the arena is private, <code>false</code> otherwise
     * @see #isPrivateArena(Arena)
     */
    public boolean isPrivateArena(RemoteArena arena) {
        Validate.notNull(arena, "arena");

        return Utility.getManager().isPrivateArena(arena);
    }

    /**
     * Get the party managing the private arena.
     *
     * @param arena The arena to check
     * @return The managing party, or <code>null</code> if the arena is not private
     * @see #isPrivateArena(Arena)
     */
    @Nullable
    public Party getManagingParty(Arena arena) {
        Validate.notNull(arena, "arena");

        return Utility.getManager().getManagingParty(arena);
    }

    /**
     * Unset the private status of an arena.
     * <p>
     *   With that, anyone can join the arena again and
     *   buff effects will be removed.
     * </p>
     *
     * @param arena The arena to unset
     * @see #setPrivateArena(Arena, Party)
     */
    public void unsetPrivateArena(Arena arena) {
        Validate.notNull(arena, "arena");

        Utility.getManager().unsetPrivateArena(arena);
    }

    /**
     * Set an arena to private for a specific party.
     * <p>
     *   Does not automatically kick players not part of the party.
     * </p>
     *
     * @param arena The arena to set
     * @param party The party allowed to join
     * @see #unsetPrivateArena(Arena)
     */
    public void setPrivateArena(Arena arena, Party party) {
        Validate.notNull(arena, "arena");
        Validate.notNull(party, "party");

        Utility.getManager().setPrivateArena(arena, party);
    }

    /**
     * Get the buff state for a specific arena.
     *
     * @param arena The arena to get the buff state for
     * @return The arena buff state, or <code>null</code> if the arena is not private
     */
    @Nullable
    public ArenaBuffState getBuffState(Arena arena) {
        Validate.notNull(arena, "arena");

        return Utility.getManager().getBuffState(arena);
    }

    /**
     * Check if a player has private mode enabled.
     * <p>
     *   May not be absolutely accurate in certain situations if player is not
     *   online, but should be fine for most use-cases.
     * </p>
     *
     * @param playerProps The player properties to check
     * @return <code>true</code> if private mode is enabled, <code>false</code> otherwise
     */
    public boolean hasPrivateModeEnabled(PlayerProperties playerProps) {
        Validate.notNull(playerProps, "playerProps");

        return Utility.getManager().getPlayerPrivateMode(playerProps, Bukkit.getPlayer(playerProps.getPlayerUUID()));
    }

    /**
     * Check if a player has private mode enabled.
     * <p>
     *   Callback is executed on the server's main thread.
     * </p>
     *
     * @param player The player to check
     * @param callback The callback to execute with the result
     */
    public void hasPrivateModeEnabled(Player player, Consumer<Boolean> callback) {
        Validate.notNull(player, "player");
        Validate.notNull(callback, "callback");

        PlayerDataAPI.get().getProperties(player, props -> {
            callback.accept(Utility.getManager().getPlayerPrivateMode(props, player));
        });
    }

    /**
     * Enable or disable private mode for a player.
     * <p>
     *   {@link #hasPrivateModeEnabled(PlayerProperties)} might return <code>false</code>
     *   regardless it is enabled if the player does not have the permission
     *   (see {@link #hasCreatePermission(Permissible)}).
     * </p>
     *
     * @param playerProps The player properties to set
     * @param newState <code>true</code> to enable, <code>false</code> to disable
     */
    public void setPrivateModeEnabled(PlayerProperties playerProps, boolean newState) {
        Validate.notNull(playerProps, "playerProps");

        Utility.getManager().setPrivateGameMode(playerProps, newState);
    }

    /**
     * Enable or disable private mode for a player.
     * <p>
     *   {@link #hasPrivateModeEnabled(Player, Consumer)} might return <code>false</code>
     *   regardless it is enabled if the player does not have the permission
     *   (see {@link #hasCreatePermission(Permissible)}).
     * </p>
     * <p>
     *   Callback is executed on the server's main thread.
     * </p>
     *
     * @param player The player to set
     * @param newState <code>true</code> to enable, <code>false</code> to disable
     * @param callback The callback to execute after setting (can be <code>null</code> if not needed)
     */
    public void setPrivateModeEnabled(Player player, boolean newState, @Nullable Runnable callback) {
        Validate.notNull(player, "player");

        PlayerDataAPI.get().getProperties(player, props -> {
            Utility.getManager().setPrivateGameMode(props, newState);

            if (callback != null)
                callback.run();
        });
    }

    /**
     * Check if a player has permission to create private games.
     *
     * @param player The player to check
     * @return <code>true</code> if the player has permission, <code>false</code> otherwise
     */
    public boolean hasCreatePermission(Permissible player) {
        Validate.notNull(player, "player");

        return Utility.getManager().hasCreatePermission(player);
    }

    /**
     * Get the global instance of PrivateGamesAPI.
     *
     * @return The PrivateGamesAPI instance
     */
    public static PrivateGamesAPI get() {
        if (PrivateGamesPlugin.getInstance() == null)
            throw new IllegalStateException("PrivateGamesPlugin is not enabled yet");

        return INSTANCE;
    }
}
