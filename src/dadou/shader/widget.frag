
uniform sampler2D texture;
uniform vec4 discardColor;
uniform vec4 couleurTransparente;
varying vec2 vcoordTexture2D ;


void main(void)
{
   vec4 baseColorTmp ;
  
    baseColorTmp =  texture2D( texture, vcoordTexture2D ); 
   if (baseColorTmp.xyz == discardColor.xyz ) { discard; } else{
   if (baseColorTmp.xyz == couleurTransparente.xyz ) {
   baseColorTmp.w=couleurTransparente.w;
   }
     
        gl_FragData[0]		= baseColorTmp;
	gl_FragData[1]		= vec4(0.666,0,0,0);
	gl_FragData[2]		=vec4(0.666,0,0,1.0);

	gl_FragData[3] = vec4(0,0,0,0.0);
   gl_FragDepth = 0.00001;}
}