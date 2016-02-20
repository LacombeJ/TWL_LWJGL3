package org.lacombej.twl;

import org.lacombej.lwjgl.GLFWWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;

/**
 * Utility class used to handle TWL - LWJGL communication
 * 
 * @author Jonathan Lacombe
 *
 */
public class TLC {
    
    private static Window window;
    
    /** Assigns a GLFW window id to this TLC layer */
    public static void create(long windowID) {
        window = new GLFWWindow(windowID);
    }
    
    /**
     * Make sure create method is called before this method is called
     * @see #create(long)
     */
    public static void update() {
        window.update();
    }
    
    public static Window window() {
        return window;
    }
    
    public static Keyboard keyboard() {
        return window.keyboard();
    }
    
    public static Mouse mouse() {
        return window.mouse();
    }
    
    /**
     * From {@link org.lwjgl.opengl.GLUtil#checkGLError()}
     * @throws OpenGLException
     */
    public static void checkGLError() throws OpenGLException {
        int err = GL11.glGetError();
        if ( err != GL11.GL_NO_ERROR )
            throw new OpenGLException(err);
    }
    
    /** @return time in milliseconds since window was created */
    public static double getTime() {
        return GLFW.glfwGetTime() / 1000;
    }
    
}
