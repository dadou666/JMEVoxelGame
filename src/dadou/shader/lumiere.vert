
uniform float size;
uniform vec3 position;
attribute vec3 vertex;

attribute vec2 coordTexture2D;
uniform mat4 modelView;

varying vec2 coordTex ;

void main(void)
{



   gl_Position = gl_ProjectionMatrix * (modelView*vec4(position,1.0) +vec4(2*vertex,1.0));

   coordTex = coordTexture2D;


}
          