package com.knight.alphavideoplayer.giftvideo

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.IntDef
import android.view.ViewGroup
import com.knight.alphavideoplayer.giftvideo.player.IPlayer
import com.knight.alphavideoplayer.giftvideo.player.VideoPlayerListener
import com.knight.alphavideoplayer.giftvideo.player.exo.ExoPlayer
import com.knight.alphavideoplayer.giftvideo.player.ijk.IjkPlayer
import com.knight.alphavideoplayer.giftvideo.player.system.MediaPlayer
import com.knight.alphavideoplayer.giftvideo.view.IAlphaView
import com.knight.alphavideoplayer.giftvideo.view.surfaceview.AlphaGLSurfaceView
import com.knight.alphavideoplayer.giftvideo.view.textureview.AlphaTextureView
import com.knight.alphavideoplayer.utils.LOG

class VideoController(val parent: ViewGroup, val isLoop: Boolean = false,
                      @PlayerType playerType: Int = PLAYER_IJK, @ViewType viewType: Int = VIEW_GLSURFACE) : LifecycleObserver {

    companion object {
        const val PLAYER_IJK = 1
        const val PLAYER_EXO = 2
        const val PLAYER_MEDIA = 3

        @IntDef(PLAYER_IJK, PLAYER_EXO, PLAYER_MEDIA)
        @Retention(AnnotationRetention.SOURCE)
        annotation class PlayerType


        const val VIEW_GLSURFACE = 4
        const val VIEW_GLTEXUTRE = 5

        @IntDef(VIEW_GLSURFACE, VIEW_GLTEXUTRE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ViewType
    }


    private val context = parent.context
    val player: IPlayer by lazy(LazyThreadSafetyMode.NONE) {
        when (playerType) {
            PLAYER_IJK -> IjkPlayer(listener, context, isLoop)
            PLAYER_EXO -> ExoPlayer(listener, context, isLoop)
            else -> MediaPlayer(listener, context, isLoop)
        }
    }

    val listener: VideoPlayerListener by lazy(LazyThreadSafetyMode.NONE) {
        object : VideoPlayerListener() {
            override fun onCompletion(player: IPlayer) {
                alphaView.clearTexture()
            }

            override fun onError(player: IPlayer, message: String) {
                super.onError(player, message)
            }

            override fun onPrepared(player: IPlayer) {
            }
        }
    }

    val alphaView: IAlphaView by lazy(LazyThreadSafetyMode.NONE) {
        when (viewType) {
            VIEW_GLSURFACE -> AlphaGLSurfaceView(context) as IAlphaView
            else -> AlphaTextureView(context) as IAlphaView
        }
    }

    init {
        alphaView.onSurfaceCreated = {
            player.setSurface(it)

        }
        parent.addView(alphaView.mView)
    }

    fun prepareVideo(mp4Path: String) {
        try {
            player.prepare(mp4Path)
        } catch (e: Exception) {
            e.printStackTrace()
            LOG.logE(e.toString())
        }

    }

    fun start() {
        player.start()
    }

    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
//        player.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
//        player.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
//        player.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        player.release()
    }

}