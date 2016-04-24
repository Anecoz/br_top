#version 430

layout (location = 0) in vec2 inPosition;

uniform mat4 projMatrix;
uniform mat4 modelMatrix;

out vec2 fragWorldPos;

void main() {
    fragWorldPos = (modelMatrix * vec4(inPosition, 0.0, 1.0)).xy;
	gl_Position = projMatrix * modelMatrix * vec4(inPosition, 0.0, 1.0);
}
