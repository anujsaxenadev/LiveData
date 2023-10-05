package com.wordpress.anujsaxenadev.live_data.lib

import androidx.lifecycle.LifecycleOwner
import com.wordpress.anujsaxenadev.live_data.lib.impl.LiveDataObserver

interface LiveData<T> {
    fun setValue(value: T)
    fun postValue(value: T)
    fun getValue(): T?
    fun observeForever(observer: LiveDataObserver<T>)
    fun observe(owner: LifecycleOwner, observer: LiveDataObserver<T>)
    fun removeObserver(observer: LiveDataObserver<T>)
}