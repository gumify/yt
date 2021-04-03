package app.spidy.yt.interfaces

import app.spidy.yt.data.SearchVideo

interface UrlListener {
    fun onSuccess(url: String)
    fun onFail()
    fun onError()
}