package org.lacombej.lwjgl;

import org.lacombej.twl.Keyboard;
import org.lacombej.twl.Mouse;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;

/**
 * Utility class used to handle TWL- LWJGL communication
 * 
 * @author Jonathan Lacombe
 *
 */
public class TLC {

    private static long windowID;
    private static GLUtil util;
    
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
    
    public static long getWindowID() {
        return windowID;
    }
    
    public static void checkGLError() {
        util.checkGLError();
    }
    
    /** @return time in milliseconds since window was created */
    public static double getTime() {
        return GLFW.glfwGetTime() / 1000;
    }
    
}
