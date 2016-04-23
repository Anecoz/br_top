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
uniform int windowSize;

//uniform vec2 lightArr[50];
uniform vec2 lightPos;
uniform int numLights = 0;

const float DAMP_FACTOR = 0;
const float EPS = 0.003;

// Determines if a world position is inside a shadow caster or not
int posInsideShadow(vec2 coords) {
    float shadow = texture(shadowTex, coords).r*255.0;
    if (shadow > 0.5) {
        return 1;
    }
    else {
        return 0;
    }
}

// Helpers for amanatide
int signum(float val) {
	if (val < 0.0) {return -1;}
	else {return 1;}
}

float intbounds(float s, float ds)
{
	return (ds > 0? ceil(s)-s: s-floor(s)) / abs(ds);
}

// Smarter ray traversal algorithm
int amanatideTraverse(vec2 initPos, vec2 pos) {
	vec2 currPos = initPos;
	// Init phase
	int X = int(floor(currPos.x));
	int Y = int(floor(currPos.y));

	// Origin (floats)
	float Ox = currPos.x;
	float Oy = currPos.y;

	vec2 dir = normalize(pos - currPos);
	float dx = dir.x;
	float dy = dir.y;

	int StepX = signum(dx);
	int StepY = signum(dy);

	float tMaxX = intbounds(Ox, dx);
	float tMaxY = intbounds(Oy, dy);

	float tDeltaX = float(StepX)/dx;
	float tDeltaY = float(StepY)/dy;

	// Loop phase
	while ( (X != int(floor(pos.x))) || (Y != int(floor(pos.y))) ) {
		if(tMaxX < tMaxY) {
            X= X + StepX;
            tMaxX= tMaxX + tDeltaX;
		}
		else {
            Y= Y + StepY;
            tMaxY= tMaxY + tDeltaY;
		}
		int inside = posInsideShadow(vec2(float(X)/float(worldWidth - 1.0), float(Y)/float(worldHeight - 1.0)));
		if (inside == 1) {
			return 1;
		}
	}
	// We made it through the loop, so there was nothing in the way, return 1
	return 0;
}

void main() {
    vec4 col = texture(atlas, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
    if (col.a < 0.5) {
        discard;
    }
    outColor = col;

    // Shadows
    // This does the player visibility
    /*if (distance(fragWorldCoords, playerPos) < windowSize*2) {
        // Check if current frag is on a shadow caster
        if (posInsideShadow(vec2(fragWorldCoords.x/float(worldWidth), fragWorldCoords.y/float(worldHeight))) == 1) {
            outColor = col*DAMP_FACTOR;
        }
        if (amanatideTraverse(fragWorldCoords, playerPos) == 1) {
            outColor = col*DAMP_FACTOR;
        }
    }*/

    // This does shadows cast by light source
    if (numLights > 0) {
        //vec2 lightPos = lightArr[0];
        float maxDist = 5.0;
        float dist = 1.5 - clamp(distance(lightPos, fragWorldCoords)/maxDist, 0.0, 1.0);
        if (distance(fragWorldCoords, lightPos) < windowSize*2) {
            if (posInsideShadow(vec2(fragWorldCoords.x/float(worldWidth), fragWorldCoords.y/float(worldHeight))) == 1) {
                outColor = col*DAMP_FACTOR;
            }
            else if (amanatideTraverse(fragWorldCoords, lightPos+EPS) == 1) {
                outColor *= 0.1;
            }
            else {
                outColor *= pow(dist, 3);
            }
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