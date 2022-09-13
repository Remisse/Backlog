package com.github.backlog.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.github.backlog.BuildConfig
import com.github.backlog.R
import java.io.File
import java.lang.IllegalStateException

class ComposeFileProvider private constructor (): FileProvider(R.xml.provider_paths) {
    companion object {
        fun createNewImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            if (!(directory.exists() || directory.mkdirs())) {
                throw IllegalStateException("Not all directories were created.")
            }

            val file = File.createTempFile("selected_image_", ".jpg", directory)
            val authority = BuildConfig.APPLICATION_ID + ".provider"

            return getUriForFile(context, authority, file)
        }
    }
}
