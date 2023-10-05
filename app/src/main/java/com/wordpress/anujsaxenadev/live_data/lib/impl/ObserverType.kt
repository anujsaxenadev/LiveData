package com.wordpress.anujsaxenadev.live_data.lib.impl

sealed interface ObserverType{
    object ForeverObserver: ObserverType
    object LifecycleBoundObserver: ObserverType
}