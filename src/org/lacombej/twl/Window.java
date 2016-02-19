package org.lacombej.twl;

/**
 * Interface for a window to be used with TLC
 * 
 * @author Jonathan Lacombe
 */
public interface Window {
    
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
