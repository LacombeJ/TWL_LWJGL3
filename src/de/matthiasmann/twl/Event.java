/*
 * Copyright (c) 2008-2012, Matthias Mann
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Matthias Mann nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.matthiasmann.twl;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

/**
 * UI events for Mouse and Keyboard.
 *
 * The keyboard codes (KEY_*) are compatible with LWJGL 2.x
 * 
 * @author Matthias Mann
 */
public final class Event {

    public enum Type {
        /**
         * The mouse has entered the widget.
         * You need to return true from {@link Widget#handleEvent(de.matthiasmann.twl.Event) } in order to receive further mouse events.
         */
        MOUSE_ENTERED(true, false),
        /**
         * The mouse has moved over the widget - no mouse buttons are pressed.
         * You need to return true from {@link Widget#handleEvent(de.matthiasmann.twl.Event) } in order to receive further mouse events.
         */
        MOUSE_MOVED(true, false),
        /**
         * A mouse button has been pressed. The pressed button is available via {@link Event#getMouseButton() }
         */
        MOUSE_BTNDOWN(true, false),
        /**
         * A mouse button has been released. The released button is available via {@link Event#getMouseButton() }
         */
        MOUSE_BTNUP(true, false),
        /**
         * A click event with the left mouse button. A click is defined by a MOUSE_BTNDOWN event followed
         * by a MOUSE_BTNUP without moving the mouse outside the click distance. The MOUSE_BTNUP event is
         * sent before the MOUSE_CLICKED.
         */
        MOUSE_CLICKED(true, false),
        /**
         * The mouse has moved while at least one mouse button was pressed. The widget automatically
         * captures the mouse when a drag is started, which means that the widgets will receive mouse
         * events from this drag also outside of it's bounds. The drag ends when the last mouse button
         * is released.
         * 
         * @see Event#isMouseDragEvent()
         * @see Event#isMouseDragEnd()
         */
        MOUSE_DRAGGED(true, false),
        /**
         * The mouse has left the widget.
         */
        MOUSE_EXITED(true, false),
        /**
         * The mouse wheel has been turned. The amount is available via {@link Event#getMouseWheelDelta() }
         */
        MOUSE_WHEEL(true, false),
        /**
         * A key has been pressed. Not all keys generate characters.
         * @see #isKeyEvent()
         * @see #isKeyPressedEvent() 
         * @see #isKeyRepeated() 
         * @see #hasKeyChar() 
         * @see #hasKeyCharNoModifiers() 
         */
        KEY_PRESSED(false, true),
        /**
         * A key has been released. No character data is available.
         * @see #isKeyEvent() 
         */
        KEY_RELEASED(false, true),
        /**
         * A popup has been opened. Input event delivery will stop until the popup is closed.
         */
        POPUP_OPENED(false, false),
        /**
         * A popup has closed. Input events delivery will resume if no other popups are open.
         */
        POPUP_CLOSED(false, false),
        /**
         * Send when {@link GUI#clearKeyboardState() } is called.
         * Widgets which remeber {@link #KEY_PRESSED} events should clear their state.
         */
        CLEAR_KEYBOARD_STATE(false, false);
        
        final boolean isMouseEvent;
        final boolean isKeyEvent;
        Type(boolean isMouseEvent, boolean isKeyEvent) {
            this.isMouseEvent = isMouseEvent;
            this.isKeyEvent = isKeyEvent;
        }
    };
    
    public static final int MODIFIER_LSHIFT = 1;
    public static final int MODIFIER_LMETA = 2;
    public static final int MODIFIER_LCTRL = 4;
    public static final int MODIFIER_RSHIFT = 8;
    public static final int MODIFIER_RMETA = 16;
    public static final int MODIFIER_RCTRL = 32;
    public static final int MODIFIER_LBUTTON = 64;
    public static final int MODIFIER_RBUTTON = 128;
    public static final int MODIFIER_MBUTTON = 256;
    public static final int MODIFIER_LALT = 512;
    public static final int MODIFIER_RALT = 1024;

    /**
     * One of the shift keys is pressed
     * @see #getModifiers()
     */
    public static final int MODIFIER_SHIFT = MODIFIER_LSHIFT | MODIFIER_RSHIFT;

