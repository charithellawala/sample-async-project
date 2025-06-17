package com.example.charging.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	println("This is a simple statement printed to console")
	runApplication<Application>(*args)
}
