package com.wordpress.anujsaxenadev.customlivedata.lib.observers

class LiveDataObserver<T>(
    val observer: (T?) -> Unit,
    var lifecycleObserver: LiveDataLifeCycleObserver? = null
)
