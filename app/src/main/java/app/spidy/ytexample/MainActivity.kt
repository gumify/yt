package app.spidy.ytexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.spidy.yt.core.YouTube
import app.spidy.yt.data.SearchVideo
import app.spidy.yt.data.Video
import app.spidy.yt.interfaces.SearchListener
import app.spidy.yt.interfaces.UrlListener
import app.spidy.yt.interfaces.VideoListener
import app.spidy.yt.utils.getMp3
import app.spidy.yt.utils.getMp4

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val yt = YouTube(this).initialize()

        yt.getVideo("bESGLojNYSo", object : VideoListener {
            override fun onSuccess(video: Video) {
                Log.d("hello", video.toString())
                val mp3Formats = video.getMp3()
                val mp4Formats = video.getMp4()
                yt.getDirectUrl(video.videoId, mp4Formats[0].token, object : UrlListener {
                    override fun onSuccess(url: String) {
                        Log.d("hello", url)
                    }

                    override fun onFail() {}
                    override fun onError() {}
                })
            }
            override fun onFail() {}
            override fun onError() {}
        })
    }
}