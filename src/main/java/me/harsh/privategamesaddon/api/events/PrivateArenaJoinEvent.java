package me.harsh.privategamesaddon.api.events;

import de.marcely.bedwars.api.arena.AddPlayerIssue;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.ArenaEvent;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Validate;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Gets called when a player attempts to join a private arena.
 * <p>
 *   This includes both members and non-members of the party.
 *   This event is not called if the arena is just turned private due to the joining
 *   player (use {@link PrivateArenaCreateEvent} for that). It is also not called if the
 *   arena is public.
 * </p>
 */
public class PrivateArenaJoinEvent extends PlayerEvent implements ArenaEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  @Getter
  private final Arena arena;

  private Result result;

  public PrivateArenaJoinEvent(Player player, Arena arena, Result result) {
    super(player);

    this.arena = arena;
    this.result = result;
  }

  /**
   * Get the result of the join attempt.
   *
   * @return The join result
   */
  public Result getResult() {
    return this.result;
  }

  /**
   * Set the result of the join attempt.
   *
   * @param result The new join result
   */
  public void setResult(Result result) {
    Validate.notNull(result, "result");

    this.result = result;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }


  /**
   * Defines what is the outcome of a player's attempt to join a private arena.
   */
  public enum Result {

    /**
     * The player is allowed to join because they are a member of the party.
     */
    ALLOW_IS_MEMBER,

    /**
     * The player is allowed to join because they are a server admin and forc-joined.
     */
    ALLOW_IS_ADMIN,

    /**
     * The player is allowed to join due to a custom reason (only from API).
     * <p>
     *   No join message will be sent.
     * </p>
     */
    ALLOW_CUSTOM,

    /**
     * The player is denied from joining because they are not in the party.
     */
    DENY_NOT_IN_PARTY,

    /**
     * The player is denied from joining due to a custom reason (only from API).
     * <p>
     *   No deny message will be sent.
     * </p>
     */
    DENY_CUSTOM;

    private static final AddPlayerIssue ISSUE_PRIVATE = AddPlayerIssue.construct(
        "privategames:private_arena",
        Message.buildByKey("PrivateGames_JoinArenaPrivate"));

    private static final AddPlayerIssue ISSUE_CUSTOM = AddPlayerIssue.construct(
        "privategames:custom", (String) null);

    /**
     * Check whether this result allows the player to join.
     *
     * @return <code>true</code> if the player is allowed to join, <code>false</code> otherwise
     */
    public boolean isAllow() {
      switch (this) {
        case ALLOW_IS_MEMBER:
        case ALLOW_IS_ADMIN:
        case ALLOW_CUSTOM:
          return true;
        default:
          return false;
      }
    }

    /**
     * Check whether this result denies the player from joining.
     *
     * @return <code>true</code> if the player is denied from joining, <code>false</code> otherwise
     */
    public boolean isDeny() {
      return !isAllow();
    }

    /**
     * Get the issue associated with this result.
     * <p>
     *   Do note that the internal logic may not always use this issue directly
     *   due to the need of async loading.
     * </p>
     *
     * @return The associated issue, or <code>null</code> if {@link #isDeny()}
     */
    @Nullable
    public AddPlayerIssue getIssue() {
      switch (this) {
        case DENY_NOT_IN_PARTY:
          return ISSUE_PRIVATE;
        case DENY_CUSTOM:
          return ISSUE_CUSTOM;
        default:
          return null;
      }
    }
  }
}
