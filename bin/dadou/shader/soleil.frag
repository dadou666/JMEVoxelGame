
varying vec2 coordTex ;


void main(void)
{
  
  	float l=length(coordTex-vec2(0.5,0.5));
 if (l <=0.5) {
		 float t= 1.0-l*2.0;
        
         gl_FragData[0]	= vec4(1.0,1.0,1.0,t);
           gl_FragData[1]	= vec4(0.0,0.0,0.0,0);
         	gl_FragDepth=1.0;
     

} else {
   discard;
}

}