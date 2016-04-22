#version 430

layout (location = 0) in vec2 inPosition;
layout (location = 1) in vec2 texCoords;

out vec2 fragTexCoords;

uniform int isBackground;
uniform float scale = 1.0;
uniform float baseScale = 1.0;

uniform int itemsPerRow = 5;
uniform int itemIndex;

uniform mat4 projMatrix;
uniform mat4 modelMatrix;

void main() {
    fragTexCoords = texCoords;

    // Are we drawing the background or an inventory item?
    if (isBackground == 1) {
        vec2 verts = inPosition*baseScale;
        gl_Position = projMatrix * modelMatrix * vec4(verts, 0.0, 1.0);
    }
    else {
        vec2 vertices = inPosition;
        // Get this particular item's position relative to window
        float y = floor(float(itemIndex)/float(itemsPerRow));
        float x = float(itemIndex) - float(itemsPerRow * y);
        y /= itemsPerRow;
        x /= itemsPerRow;

        vertices.x *= scale;
        vertices.y *= scale;
        vertices.x += x*baseScale;
        vertices.y += y*baseScale;

        gl_Position = projMatrix * modelMatrix * vec4(vertices, 0.0, 1.0);
    }
}