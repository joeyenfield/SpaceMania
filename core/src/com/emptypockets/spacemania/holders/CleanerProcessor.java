package com.emptypockets.spacemania.holders;

/**
 * Created by jenfield on 14/05/2015.
 */
public interface CleanerProcessor<ENT> {
    public boolean shouldRemove(ENT entity);
}
