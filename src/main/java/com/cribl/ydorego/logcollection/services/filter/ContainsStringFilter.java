package com.cribl.ydorego.logcollection.services.filter;

/**
 * Given a string, this filter will evaluate that the event contains that substring
 * 
 * contains-string://token1
 */
public class ContainsStringFilter extends DefaultFilterImpl {

    private final String string;

    public ContainsStringFilter(String filterScheme) {
        super(filterScheme);

        string = this.getFilterScheme().substring("contains:".length());
    }

    @Override
    public boolean accept(String event) {
        if (event != null) {
            return event.contains(string);
        }
        return false;
    }
    
}
