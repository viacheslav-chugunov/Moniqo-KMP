package io.github.viacheslav.chugunov.moniqo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform