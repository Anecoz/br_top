#version 430

layout (location = 0) out vec4 outColor;
in vec2 fragTexCoords;
in vec2 fragWorldPos;

uniform sampler2D tex;
//uniform vec2 lightArr[50];
uniform vec2 lightPos;
uniform int numLights = 0;

void main() {
    outColor = texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
    if (outColor.a < 0.5) {
        discard;
    }

    // Dampen by distance to lights
    if (numLights > 0) {
        float maxDist = 5.0;
        //vec2 lightPos = lightArr[0];

        float dist = 1.5 - clamp(distance(lightPos, fragWorldPos)/maxDist, 0.0, 1.0);
        outColor *= pow(dist, 3);
    }
}