package org.lacombej.test;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

/**
 * Window class used to replace lwjgl2 Display
 * 
 * @author Jonathan Lacombe
 *
 */
public class Window {

    public long id;
    
    @SuppressWarnings("unused")
    private static GLFWErrorCallback errorCallback;
    
    public Window(String title, int width, int height, boolean fullscreen, boolean resizable, boolean vsync) {
        GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
        
        if (GLFW.glfwInit() == GL11.GL_FALSE) {
            throw new IllegalStateException("GLFW initialization failed.");
        }
        
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resizable ?
                GL11.GL_TRUE : GL11.GL_FALSE);
        
        id = GLFW.glfwCreateWindow(width,height,title,
                fullscreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL,
                MemoryUtil.NULL);
        
        if (id == MemoryUtil.NULL) {
            throw new IllegalStateException("GLFW window creation failed.");
        }
        
        ByteBuffer mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        int x = (GLFWvidmode.width(mode) - width) / 2;
        int y = (GLFWvidmode.height(mode) - height) / 2;
        
        GLFW.glfwSetWindowPos(id,x,y);
        
        GLFW.glfwMakeContextCurrent(id);
        GL.createCapabilities();
        
        //Limits to 60 fps (might be monitor dependent?)
        if (vsync) {
            GLFW.glfwSwapInterval(1);
        }
    }
    
    public Window(String title, int width, int height, boolean fullscreen) {
        this(title,width,height,fullscreen,false,true);
    }
    
    public Window(String title, int width, int height) {
        this(title,width,height,false,false,true);
    }
    
    public Window(String title) {
        this(title,800,600);
    }
    
    public Window(int width, int height) {
        this("GLFW Window",800,600);
    }
    
    public Window() {
        this("GLFW Window");
    }
    
    public boolean isRunning() {
        return GLFW.glfwWindowShouldClose(id)==GL11.GL_FALSE;
    }
    
    public void update() {
        GLFW.glfwSwapBuffers(id);
        GLFW.glfwPollEvents();
    }
    
    public void destroy() {
        GLFW.glfwDestroyWindow(id);
    }
    
}