    /**
     * One of the meta keys (ALT on Windows) is pressed
     * @see #getModifiers()
     */
    public static final int MODIFIER_META = MODIFIER_LMETA | MODIFIER_RMETA;

    /**
     * One of the control keys is pressed
     * @see #getModifiers()
     */
    public static final int MODIFIER_CTRL = MODIFIER_LCTRL | MODIFIER_RCTRL;

    /**
     * One of the mouse buttons is pressed
     * @see #getModifiers()
     */
    public static final int MODIFIER_BUTTON = MODIFIER_LBUTTON | MODIFIER_MBUTTON | MODIFIER_RBUTTON;

    /**
     * One of the alt/menu keys is pressed
     * @see #getModifiers()
     */
    public static final int MODIFIER_ALT = MODIFIER_LALT | MODIFIER_RALT;

    /**
     * Left mouse button - this is the primary mouse button
     * @see #getMouseButton()
     */
    public static final int MOUSE_LBUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;

    /**
     * Right mouse button - this is for context menus
     * @see #getMouseButton()
     */
    public static final int MOUSE_RBUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;

    /**
     * Middle mouse button
     * @see #getMouseButton()
     */
    public static final int MOUSE_MBUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

    /**
     * The special character meaning that no character was translated for the event.
     */
    public static final char CHAR_NONE          = '\0';

    /**
     * The special keycode meaning that only the translated character is valid.
     */
    public static final int KEY_NONE            = 0x00;

