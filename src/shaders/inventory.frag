#version 430

out vec4 outColor;

in vec2 fragTexCoords;

uniform int isBackground;
uniform sampler2D itemTex;

void main() {
    if (isBackground == 1) {
        outColor = vec4(0.0, 0.0, 0.0, 0.7);
    }
    else {
        outColor = texture(itemTex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
        if (outColor.a < 0.5) {
            outColor = vec4(0.0, 0.0, 0.0, 0.5);
        }
    }
}
