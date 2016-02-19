package org.lacombej.test;

public class Test {

    public static void main(String[]args) {
        
        Window window = new Window();
        
        while (!window.isRunning()) {
            
            window.update();
        }
        
        window.destroy();
        
    }
    
}
