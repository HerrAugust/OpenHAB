/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storedecision;

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
        
        s.write("decision", "value", args[0]);        
        
    }
    
}
