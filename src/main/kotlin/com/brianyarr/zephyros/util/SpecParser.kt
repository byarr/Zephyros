package com.brianyarr.zephyros.util

import org.json.JSONObject
import org.json.JSONTokener

object SpecParser {
    fun getPropertyTypes() {

        val propCount :MutableMap<String, Int> = mutableMapOf()

        val inputStream = javaClass.getResourceAsStream("/CloudFormationResourceSpecification.json")
        val value = JSONTokener(inputStream).nextValue() as JSONObject
        val propertyTypes = value["PropertyTypes"] as JSONObject
        println(propertyTypes.keySet())

        for (k in propertyTypes.keySet()) {
            val props = ((propertyTypes[k] as JSONObject)["Properties"]) as JSONObject

            for (p in props.keySet()) {
                propCount[p] = propCount.getOrElse(p, {0}) + 1
            }
        }

        println(propertyTypes.keySet().size)
        println(propCount)
    }
}

fun main(args: Array<String>) {
    SpecParser.getPropertyTypes()
}

