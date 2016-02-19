package org.lacombej.twl;

/**
 * Interface for a Mouse
 * 
 * @author Jonathan Lacombe
 *
 */
public interface Mouse {

    /**
     * Gets the next mouse event. After this is called you can:
     * </br>
     * Get the button that caused the event</br>
     * Get the mouse position at the event</br>
     * Get the state of the event</br>
     * Get the mouse scroll offset</br>
     * @see #getEventButton()
     * @see #getEventX()
     * @see #getEventY()
     * @see #getEventState()
     * @see #getEventDWheel()
     * @return true iff a mouse event was read
     */
    public boolean next();
    
    /** @return the button corresponding to the event */
    public int getEventButton();
    
    /** @return the mouse x position at the event */
    public int getEventX();
    
    /** @return the mouse y position at the event */
    public int getEventY();
    
    /** @return the mouse scroll offset at the event */
    public int getEventDWheel();
    
    /** @return true if button was pressed or false if released */
    public boolean getEventButtonState();
    
    /** @return true if mouse is inside window */
    public boolean isInsideWindow();
    
}