    public static final int KEY_ESCAPE          = GLFW.GLFW_KEY_ESCAPE;
    public static final int KEY_1               = GLFW.GLFW_KEY_1;
    public static final int KEY_2               = GLFW.GLFW_KEY_2;
    public static final int KEY_3               = GLFW.GLFW_KEY_3;
    public static final int KEY_4               = GLFW.GLFW_KEY_4;
    public static final int KEY_5               = GLFW.GLFW_KEY_5;
    public static final int KEY_6               = GLFW.GLFW_KEY_6;
    public static final int KEY_7               = GLFW.GLFW_KEY_7;
    public static final int KEY_8               = GLFW.GLFW_KEY_8;
    public static final int KEY_9               = GLFW.GLFW_KEY_9;
    public static final int KEY_0               = GLFW.GLFW_KEY_0;
    public static final int KEY_MINUS           = GLFW.GLFW_KEY_MINUS; /* - on main keyboard */
    public static final int KEY_EQUALS          = GLFW.GLFW_KEY_EQUAL;
    public static final int KEY_BACK            = GLFW.GLFW_KEY_BACKSPACE; /* backspace */
    public static final int KEY_TAB             = GLFW.GLFW_KEY_TAB;
    public static final int KEY_Q               = GLFW.GLFW_KEY_Q;
    public static final int KEY_W               = GLFW.GLFW_KEY_W;
    public static final int KEY_E               = GLFW.GLFW_KEY_E;
    public static final int KEY_R               = GLFW.GLFW_KEY_R;
    public static final int KEY_T               = GLFW.GLFW_KEY_T;
    public static final int KEY_Y               = GLFW.GLFW_KEY_Y;
    public static final int KEY_U               = GLFW.GLFW_KEY_U;
    public static final int KEY_I               = GLFW.GLFW_KEY_I;
    public static final int KEY_O               = GLFW.GLFW_KEY_O;
    public static final int KEY_P               = GLFW.GLFW_KEY_P;
    public static final int KEY_LBRACKET        = GLFW.GLFW_KEY_LEFT_BRACKET;
    public static final int KEY_RBRACKET        = GLFW.GLFW_KEY_RIGHT_BRACKET;
    public static final int KEY_RETURN          = GLFW.GLFW_KEY_ENTER; /* Enter on main keyboard */
    public static final int KEY_LCONTROL        = GLFW.GLFW_KEY_LEFT_CONTROL;
    public static final int KEY_A               = GLFW.GLFW_KEY_A;
    public static final int KEY_S               = GLFW.GLFW_KEY_S;
    public static final int KEY_D               = GLFW.GLFW_KEY_D;
    public static final int KEY_F               = GLFW.GLFW_KEY_F;
    public static final int KEY_G               = GLFW.GLFW_KEY_G;
    public static final int KEY_H               = GLFW.GLFW_KEY_H;
    public static final int KEY_J               = GLFW.GLFW_KEY_J;
    public static final int KEY_K               = GLFW.GLFW_KEY_K;
    public static final int KEY_L               = GLFW.GLFW_KEY_L;
    public static final int KEY_SEMICOLON       = GLFW.GLFW_KEY_SEMICOLON;
    public static final int KEY_APOSTROPHE      = GLFW.GLFW_KEY_APOSTROPHE;
    public static final int KEY_GRAVE           = GLFW.GLFW_KEY_GRAVE_ACCENT; /* accent grave */
    public static final int KEY_LSHIFT          = GLFW.GLFW_KEY_LEFT_SHIFT;
    public static final int KEY_BACKSLASH       = GLFW.GLFW_KEY_BACKSLASH;
    public static final int KEY_Z               = GLFW.GLFW_KEY_Z;
    public static final int KEY_X               = GLFW.GLFW_KEY_X;
    public static final int KEY_C               = GLFW.GLFW_KEY_C;
    public static final int KEY_V               = GLFW.GLFW_KEY_V;
    public static final int KEY_B               = GLFW.GLFW_KEY_B;
    public static final int KEY_N               = GLFW.GLFW_KEY_N;
    public static final int KEY_M               = GLFW.GLFW_KEY_M;
    public static final int KEY_COMMA           = GLFW.GLFW_KEY_COMMA;
    public static final int KEY_PERIOD          = GLFW.GLFW_KEY_PERIOD; /* . on main keyboard */
    public static final int KEY_SLASH           = GLFW.GLFW_KEY_SLASH; /* / on main keyboard */
    public static final int KEY_RSHIFT          = GLFW.GLFW_KEY_RIGHT_SHIFT;
    public static final int KEY_MULTIPLY        = GLFW.GLFW_KEY_KP_MULTIPLY; /* * on numeric keypad */
    public static final int KEY_LMENU           = GLFW.GLFW_KEY_LEFT_ALT; /* left Alt */
    public static final int KEY_SPACE           = GLFW.GLFW_KEY_SPACE;
    public static final int KEY_CAPITAL         = GLFW.GLFW_KEY_CAPS_LOCK;
    public static final int KEY_F1              = GLFW.GLFW_KEY_F1;
    public static final int KEY_F2              = GLFW.GLFW_KEY_F2;
    public static final int KEY_F3              = GLFW.GLFW_KEY_F3;
    public static final int KEY_F4              = GLFW.GLFW_KEY_F4;
    public static final int KEY_F5              = GLFW.GLFW_KEY_F5;
    public static final int KEY_F6              = GLFW.GLFW_KEY_F6;
    public static final int KEY_F7              = GLFW.GLFW_KEY_F7;
    public static final int KEY_F8              = GLFW.GLFW_KEY_F8;
    public static final int KEY_F9              = GLFW.GLFW_KEY_F9;
    public static final int KEY_F10             = GLFW.GLFW_KEY_F10;
    public static final int KEY_NUMLOCK         = GLFW.GLFW_KEY_NUM_LOCK;
    public static final int KEY_SCROLL          = GLFW.GLFW_KEY_SCROLL_LOCK; /* Scroll Lock */
    public static final int KEY_NUMPAD7         = GLFW.GLFW_KEY_KP_7;
    public static final int KEY_NUMPAD8         = GLFW.GLFW_KEY_KP_8;
    public static final int KEY_NUMPAD9         = GLFW.GLFW_KEY_KP_9;
    public static final int KEY_SUBTRACT        = GLFW.GLFW_KEY_KP_SUBTRACT; /* - on numeric keypad */
    public static final int KEY_NUMPAD4         = GLFW.GLFW_KEY_KP_4;
    public static final int KEY_NUMPAD5         = GLFW.GLFW_KEY_KP_5;
    public static final int KEY_NUMPAD6         = GLFW.GLFW_KEY_KP_6;
    public static final int KEY_ADD             = GLFW.GLFW_KEY_KP_ADD; /* + on numeric keypad */
    public static final int KEY_NUMPAD1         = GLFW.GLFW_KEY_KP_1;
    public static final int KEY_NUMPAD2         = GLFW.GLFW_KEY_KP_2;
    public static final int KEY_NUMPAD3         = GLFW.GLFW_KEY_KP_3;
    public static final int KEY_NUMPAD0         = GLFW.GLFW_KEY_KP_4;
    public static final int KEY_DECIMAL         = GLFW.GLFW_KEY_KP_DECIMAL; /* . on numeric keypad */
    public static final int KEY_F11             = GLFW.GLFW_KEY_F11;
    public static final int KEY_F12             = GLFW.GLFW_KEY_F12;
    public static final int KEY_F13             = GLFW.GLFW_KEY_F13; /*                     (NEC PC98) */
    public static final int KEY_F14             = GLFW.GLFW_KEY_F14; /*                     (NEC PC98) */
    public static final int KEY_F15             = GLFW.GLFW_KEY_F15; /*                     (NEC PC98) */
    public static final int KEY_KANA            = 0x00;                     /* (Japanese keyboard)            */
    public static final int KEY_CONVERT         = 0x00;                     /* (Japanese keyboard)            */
    public static final int KEY_NOCONVERT       = 0x00;                     /* (Japanese keyboard)            */
    public static final int KEY_YEN             = 0x00;                     /* (Japanese keyboard)            */
    public static final int KEY_NUMPADEQUALS    = GLFW.GLFW_KEY_KP_EQUAL;   /* = on numeric keypad (NEC PC98) */
    public static final int KEY_CIRCUMFLEX      = 0X00;                     /* (Japanese keyboard)            */
    public static final int KEY_AT              = 0x00;                     /*                     (NEC PC98) */
    public static final int KEY_COLON           = 0x00;                     /*                     (NEC PC98) */
    public static final int KEY_UNDERLINE       = 0x00;                     /*                     (NEC PC98) */
    public static final int KEY_KANJI           = 0x00;                     /* (Japanese keyboard)            */
    public static final int KEY_STOP            = 0x00;                     /*                     (NEC PC98) */
    public static final int KEY_AX              = 0x00;                     /*                     (Japan AX) */
    public static final int KEY_UNLABELED       = 0x00;                     /*                        (J3100) */
    public static final int KEY_NUMPADENTER     = GLFW.GLFW_KEY_KP_ENTER;   /* Enter on numeric keypad */
    public static final int KEY_RCONTROL        = GLFW.GLFW_KEY_RIGHT_CONTROL;
    public static final int KEY_NUMPADCOMMA     = 0x00;                     /* , on numeric keypad (NEC PC98) */
    public static final int KEY_DIVIDE          = GLFW.GLFW_KEY_KP_DIVIDE; /* / on numeric keypad */
    public static final int KEY_SYSRQ           = 0x00;
    public static final int KEY_RMENU           = GLFW.GLFW_KEY_RIGHT_ALT; /* right Alt */
    public static final int KEY_PAUSE           = GLFW.GLFW_KEY_PAUSE; /* Pause */
    public static final int KEY_HOME            = GLFW.GLFW_KEY_HOME; /* Home on arrow keypad */
    public static final int KEY_UP              = GLFW.GLFW_KEY_UP; /* UpArrow on arrow keypad */
    public static final int KEY_PRIOR           = GLFW.GLFW_KEY_PAGE_UP; /* PgUp on arrow keypad */
    public static final int KEY_LEFT            = GLFW.GLFW_KEY_LEFT; /* LeftArrow on arrow keypad */
    public static final int KEY_RIGHT           = GLFW.GLFW_KEY_RIGHT; /* RightArrow on arrow keypad */
    public static final int KEY_END             = GLFW.GLFW_KEY_END; /* End on arrow keypad */
    public static final int KEY_DOWN            = GLFW.GLFW_KEY_DOWN; /* DownArrow on arrow keypad */
    public static final int KEY_NEXT            = GLFW.GLFW_KEY_PAGE_DOWN; /* PgDn on arrow keypad */
    public static final int KEY_INSERT          = GLFW.GLFW_KEY_INSERT; /* Insert on arrow keypad */
    public static final int KEY_DELETE          = GLFW.GLFW_KEY_DELETE; /* Delete on arrow keypad */
    public static final int KEY_LMETA           = GLFW.GLFW_KEY_LEFT_SUPER; /* Left Windows/Option key */
    public static final int KEY_RMETA           = GLFW.GLFW_KEY_RIGHT_SUPER; /* Right Windows/Option key */
    public static final int KEY_APPS            = 0X00;                     /* AppMenu key */
    public static final int KEY_POWER           = 0X00;
    public static final int KEY_SLEEP           = 0X00;
    
