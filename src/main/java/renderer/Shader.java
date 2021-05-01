/**
 * Shader class. This class deals with the shader configuration and rendering
 * for OpenGL.
 * */
package renderer;

import org.lwjgl.opengl.GL;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    /**
     * Stores the identifier for the specific shader: {@link #shaderID}
     * */
    private int shaderID;

    /**
     * Stores the path to the shader file, typically "assets/shaders/...": {@link #path}
     * */
    private String path;

    /**
     * Stores the vertex shader configuration: {@link #vertexShaderSource}
     * */
    private String vertexShaderSource;

    /**
     * Stores the fragment shader configuration: {@link #fragmentShaderSource}
     * */
    private String fragmentShaderSource;

    /**
     * Constructor for the Shader class. Loads both sources from shader .glsl file.
     * */
    public Shader (String path) {
        this.path = path;
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8); // Load content
            // from shader file to a string
            String[] shadersSource = content.split("(#type)( )+([a-zA-Z]+)"); // Split string using RegExp, this
            // is used to separate vertex shader from fragment shader on the same file.

            int cursor = content.indexOf("#type") + 6; // Find the place right after the end of the word '#type'.
            int eol = content.indexOf(System.lineSeparator(), cursor); // Find the end of the line.

            String firstMatch = content.substring(cursor, eol).trim(); // Find the type name, the content just after
            // '#type'

            cursor = content.indexOf("#type", eol) + 6; // Find position of the next declaration of '#type'
            eol = content.indexOf(System.lineSeparator(), cursor); // Find the end of the line.

            String secondMatch = content.substring(cursor, eol).trim(); // Find type name of the second type.

            if (firstMatch.equals("vertex")) {
                vertexShaderSource = shadersSource[1];
            } else if (firstMatch.equals("fragment")) {
                fragmentShaderSource = shadersSource[1];
            } else {
                throw new IOException("Invalid type name '" + firstMatch + "' in '" + path + "'.");
            }

            if (secondMatch.equals("vertex")) {
                vertexShaderSource = shadersSource[2];
            } else if (secondMatch.equals("fragment")) {
                fragmentShaderSource = shadersSource[2];
            } else {
                throw new IOException("Invalid type name '" + secondMatch + "' in '" + path + "'.");
            }


        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("Could not open file for shader '" + path + ".'");
        }
        System.out.println(vertexShaderSource);
        System.out.println(fragmentShaderSource);
    }

    private int compileVertexShader() {
        // Creating and compiling vertex shader
        int vertexShaderID;
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSource);
        glCompileShader(vertexShaderID);

        // Verify if any problem happened in compilation
        if(glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("ERROR: Could not compile vertex shader from '"+ this.path + "'.");
            System.out.println(glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));
        }
        return vertexShaderID;
    }

    private int compileFragmentShader() {
        // Creating and compiling fragment shader
        int fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSource);
        glCompileShader(fragmentShaderID);

        // Verify if any problem happened in compilation
        if(glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("ERROR: Could not compile vertex shader from '"+ this.path + "'.");
            System.out.println(glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));
        }
        return fragmentShaderID;
    }

    private void linkShaders(int vertexShaderID, int fragmentShaderID) {
        // Linking Shaders
        this.shaderID = glCreateProgram();
        glAttachShader(this.shaderID, vertexShaderID);
        glAttachShader(this.shaderID, fragmentShaderID);
        glLinkProgram(this.shaderID);

        // Verify if any problem happened while linking
        if(glGetProgrami(this.shaderID, GL_LINK_STATUS) == GL_FALSE) {
            System.out.println("ERROR: Could not link shaders from '"+ this.path + "'.");
            System.out.println(glGetProgramInfoLog(vertexShaderID, glGetProgrami(this.shaderID, GL_INFO_LOG_LENGTH)));
        }
    }

    public void compile() {
        int vertexShaderID = compileVertexShader();

        int fragmentShaderID = compileFragmentShader();

        linkShaders(vertexShaderID, fragmentShaderID);
    }

    public void render () {
        glUseProgram(shaderID);
    }

    public void detach() {
        glUseProgram(0);
    }
}
