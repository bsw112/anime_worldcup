package com.manta.worldcup.api.intercepter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.manta.worldcup.helper.Constants
import okhttp3.Interceptor
import okhttp3.Response


/**
 * http 요청시 쿠키를 받는 인터셉터
 */
class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //요청에 대한 응답을 받음. 여기서 실제로 요청을 보낸다. 아마 이 함수자체가 다른 워커스레드에서 불리는듯.
        val originalResponse = chain.proceed(chain.request()) // block됨.
        //응답에 쿠키가 있으면
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            //앱에 저장된 쿠키를 찾아, 그 쿠키에 쿠키를 추가한다.
            val sharedPref = context.getSharedPreferences(Constants.PREF_FILENAME_COOKIE, MODE_PRIVATE)
            val cookies = sharedPref.getStringSet(Constants.PREF_COOKIE, HashSet());
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies!!.add(header)
            }
            val prefEditor = sharedPref.edit()
            prefEditor.putStringSet(Constants.PREF_COOKIE, cookies).apply()
            prefEditor.commit()
        }
        //응답을 그대로 뱉는다.
        return originalResponse
    }


}