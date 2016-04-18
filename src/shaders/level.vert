#version 430

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 texCoords;

out vec2 fragTexCoords;
out vec2 fragWorldCoords;

uniform mat4 projMatrix;

void main() {
    fragTexCoords = texCoords;
    fragWorldCoords = inPosition.xy;
    gl_Position = projMatrix * vec4(inPosition, 1.0);
}