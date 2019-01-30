#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 color = texture2D(sTexture, vTextureCoord);
  vec4 color2Map = vec4(1.0, 1.0, 1.0, 1.0);
  int textureX = int(vTextureCoord.x * float(960));
  if (textureX > 240) {
     int alphaX = (textureX-240) /3;
     color2Map = texture2D(sTexture, vec2(float(alphaX)/float(960) , vTextureCoord.y));

    int index =  (textureX-240) - alphaX*3;

     if(index == 0){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.r);
     }else if(index == 1){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.g);
     }else {
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.b);
     }
  }
}