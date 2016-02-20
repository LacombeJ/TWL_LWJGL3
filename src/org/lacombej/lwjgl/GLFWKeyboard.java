package org.lacombej.lwjgl;

import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

import org.lacombej.twl.Keyboard;
import org.lacombej.twl.Window;
import org.lwjgl.glfw.GLFW;

import de.matthiasmann.twl.Event;

/**
 * Keyboard class for TWL Events
 * 
 * @author Jonathan Lacombe
 *
 */
public class GLFWKeyboard implements Keyboard {

    private final Window window;
    private final int[] eventKeys;
    private String[] keyNames;
    private final boolean[] keyDown;
    
    private final ArrayDeque<KeyEvent> eventQueue = new ArrayDeque<>();
    private final HashMap<Integer,Integer> keyMap = new HashMap<>();
    private KeyEvent event = null;
    
    public GLFWKeyboard(Window window) {
        this.window = window;
        eventKeys = getEventKeys();
        keyDown = new boolean[eventKeys.length];
    }
    
    /** Called every frame */
    public void update() {
        for (int i=0; i<eventKeys.length; i++) {
            int action = GLFW.glfwGetKey(window.id(), eventKeys[i]);
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
            boolean shift = keyDown[keyMap.get(Event.KEY_LSHIFT)] ||
                    keyDown[keyMap.get(Event.KEY_RSHIFT)];
            if (key>=Event.KEY_A && key<=Event.KEY_Z) {
                if (!shift) return (char) (key - Event.KEY_A + 'a');
                return (char)key;
            }
            if (!shift) return (char)key;
            switch (key) {
            case Event.KEY_GRAVE:       return '~';
            case Event.KEY_1:           return '!';
            case Event.KEY_2:           return '@';
            case Event.KEY_3:           return '#';
            case Event.KEY_4:           return '$';
            case Event.KEY_5:           return '%';
            case Event.KEY_6:           return '^';
            case Event.KEY_7:           return '&';
            case Event.KEY_8:           return '*';
            case Event.KEY_9:           return '(';
            case Event.KEY_0:           return ')';
            case Event.KEY_MINUS:       return '_';
            case Event.KEY_EQUALS:      return '+';
            case Event.KEY_LBRACKET:    return '{';
            case Event.KEY_RBRACKET:    return '}';
            case Event.KEY_BACKSLASH:   return '|';
            case Event.KEY_SEMICOLON:   return ':';
            case Event.KEY_APOSTROPHE:  return '\"';
            case Event.KEY_COMMA:       return '<';
            case Event.KEY_PERIOD:      return '>';
            case Event.KEY_SLASH:       return '?';
            }
            return (char)key;
        }
        return Event.CHAR_NONE;
    }
    
    private int[] getEventKeys() {
        int[] keys = new int[256];
        String[] names = new String[256];
        int keyCount = 0;
        for (Field f : Event.class.getFields()) {
            String name = f.getName();
            if (name.startsWith("KEY_")) {
                try {
                    keys[keyCount] = f.getInt(null);
                    names[keyCount] = name.substring(4);
                    keyMap.put(keys[keyCount],keyCount);
                    keyCount++;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        keyNames = Arrays.copyOf(names,keyCount);
        return Arrays.copyOf(keys,keyCount);
    }

    //This will be fixed later TODO
    @Override
    public String getKeyName(int key) {
        return keyNames[keyMap.get(key)];
    }

}
