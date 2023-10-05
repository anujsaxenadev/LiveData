package com.wordpress.anujsaxenadev.live_data.lib

sealed interface ObserverType{
    object ForeverObserver: ObserverType
    object LifecycleBoundObserver: ObserverType
}