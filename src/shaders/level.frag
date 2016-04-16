#version 430

layout (location = 0) out vec4 outColor;
in vec2 fragTexCoords;

uniform sampler2D atlas;

void main() {
    outColor = texture(atlas, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
}