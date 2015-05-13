package com.emptypockets.spacemania.engine.players.processor;

import com.emptypockets.spacemania.engine.players.Player;

/**
 * Created by jenfield on 12/05/2015.
 */
public interface PlayerProcessor<PLR extends Player> {
    public void processPlayer(PLR player);
}
