#version 430

layout (location = 0) out vec4 outColor;
in vec2 fragTexCoords;
in vec2 fragWorldPos;

uniform sampler2D tex;
uniform sampler2D shadowTex;
//uniform vec2 lightArr[50];
uniform vec2 lightPos;
uniform vec2 camPos;
uniform int numLights = 0;
uniform float windowSizeX;
uniform float windowSizeY;

const float DAMP_FACTOR = 0.2;

// Determines if a world position is inside a shadow caster or not
int posInsideShadow(vec2 coords, int channel) {
    // Convert world coordinates to texture coordinates
    vec2 texCoords = vec2((coords.x - camPos.x)/windowSizeX, (coords.y - camPos.y)/windowSizeY);

    float shadow;
    switch (channel) {
        case 0:
            shadow = texture(shadowTex, vec2(texCoords.x, 1.0 - texCoords.y)).r;
            break;
        case 1:
            shadow = texture(shadowTex, vec2(texCoords.x, 1.0 - texCoords.y)).g;
            break;
        default:
            break;
    }

    if (shadow > 0.5) {
        return 1;
    }
    else {
        return 0;
    }
}

void main() {
    vec4 col = texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
    if (col.a < 0.5) {
        discard;
    }

    outColor = col;

    // Shadows
    // This does the player visibility
    if (posInsideShadow(fragWorldPos, 1) == 1) {
        discard;
        //outColor *= 0;
    }
    else if (numLights > 0) {
         //vec2 lightPos = lightArr[0];
         if (distance(fragWorldPos, lightPos) < windowSizeX) {
             float maxDist = 5.0;
             float dist = 1.5 - clamp(distance(lightPos, fragWorldPos)/maxDist, 0.0, 1.0);
             if (posInsideShadow(fragWorldPos, 0) == 1) {
                 outColor *= DAMP_FACTOR;
             }
             /*else {
                 outColor *= pow(dist, 3);
             }*/
         }
    }
}