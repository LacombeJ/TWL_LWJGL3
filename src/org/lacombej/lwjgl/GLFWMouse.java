package org.lacombej.lwjgl;

import java.nio.DoubleBuffer;
import java.util.ArrayDeque;
import org.lacombej.twl.Mouse;
import org.lacombej.twl.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import de.matthiasmann.twl.Event;

/**
 * Mouse class for TWL Events
 * 
 * @author Jonathan Lacombe
 *
 */
public class GLFWMouse implements Mouse {

    private final Window window;
    
    private final ArrayDeque<ButtonEvent> eventQueue = new ArrayDeque<>();
    private ButtonEvent event = null;
    
    private int x;
    private int y;
    
    private final GLFWScrollCallback scrollCallback;
    private final GLFWCursorPosCallback cursorPosCallback;
    private final GLFWMouseButtonCallback mouseButtonCallback;
    
    public GLFWMouse(Window window) {
        this.window = window;
        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long windowid, double xoffset, double yoffset) {
                eventQueue.add(new ButtonEvent(-1,x,y,toInt(yoffset),false));
            }
        };
        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long windowid, double xpos, double ypos) {
                x = toInt(xpos);
                y = window.height() - toInt(ypos);
                eventQueue.add(new ButtonEvent(-1,x,y,0,false));
            }
        };
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                eventQueue.add(new ButtonEvent(button,x,y,0,action==GLFW.GLFW_PRESS));
            }
        };
        GLFW.glfwSetScrollCallback(window.id(),scrollCallback);
        GLFW.glfwSetCursorPosCallback(window.id(),cursorPosCallback);
        GLFW.glfwSetMouseButtonCallback(window.id(),mouseButtonCallback);
    }
    
    @Override
    public boolean next() {
        if (!eventQueue.isEmpty()) {
            event = eventQueue.removeFirst();
            return true;
        }
        return false;
    }

    @Override
    public int getEventButton() {
        return event.button;
    }

    @Override
    public int getEventX() {
        return event.x;
    }

    @Override
    public int getEventY() {
        return event.y;
    }

    @Override
    public int getEventDWheel() {
        return event.dWheel;
    }

    @Override
    public boolean getEventButtonState() {
        return event.buttonState;
    }

    @Override
    public boolean isInsideWindow() {
        return x>=0 && x<=window.width() && y>=0 && y<=window.height();
    }
    
    class ButtonEvent {
        int button;
        int x;
        int y;
        int dWheel;
        boolean buttonState;
        ButtonEvent(int button, int x, int y, int dWheel, boolean buttonState) {
            this.button = button;
            this.x = x;
            this.y = y;
            this.dWheel = dWheel;
            this.buttonState = buttonState;
        }
    }
    
    private static int toInt(double d) {
        return (int) Math.floor(d);
    }

}
