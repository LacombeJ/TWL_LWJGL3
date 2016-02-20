package org.lacombej.test;

import java.io.IOException;
import java.net.URL;
import org.lacombej.twl.TLC;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import sourceviewer.demo.Demo;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class Test {
    
    public static void main(String[]args) throws IOException {
        //method1();
        method2();
    }
    
    public static void method1() throws IOException {
        
        if (GLFW.glfwInit() == GL11.GL_FALSE) {
            throw new IllegalStateException("GLFW initialization failed.");
        }
        
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);
        
        long windowID = GLFW.glfwCreateWindow(1200,600,"Test",
                MemoryUtil.NULL,
                MemoryUtil.NULL);
        
        if (windowID == MemoryUtil.NULL) {
            throw new IllegalStateException("GLFW window creation failed.");
        }
        
        GLFW.glfwMakeContextCurrent(windowID);
        GL.createCapabilities();

        /* Most important part! */
        TLC.create(windowID);
        
        LWJGLRenderer renderer = new LWJGLRenderer();
        Demo demo = new Demo();
        GUI gui = new GUI(demo, renderer);

        ThemeManager theme = ThemeManager.createThemeManager(
                new URL("file:src/sourceviewer/demo/demo.xml"), renderer);
        gui.applyTheme(theme);

        while(GLFW.glfwWindowShouldClose(windowID)==GL11.GL_FALSE && !demo.quit) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            gui.update();

            GLFW.glfwSwapBuffers(windowID);
            GLFW.glfwPollEvents();
        }

        gui.destroy();
        theme.destroy();
            
        GLFW.glfwDestroyWindow(windowID);
        
    }
    
    public static void method2() throws IOException {
        
        Window window = new Window("Test",1200,600);

        /* Most important part! */
        TLC.create(window.id);
        
        LWJGLRenderer renderer = new LWJGLRenderer();
        Demo demo = new Demo();
        GUI gui = new GUI(demo, renderer);

        ThemeManager theme = ThemeManager.createThemeManager(
                new URL("file:src/sourceviewer/demo/demo.xml"), renderer);
        gui.applyTheme(theme);

        while(window.isRunning() && !demo.quit) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            gui.update();
            window.update();
        }

        gui.destroy();
        theme.destroy();
        window.destroy();
        
    }
    
}
