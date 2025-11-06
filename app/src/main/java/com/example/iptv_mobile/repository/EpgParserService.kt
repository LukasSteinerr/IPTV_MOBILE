package com.example.iptv_mobile.repository

import com.example.iptv_mobile.model.EpgChannelInfo
import com.example.iptv_mobile.model.TvProgram

class EpgParserService {
    data class EpgProgress(
        val processed: Int,
        val total: Int?,
        val message: String
    )

    suspend fun parseEpgData(
        url: String,
        onProgress: ((EpgProgress) -> Unit)? = null,
        onBatchReady: (List<TvProgram>, List<EpgChannelInfo>) -> Unit,
        onComplete: () -> Unit
    ): Boolean {
        // TODO: Implement EPG parsing
        onComplete()
        return true
    }

    fun close() {
        // TODO: Implement close
    }
}