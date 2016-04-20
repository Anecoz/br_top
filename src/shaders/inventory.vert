#version 430

layout (location = 0) in vec2 inPosition;
layout (location = 1) in vec2 texCoords;

out vec2 fragTexCoords;

uniform int isBackground;
uniform float scale = 1.0;
uniform float aspectRatio;

void main() {
    fragTexCoords = texCoords;
    // Are we drawing the background or an inventory item?
    if (isBackground == 1) {
        gl_Position = vec4(vec2(inPosition.x * aspectRatio, inPosition.y), 0.0, 1.0);
    }
    else {
        vec2 vertices = inPosition;
        // Transform from model space to screen space
        vertices.y = -vertices.y + 0.5;
        vertices.x = vertices.x - 0.5;

        vertices.x *= aspectRatio;
        vertices.x *= scale;
        vertices.y *= scale;
        gl_Position = vec4(vertices, 0.0, 1.0);
    }
}
