package org.lacombej.lwjgl;

import java.nio.IntBuffer;
import org.lacombej.twl.Keyboard;
import org.lacombej.twl.Mouse;
import org.lacombej.twl.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

/**
 * Window class for TLC
 * 
 * @author Jonathan Lacombe
 *
 */
public class GLFWWindow implements Window {

    private final long id;
    private final GLFWKeyboard keyboard;
    private final GLFWMouse mouse;
    
    private int width;
    private int height;
    
    private final GLFWWindowSizeCallback windowSizeCallback;
    
    public GLFWWindow(long windowID) {
        id = windowID;
        keyboard = new GLFWKeyboard(this);
        mouse = new GLFWMouse(this);
        IntBuffer bWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bHeight = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(id,bWidth,bHeight);
        width = bWidth.get(0);
        height = bHeight.get(0);
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = w;
            }   
        };
        GLFW.glfwSetWindowSizeCallback(id,windowSizeCallback);
    }

    @Override
    public void update() {
        keyboard.update();
    }

    @Override
    public Keyboard keyboard() {
        return keyboard;
    }

    @Override
    public Mouse mouse() {
        return mouse;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public boolean isActive() {
        return GLFW.glfwGetWindowAttrib(id, GLFW.GLFW_FOCUSED) == GLFW.GLFW_FOCUSED;
    }

    @Override
    public void setCursor(long cursorID) {
        GLFW.glfwSetCursor(id, cursorID);
    }
    

}
