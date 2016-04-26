//in vec4 fragColor;    // Not set in VAOs
in vec2 fragTexCoords;

out vec4 outColor;

uniform vec2 screenSize;
uniform sampler2D tex;
uniform vec4 time;
uniform vec2 velocity;

const float RADIUS = 0.75;

const float SOFTNESS = 0.6;

const float blurSize = 1.0/1000.0;

void main() {
    vec4 texColor = vec4(0.0); //texture2D(texture, fragTexCoords);
    //texColor = texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y));
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) - 4.0*blurSize) * 0.05;
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) - 3.0*blurSize) * 0.09;
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) - 2.0*blurSize) * 0.12;
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) - blurSize) * 0.15;
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y)) * 0.16;
    if (texColor.a < 0.5) { // Do this as soon as we know, in order to not waste resources
        discard;
    }
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) + blurSize) * 0.15;
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) + 2.0*blurSize) * 0.12;
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) + 3.0*blurSize) * 0.09;
    texColor += texture(tex, vec2(fragTexCoords.x, 1.0 - fragTexCoords.y) + 4.0*blurSize) * 0.05;

    //vec4 timedColor = (fragColor + time);

    vec2 position = (gl_FragCoord.xy / screenSize.xy) - vec2(0.5);
    float len = length(position);

    float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);

    texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);

    outColor = vec4(texColor.rgb, texColor.a);
}