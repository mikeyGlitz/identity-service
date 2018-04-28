package io.mikeyglitz.identity

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application

/**
 * Main application entry point
 * @param args The command-line arguments
 */
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}