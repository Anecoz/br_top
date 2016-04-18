#version 430

layout (location = 0) out vec4 outColor;
in vec2 fragTexCoords;

uniform sampler2D tex;

void main() {
    outColor = texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
    if (outColor.a < 0.5) {
        discard;
    }
}