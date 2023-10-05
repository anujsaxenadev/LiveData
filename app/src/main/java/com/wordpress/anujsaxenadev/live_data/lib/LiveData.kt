package com.wordpress.anujsaxenadev.live_data.lib

import androidx.lifecycle.LifecycleOwner

interface LiveData<T> {
    fun setValue(value: T)
    fun postValue(value: T)
    fun getValue(): T?
    fun observeForever(observer: LiveDataObserver<T>)
    fun observe(owner: LifecycleOwner, observer: LiveDataObserver<T>)
    fun removeObserver(observer: LiveDataObserver<T>)
}