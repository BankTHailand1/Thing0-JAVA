package threeDApp;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.system.MemoryUtil;

public class theCube {
    // Window dimensions and projection parameters
    private long window;
    private final int ywin = 720, xwin = (int)(ywin * 1.6);
    private final float fov = 90.0f;
    private final float aspectRatio = 1.6f;
    private final float nearClip = 0.1f, farClip = 1000.0f;
    
    // Our free camera
    private FreeCam camera;
    
    // --------------------------
    // FreeCam inner class
    // --------------------------
    public static class FreeCam {
        public Vector3f position;
        public Vector3f front;
        public Vector3f up;
        public float yaw, pitch;
        public float camSpeed = 0.1f;
        public float sensitivity = 0.1f;
        
        public FreeCam(Vector3f startPos) {
            this.position = startPos;
            this.front = new Vector3f(0, 0, -1);
            this.up = new Vector3f(0, 1, 0);
            this.yaw = -90.0f;   // Facing towards -Z
            this.pitch = 0.0f;
        }
        
        // Process relative mouse movement
        public void processMouseInput(double deltaX, double deltaY) {
            yaw += deltaX * sensitivity;
            pitch -= deltaY * sensitivity; // Subtract so that moving mouse up makes pitch go up
            
            // Clamp pitch to prevent flipping
            if (pitch > 89.0f) pitch = 89.0f;
            if (pitch < -89.0f) pitch = -89.0f;
            
            updateCameraVectors();
        }
        
        // Update the front vector based on yaw and pitch
        private void updateCameraVectors() {
            Vector3f newFront = new Vector3f();
            newFront.x = (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
            newFront.y = (float)Math.sin(Math.toRadians(pitch));
            newFront.z = (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
            front.set(newFront.normalize());
        }
        
        public Matrix4f getViewMatrix() {
            // Create a view matrix that looks from position toward position+front
            return new Matrix4f().lookAt(position, new Vector3f(position).add(front), up);
        }
        
        // Movement functions use normalized directions
        public void moveForward(float amount) {
            position.add(new Vector3f(front).mul(amount * camSpeed));
        }
        public void moveBackward(float amount) {
            position.sub(new Vector3f(front).mul(amount * camSpeed));
        }
        public void moveLeft(float amount) {
            Vector3f left = new Vector3f();
            front.cross(up, left).normalize();
            position.sub(left.mul(amount * camSpeed));
        }
        public void moveRight(float amount) {
            Vector3f right = new Vector3f();
            up.cross(front, right).normalize();
            position.sub(right.mul(amount * camSpeed));
        }
        public void moveUp(float amount) {
            position.y += amount * camSpeed;
        }
        public void moveDown(float amount) {
            position.y -= amount * camSpeed;
        }
    }
    
    // --------------------------
    // Main game loop and setup
    // --------------------------
    public void run() {
        init();
        loop();
        glfwDestroyWindow(window);
        glfwTerminate();
    }
    
    private void init() {
        System.out.println("Initializing GLFW...");
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        System.out.println("Creating window...");
        window = glfwCreateWindow(xwin, ywin, "3D Cube - LWJGL", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        System.out.println("Window created successfully!");
        
        glfwMakeContextCurrent(window);
        // Lock and hide the cursor (for FPS-style free look)
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        
        // Initialize camera; position it a bit away from origin
        camera = new FreeCam(new Vector3f(0, 0, 3));
    }
    
    // Set up perspective projection using glFrustum
    private void setPerspective(float fov, float aspect, float near, float far) {
        float ymax = (float)(near * Math.tan(Math.toRadians(fov / 2)));
        float xmax = ymax * aspect;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustum(-xmax, xmax, -ymax, ymax, near, far);
        glMatrixMode(GL_MODELVIEW);
    }
    
    // Process mouse input: calculate delta relative to window center
    private void processMouse() {
        double centerX = xwin / 2.0;
        double centerY = ywin / 2.0;
        double[] mouseX = new double[1];
        double[] mouseY = new double[1];
        glfwGetCursorPos(window, mouseX, mouseY);
        
        double deltaX = mouseX[0] - centerX;
        double deltaY = mouseY[0] - centerY;
        
        camera.processMouseInput(deltaX, deltaY);
        // Reset cursor position to center every frame
        glfwSetCursorPos(window, centerX, centerY);
    }
    
    // Process keyboard input with GLFW
    private void processKeyboard() {
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            camera.moveForward(1);
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            camera.moveBackward(1);
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            camera.moveLeft(1);
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            camera.moveRight(1);
        }
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            camera.moveUp(1);
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            camera.moveDown(1);
        }
    }
    
    private void loop() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        
        while (!glfwWindowShouldClose(window)) {
            // Process input each frame
            processKeyboard();
            processMouse();
            
            // Clear frame buffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            // Set projection matrix
            setPerspective(fov, aspectRatio, nearClip, farClip);
            
            // Load camera view matrix (update transformation)
            float[] viewMatrix = new float[16];
            camera.getViewMatrix().get(viewMatrix);
            glLoadMatrixf(viewMatrix);
            
            // Draw scene: here, a single cube
            drawCube();
            
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
    
    // Simple cube drawing with GL_QUADS (immediate mode)
    private void drawCube() {
        glBegin(GL_QUADS);
        
        // Front face (red)
        glColor3f(1, 0, 0);
        glVertex3f(-1, -1, 1);
        glVertex3f(1, -1, 1);
        glVertex3f(1, 1, 1);
        glVertex3f(-1, 1, 1);
        
        // Back face (green)
        glColor3f(0, 1, 0);
        glVertex3f(-1, -1, -1);
        glVertex3f(-1, 1, -1);
        glVertex3f(1, 1, -1);
        glVertex3f(1, -1, -1);
        
        // Top face (blue)
        glColor3f(0, 0, 1);
        glVertex3f(-1, 1, -1);
        glVertex3f(-1, 1, 1);
        glVertex3f(1, 1, 1);
        glVertex3f(1, 1, -1);
        
        // Bottom face (yellow)
        glColor3f(1, 1, 0);
        glVertex3f(-1, -1, -1);
        glVertex3f(1, -1, -1);
        glVertex3f(1, -1, 1);
        glVertex3f(-1, -1, 1);
        
        // Right face (magenta)
        glColor3f(1, 0, 1);
        glVertex3f(1, -1, -1);
        glVertex3f(1, 1, -1);
        glVertex3f(1, 1, 1);
        glVertex3f(1, -1, 1);
        
        // Left face (cyan)
        glColor3f(0, 1, 1);
        glVertex3f(-1, -1, -1);
        glVertex3f(-1, -1, 1);
        glVertex3f(-1, 1, 1);
        glVertex3f(-1, 1, -1);
        
        glEnd();
    }
    
    public static void main(String[] args) {
        new theCube().run();
    }
}
