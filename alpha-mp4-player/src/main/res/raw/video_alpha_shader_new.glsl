#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 color = texture2D(sTexture, vTextureCoord);
  vec4 color2Map = vec4(1.0, 1.0, 1.0, 1.0);
  float textureX = (vTextureCoord.x * float(960))-float(1);
  if (textureX >= float(240)) {
     float alphaX = (textureX-float(240)) /float(3);
     color2Map = texture2D(sTexture, vec2(alphaX/float(960) , vTextureCoord.y));

    float a = alphaX/float(3);
    int index =  int(alphaX - a*float(3));

     if(index == 0){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.r);
     }else if(index == 1){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.g);
     }else if(index == 2){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.b);
     }
  }
}