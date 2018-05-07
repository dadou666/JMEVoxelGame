uniform sampler2D baseTexture;
varying vec2 coordTex ;


void main(void)
{
   vec4 baseColorTmp ;
  
   baseColorTmp =  texture2D( baseTexture, coordTex ); 
   if (baseColorTmp.rgba == vec4(0,0,0,1) ) discard;
   gl_FragData[0] = baseColorTmp.rgba;
    gl_FragDepth = 0.0000;
    	gl_FragData[1]		= vec4(0.670,0,0,0);

   
}