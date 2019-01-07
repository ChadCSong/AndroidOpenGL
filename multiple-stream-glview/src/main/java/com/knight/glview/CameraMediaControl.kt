package com.knight.glview

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import com.knight.glview.CameraCapture
import com.knight.glview.R
import java.io.IOException

class CameraMediaControl(val context: Context) : LifecycleObserver {

    val mediaPlayer: MediaPlayer = MediaPlayer()

    fun prepare() {
        CameraCapture.get().openBackCamera()
        try {
            mediaPlayer.setDataSource(context,
                    Uri.parse("android.resource://" + context.packageName + "/" + R.raw.video1)
            )
            mediaPlayer.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun bindSurface(surfaces: List<SurfaceTexture>) {
        if (!CameraCapture.get().isPreviewing) {
            CameraCapture.get().doStartPreview(surfaces[0])
        }
        mediaPlayer.setSurface(Surface(surfaces[1]))
        mediaPlayer.start()
        mediaPlayer.setVolume(0f, 0f)
        mediaPlayer.isLooping = true
    }

    fun switchCamera() {
        CameraCapture.get().switchCamera(1)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mediaPlayer.pause()
        CameraCapture.get().doStopCamera()
    }
}