package com.wordpress.anujsaxenadev.live_data.lib.impl

import androidx.lifecycle.LifecycleOwner
import com.wordpress.anujsaxenadev.live_data.lib.LiveData
import com.wordpress.anujsaxenadev.live_data.lib.observers.LiveDataLifeCycleObserver
import com.wordpress.anujsaxenadev.live_data.lib.observers.LiveDataObserver
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LiveDataImpl<T>: LiveData<T> {
    private var value: T? = null
    private var onError : ((Throwable) -> Unit)? = null
    private var observersList: ArrayList<LiveDataObserver<T>> = ArrayList()

    override fun setValue(value: T) {
        try {
            this.value = value
            fireObserver(this.value)
        }
        catch (e: Exception){
            logException(e)
        }
    }

    override fun postValue(value: T) {
        try {
            this.value = value
            fireObserversOnBackgroundThread(value)
        }
        catch (e: Exception){
            logException(e)
        }
    }

    override fun getValue(): T? {
        return try {
            value
        } catch (e: Exception){
            logException(e)
            null
        }
    }

    override fun observe(observer: LiveDataObserver<T>, owner: LifecycleOwner?) {
        try {
            owner?.let {
                observer.lifecycleObserver = LiveDataLifeCycleObserver()
                owner.lifecycle.addObserver(observer.lifecycleObserver!!)
            }
            observersList.add(observer)
        } catch (e: Exception){
            logException(e)
        }
    }

    override fun removeObserver(observer: LiveDataObserver<T>) {
        try {
            removeObserverFromList(observer)
        } catch (e: Exception){
            logException(e)
        }
    }

    private fun fireObserversOnBackgroundThread(value: T){
        CoroutineScope(Dispatchers.Main + CoroutineExceptionHandler{ _, exception ->
            logException(exception)
        }).launch {
            flow {
                emit(value)
            }
                .flowOn(Dispatchers.Default)
                .collectLatest { value ->
                    fireObserver(value)
                }
        }
    }

    private fun fireObserver(mValue: T?){
        try {
            postDataThroughObservers(observersList, mValue)
        } catch (e: Exception){
            logException(e)
        }
    }

    private fun postDataThroughObservers(observersList: ArrayList<LiveDataObserver<T>>, mValue: T?) {
        try {
            for (observer in observersList) {
                if(observer.lifecycleObserver != null){
                    observer.lifecycleObserver?.isLifeCyclePaused()?.let {
                        if(!it){
                            observer.observer(mValue)
                        }
                    }
                }
                else{
                    observer.observer(mValue)
                }
            }
        } catch (e: Exception){
            logException(e)
        }
    }

    private fun removeObserverFromList(targetObserver: LiveDataObserver<T>){
        try {
            observersList.iterator().apply {
                while (hasNext()) {
                    val observer = next()
                    if(observer == targetObserver){
                        remove()
                    }
                }
            }
        } catch (e: Exception){
            logException(e)
        }
    }

    fun listenToErrors(onError : (Throwable) -> Unit){
        this.onError = onError
    }

    private fun logException(e: Throwable){
        onError?.invoke(e)
    }
}

