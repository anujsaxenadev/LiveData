package com.wordpress.anujsaxenadev.live_data.lib

class LiveDataObserver<T>(
    val type: ObserverType,
    val observer: (T?) -> Unit,
    var lifecycleObserver: LiveDataLifeCycleObserver? = null
)
