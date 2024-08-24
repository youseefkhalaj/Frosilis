package com.youeefkhalaj.frosilis.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class ImageConvertor {
    @TypeConverter
    fun getStringFromBitmap(bitmap: Bitmap): ByteArray {
        val outPutStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outPutStream)
        return outPutStream.toByteArray()
    }

    @TypeConverter
    fun getBitmapFromStream(byteArray: ByteArray):Bitmap{
       return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

    }
    @TypeConverter
    fun fromString(value: String): Array<Array<IntArray>>{
        val listType =object : TypeToken<Array<Array<IntArray>>>(){}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun frmArrayList(list: Array<Array<IntArray>>):String{
        return Gson().toJson(list)
    }

}