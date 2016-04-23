#version 430

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 texCoords;

out vec2 fragTexCoords;
out vec2 fragWorldPos;

uniform mat4 projMatrix;
uniform mat4 modelMatrix;
uniform mat4 rotationMatrix = mat4(1.0f);

void main() {
    fragTexCoords = texCoords;
    fragWorldPos = (rotationMatrix * modelMatrix * vec4(inPosition, 1.0)).xy;
    gl_Position = projMatrix * rotationMatrix * modelMatrix * vec4(inPosition, 1.0);
}