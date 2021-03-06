package org.lacombej.twl;

/**
 * Interface for a Keyboard
 * <p>
 * A Keyboard event is queued in the following conditions:
 * <ul>
 * <li>Key pressed: KeyEvent(key,char,true)</li>
 * <li>Key button released: KeyEvent(key,'\0',false)</li>
 * <ul>
 * </p>
 * 
 * @author Jonathan Lacombe
 *
 */
public interface Keyboard {
    
    /**
     * Gets the next Keyboard event. After this is called you can:
     * </br>
     * Get the key that caused the event</br>
     * Get the character for the event</br>
     * Get the state of the event</br>
     * @see #getEventKey()
     * @see #getEventCharacter()
     * @see #getEventKeyState()
     * @return true iff a keyboard event was read
     */
    public boolean next();
    
    /** @return the key corresponding to the event */
    public int getEventKey();
    
    /**
     * If the key is released Event.CHAR_NONE should be returned ('\0')
     * @return the character for the event
     */
    public char getEventCharacter();
    
    /** @return true if key was pressed or false if released */
    public boolean getEventKeyState();
    
    /** @return the name of a given key */
    public String getKeyName(int key);
    
}
