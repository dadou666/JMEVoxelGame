uniform vec3 up;
uniform vec3 left;
uniform float size;

attribute vec3 vertex;

attribute vec2 coordTexture2D;

varying vec2 coordTex ;

void main(void)
{
   vec4 pos = size*(vertex.x*vec4(left,1.0)+vertex.y*vec4(up,1.0)) ;
    pos.w =1.0;


   gl_Position = gl_ModelViewProjectionMatrix * pos    ;

   coordTex = coordTexture2D;


}
          