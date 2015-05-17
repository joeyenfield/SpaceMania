package com.emptypockets.spacemania.holders;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ArrayListProcessor<ENT> extends ObjectProcessor<ENT> {
    ArrayList<ENT> holder;

    public ArrayListProcessor(){
        holder = new ArrayList<ENT>();
    }

    public synchronized int getSize(){
        return holder.size();
    }
    public synchronized void add(ENT entity){
        holder.add(entity);
    }

    public synchronized void remove(ENT entity){
        holder.remove(entity);
    }

    public synchronized void clear(){
        holder.clear();
    }
    @Override
    protected Iterator<ENT> getIterator() {
        return holder.iterator();
    }
}
