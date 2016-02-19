package org.lacombej.test;

import java.nio.IntBuffer;

import org.lacombej.twl.TLC;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {

    public long id;
    @SuppressWarnings("unused")
    private static GLFWErrorCallback errorCallback;
    
    public Window(String title, int width, int height) {
        GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
        
        if (GLFW.glfwInit() == GL11.GL_FALSE) {
            throw new IllegalStateException("GLFW initialization failed.");
        }
        
        id = GLFW.glfwCreateWindow(width,height,title,
                MemoryUtil.NULL,
                MemoryUtil.NULL);
        
        if (id == MemoryUtil.NULL) {
            throw new IllegalStateException("GLFW window creation failed.");
        }
        
        IntBuffer ib0 = IntBuffer.allocate(16);
        IntBuffer ib1 = IntBuffer.allocate(16);
        GLFW.glfwGetWindowSize(id,ib0,ib1);
        int x = (ib0.get(0)-width) / 2;
        int y = (ib1.get(0)-height) / 2;
        //System.out.println(ib0.get(0));
        GLFW.glfwSetWindowPos(id,x,y);
        
        GLFW.glfwMakeContextCurrent(id);
        GL.createCapabilities();
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
