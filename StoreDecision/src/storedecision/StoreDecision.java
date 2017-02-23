/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storedecision;

import Logger.Logger;
import Storage.Storage;

/**
 *
 * @author Kelwin
 */
public class StoreDecision {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length == 0)return;
        Storage s = new Storage();
        
        
        String val = "\"" + args[0];
        if(args.length > 1)
        	val += args[1];
        val += "\"";
        Logger l = new Logger(System.getProperty("user.home") + "\\Documents\\Ruscio\\OpenHAB", "storedecision.log");
        l.writeln(val);
        s.write("decision", "value", val);        
    }
    
}
