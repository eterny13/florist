package com.example.florist.api.controller.customer.request


import groovy.json.JsonBuilder

class FixtureCustomerRequest {
    private static Map<String, ?> normalJson() {
        [
                "name" : "Steve Gatt",
                "email": "abc@example.com"
        ]
    }

    private static Map<String, ?> emptyNameJson() {
        [
                "name" : "",
                "email": "abc@example.com"
        ]
    }

    private static Map<String, ?> illegalJson() {
        [
                "name" : "Steve Gatt",
                "email": "example.com"
        ]
    }

    static asStringNormal() {
        JsonBuilder builder = new JsonBuilder()
        builder(normalJson())
        builder.toString()
    }

    static asStringIllegal() {
        JsonBuilder builder = new JsonBuilder()
        builder(illegalJson())
        builder.toString()
    }

    static asStringEmptyName() {
        JsonBuilder builder = new JsonBuilder()
        builder(emptyNameJson())
        builder.toString()
    }
}
