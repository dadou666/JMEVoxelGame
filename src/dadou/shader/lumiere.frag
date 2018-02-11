
varying vec2 coordTex ;
uniform float size;

void main(void)
{
  
  	float l=length(coordTex-vec2(0.5,0.5));
 if (l <=0.5) {
		 float t= clamp((1.0-l*2.0)/(size/2.0),0.0,1.0);
        
         gl_FragData[0]	= vec4(1.0,1.0,1.0,t);
        
     

} else {
   discard;
}

}