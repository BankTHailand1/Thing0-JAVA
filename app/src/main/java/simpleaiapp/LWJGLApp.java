package simpleaiapp;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class LWJGLApp {
    private long window;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        // Set up error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Create a GLFW window
        window = GLFW.glfwCreateWindow(800, 600, "LWJGL 3D Window", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window
        GLFW.glfwSetWindowPos(window, 100, 100);

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        GLFW.glfwShowWindow(window);

        // Initialize OpenGL capabilities
        GL.createCapabilities();
    }

    private void loop() {
        float angle = 0.0f;
    
        // Run the rendering loop
        while (!GLFW.glfwWindowShouldClose(window)) {
            // Clear the frame
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    
            // Set background color
            GL11.glClearColor(0.1f, 0.2f, 0.3f, 0.0f);
    
            // Set up 3D projection
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(-1.5f, 1.5f, -1.5f, 1.5f, -1.5f, 1.5f);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
    
            // Rotate the cube
            GL11.glLoadIdentity();
            GL11.glRotatef(angle, 1.0f, 1.0f, 0.0f);
    
            // Draw a cube
            GL11.glBegin(GL11.GL_QUADS);
    
            // Front face
            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
            GL11.glVertex3f(0.5f, -0.5f, 0.5f);
            GL11.glVertex3f(0.5f, 0.5f, 0.5f);
            GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
    
            // Repeat for other faces...
    
            GL11.glEnd();
    
            // Increment rotation angle
            angle += 0.5f;
    
            // Swap buffers and poll events
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }
    

    private void cleanup() {
        // Free the window callbacks and destroy the window
        GLFW.glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new LWJGLApp().run();
    }
}

