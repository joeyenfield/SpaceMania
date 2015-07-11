package com.emptypockets.spacemania.holders;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by jenfield on 14/05/2015.
 */
public abstract class ObjectProcessor<ENT> {

    protected abstract Iterator<ENT> getIterator();

    /**
     * Overidded in array processor
     * @param processor
     */
    public synchronized void process(SingleProcessor<ENT> processor) {
        Iterator<ENT> iterator = getIterator();
        while (iterator.hasNext()) {
            ENT entity = iterator.next();
            processor.process(entity);
        }
    }

    public synchronized void processParallel(final SingleProcessor<ENT> processor, int maxCores, int timeoutSeconds) throws InterruptedException {
        Iterator<ENT> iterator = getIterator();
        ExecutorService service = Executors.newFixedThreadPool(maxCores);
        while (iterator.hasNext()) {
            final ENT entity = iterator.next();
            service.submit(new Runnable() {
                @Override
                public void run() {
                    processor.process(entity);
                }
            });
        }
        service.awaitTermination(timeoutSeconds, TimeUnit.SECONDS);
    }

    public synchronized  void process(IteratorProcessor<ENT> processor){
        processor.process(getIterator());
    }

    public synchronized void process(final CleanerProcessor<ENT> processor){
        Iterator<ENT> iterator = getIterator();
        while (iterator.hasNext()) {
            ENT entity = iterator.next();
            if(processor.shouldRemove(entity)){
                iterator.remove();
            }
        }
    }

}
