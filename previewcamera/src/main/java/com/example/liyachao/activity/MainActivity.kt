package com.example.liyachao.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import com.example.liyachao.R
import com.example.liyachao.utils.FileUtil
import com.knight.alphavideoplayer.giftvideo.VideoController
import com.knight.glview.CameraMediaControl
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author liyachao 296777513
 * @version 1.0
 * @date 2017/3/1
 */
class MainActivity : Activity(), View.OnClickListener {


    lateinit var control: CameraMediaControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
            }
        }
        val videoController = VideoController(mRoot, isLoop = false, playerType = VideoController.IJKPLAYER)
        videoController.prepareVideo(FileUtil.initPath() + "Alarms/unicorn.mp4")
        videoController.start()
        FileUtil.initPath()
        mSwitchCamera.setOnClickListener(this)
        control = mCameraGLSurfaceView.mediaControl
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        //        mCameraGLSurfaceView.bringToFront();
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
        //        mCameraGLSurfaceView.onPause();
    }


    override fun onClick(v: View) {
        control.switchCamera()
    }
}
