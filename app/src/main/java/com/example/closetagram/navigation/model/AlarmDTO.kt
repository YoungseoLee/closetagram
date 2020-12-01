package com.example.closetagram.navigation.model

import android.os.health.UidHealthStats
import com.google.api.Billing

data class AlarmDTO(
    var destinationUid: String? = null,
    var userId: String? = null,
    var uid: String? = null,
    // 0 : like alarm
    // 1 : comment alarm
    // 2: follow alarm
    var kind: Int? = null,
    var message: String? = null,
    var timestamp: Long? = null
)