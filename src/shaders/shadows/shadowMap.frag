#version 430

in vec2 fragWorldPos;

out vec4 outColor;

uniform sampler2D collisionMap;
uniform vec2 playerPos;
uniform vec2 lightPos;
uniform int numLights;

uniform int worldWidth;
uniform int worldHeight;
uniform int windowSize;

const float EPS = 0.0003;

// Determines if a world position is inside a shadow caster or not
int posInsideShadow(vec2 coords) {
    float shadow = texture(collisionMap, coords).r*255.0;
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
    outColor = vec4(0.0, 0.0, 0.0, 1.0);
    // This does shadows cast by light source
    if (numLights > 0) {
        if (distance(fragWorldPos, lightPos) < windowSize) {
            if (posInsideShadow(vec2(fragWorldPos.x/float(worldWidth), fragWorldPos.y/float(worldHeight))) == 1) {
                outColor.r = 1.0;
            }
            else if (amanatideTraverse(fragWorldPos, lightPos+EPS) == 1) {
                outColor.r = 1.0;
            }
        }
    }
    // Player visibility calculations
    if (distance(fragWorldPos, playerPos) < windowSize) {
        // Check if current frag is on a shadow caster
        if (posInsideShadow(vec2(fragWorldPos.x/float(worldWidth), fragWorldPos.y/float(worldHeight))) == 1) {
            outColor.g = 1.0;
        }
        else if (amanatideTraverse(fragWorldPos, playerPos+EPS) == 1) {
            outColor.g = 1.0;
        }
    }
}
