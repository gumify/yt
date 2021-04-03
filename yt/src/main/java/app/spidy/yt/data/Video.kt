package app.spidy.yt.data

data class Video(
    val title: String,
    val videoId: String,
    val formats: List<VideoFormat>
)
