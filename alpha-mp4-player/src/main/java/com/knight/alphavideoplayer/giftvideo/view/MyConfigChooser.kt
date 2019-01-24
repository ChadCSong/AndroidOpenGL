package com.knight.alphavideoplayer.giftvideo.view

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLDisplay

class MyConfigChooser : GLSurfaceView.EGLConfigChooser {
    override fun chooseConfig(egl: EGL10, display: EGLDisplay?): EGLConfig? {
        val attribs = intArrayOf(
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_RENDERABLE_TYPE, 4,  // EGL_OPENGL_ES2_BIT
                EGL10.EGL_ALPHA_SIZE,8,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_SAMPLE_BUFFERS, 1,
                EGL10.EGL_SAMPLES, 4,  // 在这里修改MSAA的倍数，4就是4xMSAA，再往上开程序可能会崩
                EGL10.EGL_NONE
        )
        val configs = Array<EGLConfig?>(1) { null }
        val configCounts = IntArray(1)
        egl.eglChooseConfig(display, attribs, configs, 1, configCounts)

        if (configCounts[0] == 0) {
            // Failed! Error handling.
            return null
        } else {
            return configs[0]!!

        }
    }
}