package com.cribl.ydorego.logcollection.services.filter;

import java.util.Arrays;
import java.util.List;

/**
 * Given a list of comma separated tokens, this filter will evaluate that
 * the event contains at least one of them.
 * 
 * contains-all:token1,token2,token3
 */
public class ContainsAllFilter extends DefaultFilterImpl {

    private final List<String> tokensList;

    public ContainsAllFilter(String filterScheme) {
        super(filterScheme);

        String[] tokens = this.getFilterScheme().substring("contains-all:".length()).split(",");
        tokensList = Arrays.asList(tokens);
    }

    @Override
    public boolean accept(String event) {
        if (event != null) {
            return Arrays.asList(event.split(" ")).containsAll(tokensList);
        }
        return false;
    }
    
}
