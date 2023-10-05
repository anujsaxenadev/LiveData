package com.wordpress.anujsaxenadev.live_data.lib

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LiveDataImpl<T> : DefaultLifecycleObserver, LiveData<T>{
    private var mValue: T? = null
    private var observeForeverObserverList: ArrayList<LiveDataObserver<T>> = ArrayList()
    private var lifecycleBoundObserversList: ArrayList<LiveDataObserver<T>> = ArrayList()

    override fun setValue(value: T) {
        mValue = value
        fireObserver(mValue)
    }

    override fun postValue(value: T) {
        mValue = value

        CoroutineScope(Dispatchers.Default).launch {
            fireObserversOnBackgroundThread(value)
        }
    }

    override fun getValue(): T? {
        return mValue
    }

    override fun observeForever(observer: LiveDataObserver<T>) {
        observeForeverObserverList.add(observer)
    }

    override fun observe(owner: LifecycleOwner, observer: LiveDataObserver<T>) {
        val lifecycleObserver = LiveDataLifeCycleObserver()
        observer.lifecycleObserver = lifecycleObserver
        owner.lifecycle.addObserver(lifecycleObserver)
        lifecycleBoundObserversList.add(observer)
    }

    override fun removeObserver(observer: LiveDataObserver<T>) {
        when(observer.type){
            ObserverType.ForeverObserver -> {
                removeObserverFromList(lifecycleBoundObserversList, observer)
            }
            ObserverType.LifecycleBoundObserver -> {
                removeObserverFromList(observeForeverObserverList, observer)
            }
        }
    }

    private suspend fun fireObserversOnBackgroundThread(value: T){
        flow {
            emit(value)
        }
            .flowOn(Dispatchers.Default)
            .collectLatest { value ->
                CoroutineScope(Dispatchers.Main).launch {
                    fireObserver(value)
                }
            }
    }

    private fun fireObserver(mValue: T?){
        postDataThroughObservers(observeForeverObserverList, mValue)
        postDataThroughObservers(lifecycleBoundObserversList, mValue)
    }

    private fun postDataThroughObservers(observersList: ArrayList<LiveDataObserver<T>>, mValue: T?) {
        for (observer in observersList) {
            if(observer.type == ObserverType.LifecycleBoundObserver){
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

    private fun removeObserverFromList(list: ArrayList<LiveDataObserver<T>>, targetObserver: LiveDataObserver<T>){
        val observerIterator = list.iterator()
        while (observerIterator.hasNext()) {
            val observer = observerIterator.next()
            if(observer == targetObserver){
                observerIterator.remove()
            }
        }
    }
}

