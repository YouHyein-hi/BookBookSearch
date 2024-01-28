package com.example.bookbooksearch.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookbooksearch.dataClass.FetchState
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {

    private val _fetchState = MutableLiveData<FetchState>()
    val fetchState: LiveData<FetchState>
        get() = _fetchState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String>
        get() = _errorMessage

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    private val job = SupervisorJob()

    protected val modelScope = viewModelScope + job + coroutineExceptionHandler

    protected fun handleException(throwable: Throwable) {
        throwable.printStackTrace()
        Log.e("TAG", "CoroutineExceptionHandler: $throwable")
        when(throwable){
            is HttpException -> _fetchState.value = FetchState.HttpException
            is UnknownHostException -> {
                _fetchState.value = FetchState.UnknownHostException
                _errorMessage.value = throwable.message
            }
            is IllegalStateException -> {
                _fetchState.value = FetchState.IllegalStateException
                if(throwable.message != null){
                    val message = throwable.message!!.replace("java.lang.${fetchState}: ", "")
                    _errorMessage.value = message
                }
            }
            else -> _fetchState.value = FetchState.FAIL
        }
    }

    override fun onCleared() {
        modelScope.coroutineContext.cancelChildren()
        super.onCleared()
    }

}





/*
            SocketException
                : 소켓 통신과 관련된 문제로 발생합니다. 네트워크 연결이 끊어지거나 소켓 통신 중에 문제가 발생할 때 발생할 수 있습니다.

            HttpException
                : 네트워크 요청을 처리하는 동안 발생하는 HTTP 오류입니다. 일반적으로 서버로부터의 응답 코드가 400 이상인 경우에 발생합니다.

            UnknownHostException
                : 호스트(서버)를 찾을 수 없는 경우 발생하는 예외입니다. DNS 해상도를 실패하거나 호스트명이 잘못된 경우 발생합니다.

            IllegalStateException
                : 상태가 잘못되었을 때 발생하는 일반적인 예외입니다. 예를 들어, Android의 UI 업데이트를 메인 스레드가 아닌 다른 스레드에서 시도하는 경우 발생할 수 있습니다.

            NetworkOnMainThreadException
                : Android에서는 메인(UI) 스레드에서 네트워크 작업을 수행하려고 할 때 이 예외가 발생합니다. 메인 스레드는 UI 업데이트와 관련된 작업에 사용해야 하며, 네트워크 요청은 백그라운드 스레드에서 처리해야 합니다.
            */