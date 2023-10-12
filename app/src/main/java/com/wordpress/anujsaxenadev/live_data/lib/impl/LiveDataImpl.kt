package com.wordpress.anujsaxenadev.live_data.lib.impl

import androidx.lifecycle.LifecycleOwner
import com.wordpress.anujsaxenadev.live_data.lib.LiveData
import com.wordpress.anujsaxenadev.live_data.lib.observers.LiveDataLifeCycleObserver
import com.wordpress.anujsaxenadev.live_data.lib.observers.LiveDataObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LiveDataImpl<T>: LiveData<T> {
    private var value: T? = null
    private var observersList: ArrayList<LiveDataObserver<T>> = ArrayList()

    override fun setValue(value: T) {
        this.value = value
        fireObserver(this.value)
    }

    override fun postValue(value: T) {
        this.value = value
        fireObserversOnBackgroundThread(value)
    }

    override fun getValue(): T? {
        return value
    }

    override fun observe(observer: LiveDataObserver<T>, owner: LifecycleOwner?) {
        owner?.let {
            observer.lifecycleObserver = LiveDataLifeCycleObserver()
            owner.lifecycle.addObserver(observer.lifecycleObserver!!)
        }
        observersList.add(observer)
    }

    override fun removeObserver(observer: LiveDataObserver<T>) {
        removeObserverFromList(observer)
    }

    private fun fireObserversOnBackgroundThread(value: T){
        CoroutineScope(Dispatchers.Main).launch {
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
        postDataThroughObservers(observersList, mValue)
    }

    private fun postDataThroughObservers(observersList: ArrayList<LiveDataObserver<T>>, mValue: T?) {
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
    }

    private fun removeObserverFromList(targetObserver: LiveDataObserver<T>){
        observersList.iterator().apply {
            while (hasNext()) {
                val observer = next()
                if(observer == targetObserver){
                    remove()
                }
            }
        }
    }
}

