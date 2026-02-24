package com.example.sounds.data.repository

import android.util.Log
import com.example.sounds.soundsDebugTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SyncState {
    object Idle: SyncState()
    object Syncing: SyncState()
    object Done: SyncState()
    object Error: SyncState()
}

class SyncManager(
    private val scope: CoroutineScope,
    private val sync: suspend () -> Boolean,
) {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState = _syncState
        .asStateFlow()

    private var syncJob: Job? = null

    fun triggerSync() {
        if (syncJob?.isActive == true) return
        syncJob = scope.launch {
            _syncState.value = SyncState.Syncing
            try {
                val didSyncRun = sync()
                Log.d(soundsDebugTag, "didSyncRun: $didSyncRun")
                _syncState.value = if (didSyncRun) SyncState.Done else SyncState.Error
                delay(2000)
                _syncState.value = SyncState.Idle
            } catch (e: Exception) {
                Log.d(soundsDebugTag, "sync failed, ${e.message}")
            }
        }
    }
}