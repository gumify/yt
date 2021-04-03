package app.spidy.yt.interfaces

import app.spidy.yt.data.Video

interface VideoListener {
    fun onSuccess(video: Video)
    fun onFail()
    fun onError()
}