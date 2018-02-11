uniform sampler2D baseTexture;
varying vec2 coordTex ;
uniform float transparence;


void main(void)
{
   vec4 baseColorTmp ;
  
   baseColorTmp =  texture2D( baseTexture, vec2(1.0-coordTex.x,coordTex.y) ); 
   if (baseColorTmp.rgba == vec4(0,0,0,1) ) { discard; } else {
           gl_FragData[0]	=vec4(baseColorTmp.rgb,transparence);
   
             gl_FragDepth = 0.0000;
           
	gl_FragData[1]		=vec4(0.777,0,0,0);
	gl_FragData[2]		=vec4(0,0,0,1.0);

	gl_FragData[3] = vec4(0,0,0,0.0);

}   
}