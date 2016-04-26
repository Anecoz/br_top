#version 430

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 texCoords;
//layout (location = 1) in vec4 color;  // This attribute list is __not__ set for the VAOs

out vec2 fragTexCoords;
//out vec4 fragColor;

uniform mat4 projMatrix;
uniform mat4 modelMatrix;
uniform mat4 rotationMatrix = mat4(1.0f);

void main() {
    //fragColor = color;
    fragTexCoords = texCoords;
    gl_Position = projMatrix * rotationMatrix * modelMatrix * vec4(inPosition, 1.0);
}
