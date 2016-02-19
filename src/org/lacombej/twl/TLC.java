package org.lacombej.twl;

import java.nio.IntBuffer;

import org.lacombej.lwjgl.GLFWKeyboard;
import org.lacombej.lwjgl.GLFWMouse;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.OpenGLException;

/**
 * Utility class used to handle TWL- LWJGL communication
 * 
 * @author Jonathan Lacombe
 *
 */
public class TLC {

    private static long windowID;
    
    private static GLFWKeyboard keyboard;
    private static GLFWMouse mouse;
    
    private static final IntBuffer BUFFER_0;
    private static final IntBuffer BUFFER_1;
    
    private static int windowWidth;
    private static int windowHeight;
    
    static {
        BUFFER_0 = IntBuffer.allocate(1);
        BUFFER_1 = IntBuffer.allocate(1);
    }
    
    public static void create(long windowID) {
        TLC.windowID = windowID;
        keyboard = new GLFWKeyboard(windowID);
        mouse = new GLFWMouse(windowID);
    }
    
    public static void update() {
        GLFW.glfwGetWindowSize(windowID, BUFFER_0, BUFFER_1);
        windowWidth = BUFFER_0.get(0);
        windowHeight = BUFFER_1.get(0);
        System.out.println(windowWidth+" "+windowHeight);
        keyboard.update();
        mouse.update();
    }
    
    public static Keyboard keyboard() {
        return keyboard;
    }
    
    public static Mouse mouse() {
        return mouse;
    }
    
    public static boolean isWindowActive() {
        return GLFW.glfwGetWindowAttrib(windowID, GLFW.GLFW_FOCUSED) == GLFW.GLFW_FOCUSED;
    }
    
    public static boolean isInsideWindow(int x, int y) {
        return x>=0 && x<=windowWidth && y>=0 && y<=windowHeight;
    }
    
    public static long getWindowID() {
        return windowID;
    }
    
    public static int getWindowWidth() {
        return windowWidth;
    }
    
    public static int getWindowHeight() {
        return windowHeight;
    }
    
    public static void checkGLError() throws OpenGLException {
        int err = GL11.glGetError();
        if ( err != GL11.GL_NO_ERROR )
            throw new OpenGLException(err);
    }
    
    /** @return time in milliseconds since window was created */
    public static double getTime() {
        return GLFW.glfwGetTime() / 1000;
    }
    
    public static void setCursor(long cursorID) {
        GLFW.glfwSetCursor(windowID, cursorID);
    }
    
}
