package com.github.backlog.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri

/**
 * @throws IOException
 * @throws FileNotFoundException
 */
fun Uri.toBitmap(context: Context): Bitmap {
    val source = ImageDecoder.createSource(context.contentResolver, this)
    return ImageDecoder.decodeBitmap(source)
}
