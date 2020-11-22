package com.manta.worldcup.model

import android.graphics.Bitmap

data class Picture(val mComments : List<Comment>, val mDescription : String,
                   val mBitmap : Bitmap, val WinCnt : Int, val mPictureName : String);