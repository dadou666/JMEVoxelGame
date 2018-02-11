
attribute vec3 vertex;
attribute vec2 coordTexture2D;
varying vec2 vcoordTexture2D ;
uniform vec3 center;
uniform float echelle;
void main(void)
{

   
   gl_Position = vec4(center+(vertex-center)*echelle,1.0)  ;

   vcoordTexture2D=coordTexture2D;

}