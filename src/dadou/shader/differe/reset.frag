

uniform vec3 color;



void main(void)
{
   gl_FragData[0]		= vec4(color,0.0);
	gl_FragData[1]		= vec4(0.0,0.0,0.0,1.0);
	gl_FragData[2]		=vec4(0.0,0.0,0.0,0.0);
  gl_FragDepth =0.99;

}