package com.uenobank.sporchestratorapi.infrastructure.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.uenobank.sporchestratorapi.infrastructure.config.properties.CacheConfigProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig(
    private val cacheConfigProperties: CacheConfigProperties
) {

    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager()

        // Configuración por defecto para caches no especificados
        cacheManager.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(cacheConfigProperties.default.ttlSeconds))
                .maximumSize(cacheConfigProperties.default.maxSize)
        )

        // Configurar caches específicos de forma dinámica desde properties
        cacheConfigProperties.caches.forEach { cacheConfig ->
            cacheManager.registerCustomCache(
                cacheConfig.name,
                Caffeine.newBuilder()
                    .expireAfterWrite(Duration.ofSeconds(cacheConfig.getTtlInSeconds()))
                    .maximumSize(cacheConfig.maxSize)
                    .build()
            )
        }

        return cacheManager
    }
}
