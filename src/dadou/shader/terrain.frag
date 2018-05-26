varying vec3 vnormal;
varying vec4 position;
varying float h;
varying  vec3 vcolor;
void main(void) {

	gl_FragData[0]= vec4(vcolor,0.0);
	gl_FragData[1]=vec4(dot(vnormal,position.xyz), length(position.xyz),0.0,0.0);

}
