package org.lacombej.lwjgl;

import org.lacombej.twl.Keyboard;
import org.lacombej.twl.Mouse;

/**
 * Utility class used to handle TWL- LWJGL communication
 * 
 * @author Jonathan Lacombe
 *
 */
public class TLC {

    private static long windowID;
    
    public static void create(long windowID) {
        TLC.windowID = windowID;
    }
    
    public static Keyboard keyboard() {
        return null; //TODO
    }
    
    public static Mouse mouse() {
        return null; //TODO
    }
    
    public static boolean isWindowActive() {
        return true; //TODO
    }
    
    public long getWindowID() {
        return windowID;
    }
    
}
