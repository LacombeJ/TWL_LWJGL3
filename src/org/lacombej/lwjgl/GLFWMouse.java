package org.lacombej.lwjgl;

import java.nio.DoubleBuffer;
import java.util.ArrayDeque;
import org.lacombej.twl.Mouse;
import org.lacombej.twl.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
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
    private final int[] eventButtons;
    
    private final ArrayDeque<ButtonEvent> eventQueue = new ArrayDeque<>();
    private ButtonEvent event = null;
    
    private final DoubleBuffer xBuffer;
    private final DoubleBuffer yBuffer;
    
    private int x;
    private int y;
    
    @SuppressWarnings("unused")
    private final GLFWScrollCallback scrollCallback;
    private int scrollDY;
    
    public GLFWMouse(Window window) {
        this.window = window;
        eventButtons = new int[]{Event.MOUSE_LBUTTON, Event.MOUSE_RBUTTON, Event.MOUSE_MBUTTON};
        xBuffer = BufferUtils.createDoubleBuffer(1);
        yBuffer = BufferUtils.createDoubleBuffer(1);
        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long windowid, double xoffset, double yoffset) {
                scrollDY = (int) yoffset;
            }
        };
    }
    
    /** Called every frame */
    public void update() {
        GLFW.glfwGetCursorPos(window.id(),xBuffer,yBuffer);
        x = (int) Math.floor(xBuffer.get(0));
        y = (int) Math.floor(yBuffer.get(0));
        y = window.height() - y;
        for (int i=0; i<eventButtons.length; i++) {
            int action = GLFW.glfwGetMouseButton(window.id(), eventButtons[i]);
            switch (action) {
            case GLFW.GLFW_PRESS:
                eventQueue.add(new ButtonEvent(eventButtons[i],x,y,scrollDY,true));
                break;
            case GLFW.GLFW_RELEASE:
                eventQueue.add(new ButtonEvent(eventButtons[i],x,y,scrollDY,false));
                break;
            }
        }
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

}
