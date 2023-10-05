package com.wordpress.anujsaxenadev.live_data.lib

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class LiveDataLifeCycleObserver: DefaultLifecycleObserver {

    private var isLifeCyclePaused = false

    fun isLifeCyclePaused(): Boolean{
        return isLifeCyclePaused
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        isLifeCyclePaused = false
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        isLifeCyclePaused = true
    }
}