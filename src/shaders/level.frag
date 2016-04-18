#version 430

#define PI 3.14159265359

layout (location = 0) out vec4 outColor;
in vec2 fragTexCoords;
in vec2 fragWorldCoords;

uniform sampler2D atlas;
uniform sampler2D shadowTex;
uniform vec2 lightPos;
uniform int worldWidth;
uniform int worldHeight;

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
int amanatideTraverse(vec2 lightPos) {
	vec2 currPos = fragWorldCoords;

	// Init phase
	int X = int(floor(currPos.x));
	int Y = int(floor(currPos.y));

	// Origin (floats)
	float Ox = currPos.x;
	float Oy = currPos.y;

	vec2 dir = normalize(lightPos - currPos);
	float dx = dir.x;
	float dy = dir.y;

	int StepX = signum(dx);
	int StepY = signum(dy);

	float tMaxX = intbounds(Ox, dx);
	float tMaxY = intbounds(Oy, dy);

	float tDeltaX = float(StepX)/dx;
	float tDeltaY = float(StepY)/dy;

	// Loop phase
	while ( (X != int(floor(lightPos.x))) || (Y != int(floor(lightPos.y))) ) {
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
    // Only do it if we are within some bounds of light
    if (distance(fragWorldCoords, lightPos) < 15) {
        // Check if current frag is on a shadow caster
        if (posInsideShadow(vec2(fragWorldCoords.x/float(worldWidth), fragWorldCoords.y/float(worldHeight))) == 1) {
            outColor = col*0.2;
        }
        else if (amanatideTraverse(lightPos) == 1) {
            outColor = col*0.2;
        }
    }
}