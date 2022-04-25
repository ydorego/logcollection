package com.cribl.ydorego.logcollection.services.filter;

/**
 * Given a list of comma separated tokens, this filter will evaluate that
 * the event contains at least one of them.
 * 
 * contains://token1,token2,token3
 */
public abstract class DefaultFilterImpl implements IEventFilter {

    private final String filterScheme;

    public DefaultFilterImpl(String filterScheme) {
        this.filterScheme = filterScheme;
    }

    public String getFilterScheme() {
        return filterScheme;
    }
    
}
