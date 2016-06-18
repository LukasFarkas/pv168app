/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.common;

/**
 *
 * @author tomf
 */
public class DataSourceException extends RuntimeException {

    /**
     * Constructs an instance of <code>EntityNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DataSourceException(String msg) {
        super(msg);
    }
    
    public DataSourceException(Throwable cause) {
        super(cause);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}