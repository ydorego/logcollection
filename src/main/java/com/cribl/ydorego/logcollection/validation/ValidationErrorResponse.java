package com.cribl.ydorego.logcollection.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();


    public List<Violation> getViolations() {
        return violations;
    }
}
