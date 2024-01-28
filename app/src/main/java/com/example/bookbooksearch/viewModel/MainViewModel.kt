package com.example.bookbooksearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookbooksearch.base.BaseViewModel
import com.example.bookbooksearch.dataClass.DataDetail
import com.example.bookbooksearch.dataClass.DataMain
import com.example.bookbooksearch.dataClass.DetailItem
import com.example.bookbooksearch.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainViewModel : BaseViewModel() {
    init {
        Log.e("MainViewModel", "MainViewModel에 들어감!!", )
    }

    private val bookService = RetrofitClient.getRetrofitService()

    private val _result = MutableLiveData<DataMain?>()
    val result: LiveData<DataMain?>
        get() = _result

    private val _response = MutableLiveData<Response<DataMain>>()
    val response: LiveData<Response<DataMain>>
        get() = _response

    private val _resultDetail = MutableLiveData<DataDetail?>()
    val resultDetail: LiveData<DataDetail?>
        get() = _resultDetail

    private val _responseDetail = MutableLiveData<Response<DataDetail>>()
    val responseDetail: LiveData<Response<DataDetail>>
        get() = _responseDetail

    private val _detailData = MutableLiveData<DetailItem?>()
    val detailData : LiveData<DetailItem?>
        get() = _detailData
    fun setDetailData(data : DetailItem){
        _detailData.value = data
    }

    private val _isbn = MutableLiveData<String>()
    val isbn: LiveData<String>
        get() = _isbn
    fun getIsbn(isbn: String){
        _isbn.postValue(isbn)
    }

    private val _display = MutableLiveData<Int>()
    val display: LiveData<Int>
        get() = _display
    fun getdDisplay(display: Int){
        _display.postValue(display)
    }

    private val _sort = MutableLiveData<String>()
    val sort: LiveData<String>
        get() = _sort
    fun getdSort(sort: String){
        _sort.postValue(sort)
    }


    fun getDataApi(title: String, display: Int?, sort: String?) {
        Log.e("TAG", "getDataApi: ", )

        /** CoroutineExceptionHandler **/
        modelScope.launch {
            try {
                val dataCall = bookService.getSearchBooks(
                    "_D5fPEhr6vCageKJ4cGl", "1zRMr4RFDP", query = title, display = display, sort = sort
                )
                val dataResponse = withContext(Dispatchers.IO) {
                    val response = dataCall.execute()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _result.postValue(responseBody)
                        Log.e("MainViewModel", "getDataApi result: ${result.value}", )
                    }
                    else{
                        Log.e("MainViewModel", "getDataApi: 아니 미친 없냐?", )}
                    response
                }
                _response.postValue(dataResponse)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun getDataDetail(isbn: String) {
        Log.e("MainViewModel", "getDataDetail: ", )

        /** CoroutineExceptionHandler **/
        modelScope.launch {
            try {
                val dataCall = bookService.getSearchBooksDetail(
                    "_D5fPEhr6vCageKJ4cGl", "1zRMr4RFDP", d_isbn = isbn
                )
                val dataResponse = withContext(Dispatchers.IO) {
                    val response = dataCall.execute()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _resultDetail.postValue(responseBody)
                        Log.e("MainViewModel", "getDataDetail result: ${resultDetail.value}", )
                    }
                    else{
                        Log.e("MainViewModel", "getDataDetail: 아니 미친 없냐?", )}
                    response
                }
                _responseDetail.postValue(dataResponse)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }



}