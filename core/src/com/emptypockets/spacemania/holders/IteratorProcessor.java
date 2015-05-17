package com.emptypockets.spacemania.holders;

import java.util.Iterator;

/**
 * Created by jenfield on 14/05/2015.
 */
public interface IteratorProcessor<TYPE> {
    public void process(Iterator<TYPE> iterator);
}
