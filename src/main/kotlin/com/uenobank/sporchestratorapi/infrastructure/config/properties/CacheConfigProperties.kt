package com.uenobank.sporchestratorapi.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "cache-config")
data class CacheConfigProperties(
    var default: CacheDefaultConfig = CacheDefaultConfig(),
    var caches: List<CacheInstanceConfig> = emptyList()
)

data class CacheDefaultConfig(
    var ttlSeconds: Long = 30,
    var maxSize: Long = 1000
)

data class CacheInstanceConfig(
    var name: String = "",
    var ttlSeconds: Long? = null,
    var maxSize: Long = 1000
) {
    fun getTtlInSeconds(): Long {
        return ttlSeconds?: 30
    }
}
