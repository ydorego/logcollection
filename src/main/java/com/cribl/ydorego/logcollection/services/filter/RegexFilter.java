package com.cribl.ydorego.logcollection.services.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Given a regex expression, this filter will evaluate that the event pass the regex.
 * 
 * regex:[^abc]
 */
public class RegexFilter extends DefaultFilterImpl {

    private final Pattern pattern;

    public RegexFilter(String filterScheme) {
        super(filterScheme);
        pattern = Pattern.compile(getFilterScheme().substring("regex:".length()), Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean accept(String event) {
        if (event != null) {
            Matcher matcher = pattern.matcher(event);
            return matcher.find();
        }
        return false;
    }
    
}
