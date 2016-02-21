package org.lacombej.twl;

/**
 * Interface for a window to be used with TLC
 * <p>
 * A window event is queued in the following conditions:
 * <ul>
 * <li>Window is moved: WindowEvent(width,height)</li>
 * <ul>
 * </p>
 * 
 * @author Jonathan Lacombe
 */
public interface Window {
    
    /**
     * Gets the next window event. After this is called you can:
     * </br>
     * Get the window position at the event</br>
     * @see #getEventWidth()
     * @see #getEventHeight()
     * @return true iff a mouse event was read
     */
    public boolean next();
    
    /** @return the window width at the event */
    public int getEventWidth();
    
    /** @return the window height at the event */
    public int getEventHeight();
    
    /**
     * Called every frame by GUI update
     * </br>
     * This should handle necessary keyboard and
     * mouse updates
     */
    public void update();

    /** @return keyboard associated with this window */
    public Keyboard keyboard();
    
    /** @return mouse associated with this window */
    public Mouse mouse();
    
    /** @return the GLFW window id */
    public long id();
    
    /** @return window width */
    public int width();
    
    /** @return window height */
    public int height();
    
    /** @return true if window is active */
    public boolean isActive();
    
    /** Sets the cursor for this window */
    public void setCursor(long cursorID);
    
}
