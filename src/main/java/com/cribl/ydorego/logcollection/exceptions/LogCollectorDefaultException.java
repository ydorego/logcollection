package com.cribl.ydorego.logcollection.exceptions;

/**
 * Default exception class for the log collector.
 * 
 */
public class LogCollectorDefaultException extends Exception {

    private static final long serialVersionUID = 987750562339932511L;
  
    private String fieldName;

    public LogCollectorDefaultException(String fieldName, String message) {
         super(message);
         this.fieldName = fieldName;
    }
  
    public LogCollectorDefaultException(String message) {
      super(message);
    }
  
    public LogCollectorDefaultException(String message, Throwable cause) {
      super(message, cause);
    }

    public LogCollectorDefaultException(String fieldName, String message, Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
      }
  
    public LogCollectorDefaultException(Throwable cause) {
      super(cause);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    
}