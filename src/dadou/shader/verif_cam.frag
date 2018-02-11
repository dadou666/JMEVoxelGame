uniform sampler3D texture0;
uniform sampler3D texture1;
varying vec3 coordTex ;
void main(void) {


vec4 color0 =texture3D(texture0, coordTex);
vec4 color1 =texture3D(texture1, coordTex);
vec4 empty = vec4(0,0,0,1.0);

if (color0.xyz == empty.xyz && color1.xyz ==empty.xyz ) {
//gl_FragColor =vec4(coordTex,1.0);
if (empty.w == 1) {
	discard; } else {
	 gl_FragDepth=1.0;
	}
	} else {
//gl_FragColor =vec4(coordTex,1.0);
 gl_FragDepth=1.0;
   }

}