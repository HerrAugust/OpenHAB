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
        // TODO code application logic here
        Storage s = new Storage();
        
        s.exeQuery(args[0]);        
        
    }
    
}
