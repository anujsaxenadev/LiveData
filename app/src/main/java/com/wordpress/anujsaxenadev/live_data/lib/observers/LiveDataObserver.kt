package com.wordpress.anujsaxenadev.live_data.lib.observers

class LiveDataObserver<T>(
    val observer: (T?) -> Unit,
    var lifecycleObserver: LiveDataLifeCycleObserver? = null
)
