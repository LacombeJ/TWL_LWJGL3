package org.lacombej.lwjgl;

import java.nio.IntBuffer;
import org.lacombej.twl.Keyboard;
import org.lacombej.twl.Mouse;
import org.lacombej.twl.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

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
    
    private final IntBuffer buffer0;
    private final IntBuffer buffer1;
    
    private int width;
    private int height;
    
    public GLFWWindow(long windowID) {
        id = windowID;
        keyboard = new GLFWKeyboard(this);
        mouse = new GLFWMouse(this);
        buffer0 = BufferUtils.createIntBuffer(1);
        buffer1 = BufferUtils.createIntBuffer(1);
    }

    @Override
    public void update() {
        GLFW.glfwGetWindowSize(id,buffer0,buffer1);
        width = buffer0.get(0);
        height = buffer1.get(0);
        keyboard.update();
        mouse.update();
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
