package com.cribl.ydorego.logcollection.services.filter;

/**
 * Default abstract filter implementation to keep the passed scheme
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
