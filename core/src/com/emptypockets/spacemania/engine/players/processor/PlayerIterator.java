package com.emptypockets.spacemania.engine.players.processor;

import com.emptypockets.spacemania.engine.players.Player;

import java.util.Iterator;

/**
 * Created by jenfield on 12/05/2015.
 */
public interface PlayerIterator<PLR extends Player> {
    public void iterateOver(Iterator<PLR> iterator);
}