    Type type;
    int mouseX;
    int mouseY;
    int mouseWheelDelta;
    int mouseButton;
    int mouseClickCount;
    boolean dragEvent;
    boolean keyRepeated;
    char keyChar;
    int keyCode;
    int modifier;
    private Event subEvent;

    Event() {
    }

    /**
     * Returns the type of the event.
     * @return the type of the event.
     */
    public final Type getType() {
        return type;
    }

    /**
     * Returns true for all MOUSE_* event types.
     * @return true if this is a mouse event.
     */
    public final boolean isMouseEvent() {
        return type.isMouseEvent;
    }

    /**
     * Returns true for all MOUSE_* event types except MOUSE_WHEEL.
     * @return true if this is a mouse event but not a mouse wheel event.
     */
    public final boolean isMouseEventNoWheel() {
        return type.isMouseEvent && type != Type.MOUSE_WHEEL;
    }

    /**
     * Returns true for all KEY_* event types.
     * @return true if this is a key event.
     */
    public final boolean isKeyEvent() {
        return type.isKeyEvent;
    }

    /**
     * Returns true for the KEY_PRESSED event type.
     * @return true if this is key pressed event.
     */
    public final boolean isKeyPressedEvent() {
        return type == Type.KEY_PRESSED;
    }

    /**
     * Returns true if this event is part of a drag operation
     * @return true if this event is part of a drag operation
     */
    public final boolean isMouseDragEvent() {
        return dragEvent;
    }

