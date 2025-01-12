package me.harsh.privategamesaddon.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.harsh.privategamesaddon.managers.PrivateGameManager;

@UtilityClass
public class Utility {

  @Getter
  private final PrivateGameManager manager = new PrivateGameManager();
}
