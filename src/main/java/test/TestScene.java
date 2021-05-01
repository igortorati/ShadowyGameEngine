package test;

import org.lwjgl.BufferUtils;
import renderer.Shader;
import sge.Scene;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TestScene extends Scene {
    private float[] vertexes = {
            //Vec3 Pos.         //RGBA Color
            1.0f, -1.0f, 0.0f,  1.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f,  0.0f, 0.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 0.0f,  1.0f, 1.0f, 0.0f, 1.0f,
    };

    private int[] elementArray = {
            // first triangle   // second triangle
            2, 1, 0,            0, 1, 3
    };

    private int VAOID; // Vertex Array Object
    private int VBOID; // Vertex Buffer Object
    private int EBOID; // Element Buffer Object

    private Shader defaultShader;

    public TestScene() {
        System.out.println("Testing...");
    }

    @Override
    public void init() {
        this.defaultShader = new Shader("assets/shaders/basicShader.glsl");
        this.defaultShader.compile();

        VAOID = glGenVertexArrays(); // Generate VAO unique ID
        glBindVertexArray(VAOID); // Mark to do things on the object with this ID

        FloatBuffer vertexesBuffer = BufferUtils.createFloatBuffer(vertexes.length); // Create Float Buffer
        vertexesBuffer.put(vertexes).flip(); // put vertexes and orient it correctly for OpenGL

        VBOID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBOID); // Set it to use this buffer
        glBufferData(GL_ARRAY_BUFFER, vertexesBuffer, GL_DYNAMIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip(); // put elements and orient it correctly for OpenGL

        EBOID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_DYNAMIC_DRAW);

        // Define what part of the vertexes array defines position and which defines color.
        int posSize = 3; // x, y, z
        int colorSize = 4; // R, G, B, A
        int vertexSizeInBytes = (posSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, posSize, GL_FLOAT, false, vertexSizeInBytes, 0); // Location 0
        // is position on the basicShader;
        glEnableVertexAttribArray(0); // Location 0

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, posSize * Float.BYTES);
        // Location 1 is position on the basicShader;
        glEnableVertexAttribArray(0); // Location 1
    }

    @Override
    public void update(float deltaTime) {
        // Bind Shader Program
        this.defaultShader.render();

        // Bind used VAO
        glBindVertexArray(VAOID);

        // Enable vertex attributes for position and color
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw on screen
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind attributes
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        // Unbind Shader Program
        defaultShader.detach();
    }
}