    /**
     * Returns true if this event ends a drag operation
     * @return true if this event ends a drag operation
     */
    public final boolean isMouseDragEnd() {
        return (modifier & MODIFIER_BUTTON) == 0;
    }

    /**
     * Returns the current absolute mouse X coordinate
     * @return the current absolute mouse X coordinate
     */
    public final int getMouseX() {
        return mouseX;
    }

    /**
     * Returns the current absolute mouse Y coordinate
     * @return the current absolute mouse Y coordinate
     */
    public final int getMouseY() {
        return mouseY;
    }

    /**
     * The mouse button. Only valid for MOUSE_BTNDOWN or MOUSE_BTNUP events
     * @return the mouse button
     * @see Type#MOUSE_BTNDOWN
     * @see Type#MOUSE_BTNUP
     * @see #MOUSE_LBUTTON
     * @see #MOUSE_RBUTTON
     * @see #MOUSE_MBUTTON
     */
    public final int getMouseButton() {
        return mouseButton;
    }

    /**
     * The mouse wheel delta. Only valid for MOUSE_WHEEL events
     * @return the mouse wheel delta
     * @see Type#MOUSE_WHEEL
     */
    public final int getMouseWheelDelta() {
        return mouseWheelDelta;
    }
    
    /**
     * The mouse click count. Only valid for MOUSE_CLICKED events
     * @return the mouse click count
     * @see Type#MOUSE_CLICKED
     */
    public final int getMouseClickCount() {
        return mouseClickCount;
    }
    
    /**
     * Returns the key code. Only valid for KEY_PRESSED or KEY_RELEASED events
     * @return the key code (one of the KEY_* constants)
     */
    public final int getKeyCode() {
        return keyCode;
    }

    /**
     * Returns the key character. Only valid if hasKeyChar() returns true.
     * @see #hasKeyChar()
     * @return the key character
     */
    public final char getKeyChar() {
        return keyChar;
    }

