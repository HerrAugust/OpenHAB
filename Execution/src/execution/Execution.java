/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execution;

import Logger.Logger;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Kelwin
 */
public class Execution {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length == 0) {
            return;
        }

        try {
            PrintWriter writer = new PrintWriter("execute.txt", "UTF-8");
            switch (args[0]) {
                case "close lights": //used
                    if (args.length == 1) {
                        new Logger("Execution").writeln("[ERROR] Light to be turned off not set. Nothing to do...");
                        return;
                    }
                    writer.println("close lights: " + args[1] + "");
                    //Execution.closeLights_room(args[1]);
                    break;
                case "close light": //used
                    if (args.length == 1) {
                        new Logger("Execution").writeln("[ERROR] Light to be turned off not set. Nothing to do...");
                        return;
                    }
                    writer.println("close light: " + args[1] + "");
                    //Execution.closeLight(args[1]);
                    break;
                case "open light": //used
                    if (args.length == 1) {
                        new Logger("Execution").writeln("[ERROR] Light to be turned on not set. Nothing to do...");
                        return;
                    }
                    writer.println("open light: " + args[1] + "");
                    //Execution.openLight(args[1]);
                    break;
                case "close windows": //used
                    writer.println("windows: " + 0 + "");
                    //Execution.closeWindows();
                    break;
                case "close window": //used
                    if (args.length == 1) {
                        new Logger("Execution").writeln("[ERROR] Window to be closed not set. Nothing to do...");
                        return;
                    }
                    writer.println("close window: " + args[1] + "");
                    //Execution.closeWindow(args[1]);
                    break;
                case "open window": //used
                    if (args.length == 1) {
                        new Logger("Execution").writeln("[ERROR] Window to be open not set. Nothing to do...");
                        return;
                    }
                    writer.println("open window: " + args[1] + "");
                    //Execution.openWindow(args[1]);
                    break;
                case "increase temperature": //used
                    if (args.length == 1) {
                        new Logger("Execution").writeln("[ERROR] New temperature not set. Nothing to do...");
                        return;
                    }
                    float threshold = Float.parseFloat(args[1]);
                    writer.println("increase temperature: " + threshold + "");
                    //Execution.increaseTemperature(threshold);
                    break;
                case "decrease temperature": //used
                    if (args.length == 1) {
                        new Logger("Execution").writeln("[ERROR] New temperature not set. Nothing to do...");
                        return;
                    }
                    float threshold2 = Float.parseFloat(args[1]);
                    writer.println("decrease temperature: " + threshold2 + "");
                    //Execution.increaseTemperature(threshold);
                    break;

                case "open windows": //used
                    for (int i = 0; i < 7; i++) {
                        writer.println("windows: " + 1 + "");
                        //Execution.openWindow(i);
                    }
                    break;
                default:
                    new Logger("Execution").writeln("[DEBUG] args[0] = " + args[0] + ". Nothing to do...");
                    break;
            }
            new Logger("Execution").writeln("[DEBUG] Received command: " + args[0] + " " + (args.length == 2 ? args[1] : ""));
            writer.close();
        } catch (IOException e) {
            // do something
        }

    }
}
