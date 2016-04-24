#version 430

#define PI 3.14159265359

layout (location = 0) out vec4 outColor;
in vec2 fragTexCoords;
in vec2 fragWorldCoords;

uniform sampler2D atlas;
uniform sampler2D shadowTex;
uniform vec2 playerPos;
uniform int worldWidth;
uniform int worldHeight;
uniform float windowSizeX;
uniform float windowSizeY;
uniform vec2 camPos;

//uniform vec2 lightArr[50];
uniform vec2 lightPos;
uniform int numLights = 0;

const float DAMP_FACTOR = 0.2;
const float EPS = 0.003;

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
    vec4 col = texture(atlas, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
    if (col.a < 0.5) {
        discard;
    }
    outColor = col;

    // Shadows
    // This does the player visibility
    if (distance(fragWorldCoords, playerPos) < windowSizeX) {
        if (posInsideShadow(fragWorldCoords, 1) == 1) {
            outColor = col*0.9;
        }
    }

    if (numLights > 0) {
        //vec2 lightPos = lightArr[0];
        if (distance(fragWorldCoords, lightPos) < windowSizeX) {
            float maxDist = 5.0;
            float dist = 1.5 - clamp(distance(lightPos, fragWorldCoords)/maxDist, 0.0, 1.0);
            if (posInsideShadow(fragWorldCoords, 0) == 1) {
                outColor *= DAMP_FACTOR;
            }
            /*else {
                outColor *= pow(dist, 3);
            }*/
        }
    }
}

// Sample several closeby points as well
            /*float dist = distance(playerPos, fragWorldCoords)/30.0;
            float blur = (1.0 / float(windowSize)) * smoothstep(0.0, 1.0, dist);

            float sum = 0.0;

            sum += amanatideTraverse(fragWorldCoords + vec2(0.05, 0.0), playerPos);
            sum += amanatideTraverse(fragWorldCoords + vec2(0.09, 0.0), playerPos);
            sum += amanatideTraverse(fragWorldCoords + vec2(-0.05, 0.0), playerPos);
            sum += amanatideTraverse(fragWorldCoords + vec2(-0.09, 0.0), playerPos);

            sum += amanatideTraverse(fragWorldCoords + vec2(0.0, 0.05), playerPos);
            sum += amanatideTraverse(fragWorldCoords + vec2(0.0, 0.09), playerPos);
            sum += amanatideTraverse(fragWorldCoords + vec2(0.0, -0.05), playerPos);
            sum += amanatideTraverse(fragWorldCoords + vec2(0.0, -0.09), playerPos);

            sum = sum/8.0;

            outColor = col * (1.0/sum);*/