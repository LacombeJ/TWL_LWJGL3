package org.lacombej.lwjgl;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Arrays;

import org.lacombej.twl.Keyboard;
import org.lwjgl.glfw.GLFW;

import de.matthiasmann.twl.Event;

/**
 * Keyboard class for TWL Events
 * 
 * @author Jonathan Lacombe
 *
 */
public class GLFWKeyboard implements Keyboard {

    private final long windowID;
    private final int[] eventKeys;
    private final boolean[] keyDown;
    
    private final ArrayDeque<KeyEvent> eventQueue = new ArrayDeque<>();
    private KeyEvent event = null;
    
    public GLFWKeyboard(long windowID) {
        this.windowID = windowID;
        eventKeys = getEventKeys();
        keyDown = new boolean[eventKeys.length];
    }
    
    /** Called every frame */
    public void update() {
        for (int i=0; i<eventKeys.length; i++) {
            int action = GLFW.glfwGetKey(windowID, eventKeys[i]);
            switch (action) {
            case GLFW.GLFW_PRESS:
                if (!keyDown[i]) {
                    eventQueue.add(new KeyEvent(eventKeys[i],toChar(eventKeys[i]),true));
                }
                keyDown[i] = true;
                break;
            case GLFW.GLFW_RELEASE:
                if (keyDown[i]) {
                    eventQueue.add(new KeyEvent(eventKeys[i],Event.CHAR_NONE,false));
                }
                keyDown[i] = false;
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
    public int getEventKey() {
        return event.key;
    }

    @Override
    public char getEventCharacter() {
        return event.character;
    }

    @Override
    public boolean getEventKeyState() {
        return event.keyState;
    }
    
    class KeyEvent {
        int key;
        char character;
        boolean keyState;
        KeyEvent(int key, char character, boolean keyState) {
            this.key = key;
            this.character = character;
            this.keyState = keyState;
        }
    }
    
    public char toChar(int key) {
        if (key<256) {
            return (char)key;
        }
        return Event.CHAR_NONE;
    }
    
    private int[] getEventKeys() {
        int[] keys = new int[256];
        int keyCount = 0;
        for (Field f : Event.class.getFields()) {
            if (f.getName().startsWith("KEY_")) {
                try {
                    keys[keyCount++] = f.getInt(null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return Arrays.copyOf(keys,keyCount);
    }

}
