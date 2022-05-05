package oicq.wlogin_server

import oicq.wlogin_server.net.Connection

fun main() {
    println("Hello, world!")
    Connection().bind(8080)
}