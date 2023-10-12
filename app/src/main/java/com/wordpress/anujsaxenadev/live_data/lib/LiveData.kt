package com.wordpress.anujsaxenadev.live_data.lib

import androidx.lifecycle.LifecycleOwner
import com.wordpress.anujsaxenadev.live_data.lib.observers.LiveDataObserver

interface LiveData<T> {
    fun setValue(value: T)
    fun postValue(value: T)
    fun getValue(): T?
    fun observe(observer: LiveDataObserver<T>, owner: LifecycleOwner? = null)
    fun removeObserver(observer: LiveDataObserver<T>)
}