package com.example.auth.service

import com.example.auth.service.config.KafkaTopicsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(KafkaTopicsConfig::class)
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
