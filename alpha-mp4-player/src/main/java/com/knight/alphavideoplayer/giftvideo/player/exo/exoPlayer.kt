package com.knight.alphavideoplayer.giftvideo.player.exo

import android.content.Context
import android.graphics.SurfaceTexture
import android.net.Uri
import android.util.Log
import android.view.Surface
import bolts.Task
import bolts.Task.call
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.knight.alphavideoplayer.giftvideo.player.IPlayer
import com.knight.alphavideoplayer.giftvideo.player.VideoPlayerListener
import java.util.concurrent.Callable

class exoPlayer(override val listener: VideoPlayerListener, override val context: Context, override var isLoop: Boolean = false) : IPlayer {


    private val mMediaPlayer: SimpleExoPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(context).apply {
            addListener(object : Player.EventListener {
                override fun onPlayerError(error: ExoPlaybackException?) {
                    listener.onError(this@exoPlayer, error.toString())
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playWhenReady) {
                        listener.onPrepared(this@exoPlayer)
                    }

                    if (playbackState == 4) {
                        listener.onCompletion(this@exoPlayer)
                    }
                }
            })
        }
    }
    val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "player"))

    override fun start() {
//        mMediaPlayer.start()
        Log.i("liyachao222", "start")
//        mMediaPlayer.playWhenReady = true
        if (isLoop) {
            val loopingMediaSource = LoopingMediaSource(videoSource)
            mMediaPlayer.prepare(loopingMediaSource)
        } else {
            mMediaPlayer.prepare(videoSource)
        }
        mMediaPlayer.playWhenReady = true
    }

    override fun release() {
        mMediaPlayer.release()
    }

    override fun pause() {
        mMediaPlayer.playWhenReady = false
    }

    override fun stop() {
        mMediaPlayer.stop()
    }

    override fun reset() {
//        mMediaPlayer.reset()
    }

    override fun getDuration(): Long = mMediaPlayer.duration

    override fun buildUri(mp4Res: Any): Uri {
        return when {
            mp4Res is Int -> {
                val dataSpec = DataSpec(RawResourceDataSource.buildRawResourceUri(mp4Res))
                val rawResourceDataSource = RawResourceDataSource(context)
                rawResourceDataSource.open(dataSpec)
                rawResourceDataSource.uri!!
            }
            (mp4Res is String) and (mp4Res as String).endsWith(".mp4") -> Uri.parse("file:///$mp4Res")
            mp4Res is Uri -> mp4Res as Uri
            else -> throw RuntimeException("please check your mp4's resources")
        }
    }

    override fun setSurface(surfaceTexture: SurfaceTexture) {
        mMediaPlayer.setVideoSurface(Surface(surfaceTexture))

    }

    lateinit var videoSource: ExtractorMediaSource
    override fun prepare(mp4Res: Any) {
        videoSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(buildUri(mp4Res))
    }
}