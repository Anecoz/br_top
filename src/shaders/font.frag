#version 330

in vec2 pass_textureCoords;

out vec4 out_colour;

uniform vec3 colour;
uniform sampler2D fontAtlas;

// For border effect
uniform float width;
uniform float edge;

uniform float borderWidth;
uniform float borderEdge;

const vec3 outlineColour = vec3(0.0);

void main(void){

    float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);

    float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords).a;
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColour = mix(outlineColour, colour, alpha / overallAlpha);

	out_colour = vec4(overallColour, overallAlpha);

}