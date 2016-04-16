#version 430

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 texCoords;

out vec2 fragTexCoords;

uniform mat4 projMatrix;
uniform mat4 modelMatrix;

void main() {
    fragTexCoords = texCoords;
    gl_Position = projMatrix * modelMatrix * vec4(inPosition, 1.0);
}