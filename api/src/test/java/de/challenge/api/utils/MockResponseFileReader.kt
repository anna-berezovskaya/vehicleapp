package de.challenge.api.utils

class MockResponseFileReader(path: String) {
        val content: String = this.javaClass.getResource(path)!!.readText()
}