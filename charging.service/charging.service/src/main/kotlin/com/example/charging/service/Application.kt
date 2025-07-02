package com.example.charging.service

import com.example.charging.service.config.KafkaTopicsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableConfigurationProperties(KafkaTopicsConfig::class)
@EnableTransactionManagement
class Application

fun main(args: Array<String>) {
	println("This is a simple statement printed to console")
	runApplication<Application>(*args)
}
