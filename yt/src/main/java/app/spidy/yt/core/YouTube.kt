package app.spidy.yt.core

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import app.spidy.hiper.Hiper
import app.spidy.hiper.utils.mix
import app.spidy.kotlinutils.debug
import app.spidy.kotlinutils.onUiThread
import app.spidy.yt.data.SearchVideo
import app.spidy.yt.data.Video
import app.spidy.yt.data.VideoFormat
import app.spidy.yt.interfaces.SearchListener
import app.spidy.yt.interfaces.UrlListener
import app.spidy.yt.interfaces.VideoListener
import org.json.JSONObject
import java.lang.Exception

class YouTube(private val context: Context) {
    private val webView = WebView(context)
    private val hiper = Hiper.getInstance().async()

    @SuppressLint("SetJavaScriptEnabled")
    fun initialize(): YouTube {
        webView.settings.apply {
            javaScriptEnabled = true
        }
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        return this
    }

    fun search(query: String, listener: SearchListener) {
        hiper.post("https://yt1s.com/api/ajaxSearch/index",
                headers = mix("user-agent" to webView.settings.userAgentString),
                form = mix("q" to query, "vt" to "home")).resolve { resp ->
                    val json = JSONObject(resp.text!!).getJSONArray("items")
                    val videos = ArrayList<SearchVideo>()
                    for (i in 0 until json.length()) {
                        val item = json.getJSONObject(i)
                        videos.add(SearchVideo(videoId = item.getString("v"), title = item.getString("t")))
                    }
                    onUiThread { listener.onSuccess(videos) }
                }.reject {
                    onUiThread { listener.onFail() }
                }.catch {
                    onUiThread { listener.onError() }
                }
    }

    fun getVideo(videoId: String, listener: VideoListener) {
        hiper.post("https://yt1s.com/api/ajaxSearch/index",
                headers = mix("user-agent" to webView.settings.userAgentString),
                form = mix("q" to "https://www.youtube.com/watch?v=${videoId}", "y" to "", "vt" to "home"))
                .resolve { resp ->
                    try {
                        val json = JSONObject(resp.text!!)
                        val links = json.getJSONObject("links")
                        val title = json.getString("title")
                        val formats = ArrayList<VideoFormat>()

                        for (f in links.keys()) {
                            for (k in links.getJSONObject(f).keys()) {
                                val v = links.getJSONObject(f).getJSONObject(k)
                                formats.add(VideoFormat(token = v.getString("k"), format = f, quality = v.getString("q"), size = v.getString("size")))
                            }
                        }

                        val video = Video(title = title, formats = formats, videoId = videoId)
                        onUiThread { listener.onSuccess(video) }
                    } catch (e: Exception) {
                        onUiThread { listener.onError() }
                    }
                }
                .reject {
                    onUiThread { listener.onFail() }
                }
                .catch {
                    onUiThread { listener.onError() }
                }
    }

    fun getDirectUrl(videoId: String, token: String, listener: UrlListener) {
        hiper.post("https://yt1s.com/api/ajaxConvert/convert",
                headers = mix("user-agent" to webView.settings.userAgentString),
                form = mix("vid" to videoId, "k" to token))
                .resolve { resp ->
                    try {
                        listener.onSuccess(JSONObject(resp.text!!).getString("dlink"))
                    } catch (e: Exception) {
                        onUiThread { listener.onError() }
                    }
                }
                .reject {
                    onUiThread { listener.onFail() }
                }
                .catch {
                    onUiThread { listener.onError() }
                }
    }
}