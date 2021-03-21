package ru.alinadorozhkina.convertapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.alinadorozhkina.convertapp.mvp.model.IConverter
import java.io.*

class AndroidConverter(val context: Context) : IConverter {

    override fun convert(data: String): Completable = Completable.fromAction {
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            return@fromAction
        }
        imageFormat(data)
    }.subscribeOn(Schedulers.io())

    private fun imageFormat(uri: String) {
        val myUri = Uri.parse(uri)
        val imageStream = context.contentResolver.openInputStream(myUri);
        val bmp = BitmapFactory.decodeStream(imageStream)
        val file = File(context.getExternalFilesDir(""), "toJPEG")
        val fas = FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fas)
    }
}