
attribute vec3 vertex;
attribute vec2 coordTexture2D;
varying vec2 vcoordTexture2D ;

void main(void)
{

   
   gl_Position = vec4(vertex,1.0)  ;

   vcoordTexture2D=coordTexture2D;

}