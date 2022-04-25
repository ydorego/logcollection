package com.cribl.ydorego.logcollection.services.filter;

/**
 * 
 * Event filtering interface, to be used by implementors for
 * various filtering capabilities.
 * 
 */
public interface IEventFilter {

    /** 
     * @param event the event to be filter
     * 
     * @return true of false
     */
    boolean accept(String event);
    
}
