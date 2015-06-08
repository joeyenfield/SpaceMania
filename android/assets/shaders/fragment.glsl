#ifdef GL_ES
    precision mediump float;
#endif

varying float v_alpha;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
         vec4 color = texture2D(u_texture, v_texCoords);
         color.a = v_alpha;
         gl_FragColor = color;
}