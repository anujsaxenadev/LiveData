package com.wordpress.anujsaxenadev.customlivedata.lib

import androidx.lifecycle.LifecycleOwner
import com.wordpress.anujsaxenadev.customlivedata.lib.observers.LiveDataObserver

interface LiveDataContract<T> {
    fun setValue(value: T)
    fun postValue(value: T)
    fun getValue(): T?
    fun observe(observer: LiveDataObserver<T>, owner: LifecycleOwner? = null)
    fun removeObserver(observer: LiveDataObserver<T>)
}