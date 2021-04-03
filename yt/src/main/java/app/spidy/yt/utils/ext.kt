package app.spidy.yt.utils

import app.spidy.yt.data.Video
import app.spidy.yt.data.VideoFormat


fun Video.getMp3(): List<VideoFormat> {
    val formats = ArrayList<VideoFormat>()
    for (f in this.formats) {
        if (f.format == "mp3") {
            formats.add(f)
        }
    }
    return formats
}

fun Video.getMp4(): List<VideoFormat> {
    val formats = ArrayList<VideoFormat>()
    for (f in this.formats) {
        if (f.format == "mp4") {
            formats.add(f)
        }
    }
    return formats
}

fun Video.getFormatTypeList(): List<String> {
    val types = ArrayList<String>()
    for (f in this.formats) {
        if (!types.contains(f.format)) {
            types.add(f.format)
        }
    }
    return types
}