    /**
     * Checks if a character is available for theis KEY_PRESSED event
     * @see #getKeyChar()
     * @return true if a character is available
     */
    public final boolean hasKeyChar() {
        return type == Type.KEY_PRESSED && keyChar != CHAR_NONE;
    }

    /**
     * Checks if a characters is available and no keyboard modifiers are
     * active (except these needed to generate that character).
     * 
     * @return true if it's a character without additional modifiers
     */
    public final boolean hasKeyCharNoModifiers() {
        final int MODIFIER_ALTGR = MODIFIER_LCTRL | MODIFIER_RALT;
        return hasKeyChar() && (
                ((modifier & ~MODIFIER_SHIFT) == 0) ||
                ((modifier & ~MODIFIER_ALTGR) == 0));
    }

    /**
     * Returns true if this is a repeated KEY_PRESSED event
     * @return true if this is a repeated KEY_PRESSED event
     */
    public final boolean isKeyRepeated() {
        return type == Type.KEY_PRESSED && keyRepeated;
    }

    /**
     * Returns the current event modifiers
     * @return the current event modifiers
     */
    public final int getModifiers() {
        return modifier;
    }

    final Event createSubEvent(Type newType) {
        if(subEvent == null) {
            subEvent = new Event();
        }
        subEvent.type = newType;
        subEvent.mouseX = mouseX;
        subEvent.mouseY = mouseY;
        subEvent.mouseButton = mouseButton;
        subEvent.mouseWheelDelta = mouseWheelDelta;
        subEvent.mouseClickCount = mouseClickCount;
        subEvent.dragEvent = dragEvent;
        subEvent.keyRepeated = keyRepeated;
        subEvent.keyChar = keyChar;
        subEvent.keyCode = keyCode;
        subEvent.modifier = modifier;
        return subEvent;
    }
    
    final Event createSubEvent(int x, int y) {
        Event e = createSubEvent(type);
        e.mouseX = x;
        e.mouseY = y;
        return e;
    }
    
    void setModifier(int mask, boolean pressed) {
        if(pressed) {
            modifier |= mask;
        } else {
            modifier &= ~mask;
        }
    }

    void setModifiers(boolean pressed) {
        int mask;
        switch(keyCode) {
            case KEY_LSHIFT:   mask = MODIFIER_LSHIFT; break;
            case KEY_LMETA:    mask = MODIFIER_LMETA; break;
            case KEY_LCONTROL: mask = MODIFIER_LCTRL; break;
            case KEY_LMENU:    mask = MODIFIER_LALT; break;
            case KEY_RSHIFT:   mask = MODIFIER_RSHIFT; break;
            case KEY_RMETA:    mask = MODIFIER_RMETA; break;
            case KEY_RCONTROL: mask = MODIFIER_RCTRL; break;
            case KEY_RMENU:    mask = MODIFIER_RALT; break;
            default: return;
        }
        setModifier(mask, pressed);
    }

    private static final String[] KEY_NAMES = new String[256];
    private static final HashMap<String, Integer> KEY_MAP = new HashMap<String, Integer>(256);
    
    static {
        try {
            for(Field f : Event.class.getFields()) {
                String name = f.getName();
                if(name.startsWith("KEY_")) {
                    Integer code = (Integer)f.get(null);
                    name = name.substring(4);
                    KEY_NAMES[code] = name;
                    KEY_MAP.put(name, code);
                }
            }
        } catch (Throwable ignore) {
        }
    }

    /**
     * Returns the name for the given key code or null if the key code is not assigned.
     * @param key the key code.
     * @return the name of the key or null.
     */
    public static String getKeyNameForCode(int key) {
        if(key >= 0 && key < 256) {
            return KEY_NAMES[key];
        }
        return null;
    }

    /**
     * Returns the key code for the given key name.
     * The key name must match one of the KEY_* fields of this class.
     *
     * @param name the key name
     * @return the key code or KEY_NONE
     */
    public static int getKeyCodeForName(String name) {
        Integer code = KEY_MAP.get(name);
        if(code != null) {
            return code;
        }
        return KEY_NONE;
    }
}
