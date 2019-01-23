#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 color = texture2D(sTexture, vTextureCoord);
  vec4 color2Map = vec4(1.0, 1.0, 1.0, 1.0);
  float temp = (vTextureCoord.x * float(960));
  if (temp > float(240)) {
     float tempx = (temp-float(240)) /float(3);
     color2Map = texture2D(sTexture, vec2(tempx/float(960) , vTextureCoord.y));

    float a = tempx/float(3);
    int index =  int(tempx - a*float(3));

     if(index == 0){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.r);
     }else if(index == 1){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.g);
     }else if(index == 2){
          gl_FragColor = vec4(color.r, color.g, color.b, color2Map.b);
     }
  }
}