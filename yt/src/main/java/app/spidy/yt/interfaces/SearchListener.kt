package app.spidy.yt.interfaces

import app.spidy.yt.data.SearchVideo

interface SearchListener {
    fun onSuccess(searchVideos: List<SearchVideo>)
    fun onFail()
    fun onError()
}