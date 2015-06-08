attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute float a_alpha;

uniform mat4 u_projTrans;

varying float v_alpha;
varying vec2 v_texCoords;

void main() {
    v_alpha = a_alpha;
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}