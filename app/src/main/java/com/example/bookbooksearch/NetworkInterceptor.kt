package com.example.bookbooksearch

import android.util.Log
import com.example.bookbooksearch.dataClass.DataError
import com.example.bookbooksearch.dataClass.DataMain
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBody = response.body


        when (response.code){
            200 ->{
                val source = responseBody?.source()
                source?.request(Long.MAX_VALUE)
                val json = source?.buffer?.clone()?.readUtf8()

                if (json != null) {
                    val myData = parseDataMain(json)
                    if (myData?.items == null) {
                        Log.e("TAG", "intercept: DataError로 감!", )
                        val errorData = parseDataError(responseBody.string())
                        if (errorData != null) {
                            Log.e("TAG", "intercept: 오류 응답, ErrorCode: ${errorData.errorCode}")
                            Log.e("TAG", "intercept: 오류 응답, Message: ${errorData.errorMessage}")
                            throw IllegalStateException("${errorData.errorMessage}")
                        }
                    }
                    else{
                        Log.e("TAG", "intercept: DataMain으로 감!", )
                    }
                }
                /*
                200을 받아도 안에서는 다르게 값이 오니 확인해야됨!
                · key 값이 잘못되었을 경우
                    {"faultInfo":{"message":"유효하지않은 키값입니다.","errorCode":"320010"}}
                · 요청 항목에 필수 값이 없을 경우
                    {"faultInfo":{"message":"날짜는 필수항목입니다.[parameterName=targetDt,parameterValue=null]","errorCode":"320102"}}
                · targetDt에 입력하는 형식이 안맞을 경우
                    {"faultInfo":{"message":"날싸형식에 맞게 입력하십시요.(YYYYMMDD)[parameterName=targetDt,parameterValue=2025]","errorCode":"320103"}}
                 */
            }
            else -> {
                throw UnknownHostException("${response.code} 오류!")
            }
        }
        return response
    }


    // JSON 문자열을 파싱하여 DataMain 객체로 변환하는 함수
    // GSON 라이브러리를 사용하여 JSON으로 파싱하고, 실패하면 null로 반환
    private fun parseDataMain(json: String): DataMain? {
        return try {
            Gson().fromJson(json, DataMain::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun parseDataError(json: String?): DataError? {
        return try {
            Gson().fromJson(json, DataError::class.java)
        } catch (e: Exception) {
            null
        }
    }

}

/*
에러 경우!
1. key 값이 잘못되었을 경우 -> 200이 뜨나 받는 값은
    Response{protocol=http/1.1, code=200, message=OK, url=http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?&k=c16ceb406c4bad4a2c03718b5f676b1e&weekGb=0&targetDt=20240101&itemPerPage=1}
    {"faultInfo":{"message":"유효하지않은 키값입니다.","errorCode":"320010"}}
2. 요청 항목에 필수 값이 없을 경우 -> 200이 뜨나 받는 값은
    Response{protocol=http/1.1, code=200, message=OK, url=http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?&key=c16ceb406c4bad4a2c03718b5f676b1e&weekGb=0&t=20240101&itemPerPage=1}
    {"faultInfo":{"message":"날짜는 필수항목입니다.[parameterName=targetDt,parameterValue=null]","errorCode":"320102"}}
3. targetDt(필수/조회하고자 하는 날짜를 yyyymmdd 형식으로 입력)에서 입력하는 형식이 안맞을 경우 -> 200이 뜨나 받는 값은
    Response{protocol=http/1.1, code=200, message=OK, url=http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?&key=c16ceb406c4bad4a2c03718b5f676b1e&weekGb=0&targetDt=2025&itemPerPage=1}
    {"faultInfo":{"message":"날싸형식에 맞게 입력하십시요.(YYYYMMDD)[parameterName=targetDt,parameterValue=2025]","errorCode":"320103"}}
 */


/*
source에서 읽은 데이터는 아직 바이트 스트림 형태!
그래서 이 데이터를 문자열로 변환하려면 버퍼로 읽어온 후 문자열로 디코딩 해야함!
source?.buffer -> source를 버퍼로 읽어오는 작업 수행
.clone() 버퍼를 복제하여 원본 데이터를 보존하고 복제본에서 읽어올 수 있도록 한다.
.readUtf8() 버퍼에서 문자열을 읽어온다. 결과로 얻어진 문자열을 json 변수에 저장!
 */

/*
응답 바디로부터 JSON 데이터를 추출합니다.
이를 문자열로 읽고 파싱할 것입니다.
 */