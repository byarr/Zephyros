package com.brianyarr.zephros.cfnspec

object CodeGen {

    fun dataClass(name: String, pt: PropertyType): StringBuilder {
        val result = StringBuilder()
        val className = name.replace("::", "_").replace(".", "_")

        result.append("data class ").append(className).append("(")
        result.append(pt.Properties.map {
            "val " + it.key + ": " + it.value.getKotlinType()
        }.joinToString(separator = ", ")
        )

        result.append(")")
        return result
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val spec = SpecParser.parseSpec()
        spec.PropertyTypes.forEach { println(dataClass(it.key, it.value)) }

//        val key = "AWS::Lambda::Function.Code"
//        val propertyType = spec.PropertyTypes.get(key)!!
//
//        println(dataClass(key, propertyType))
    }

}
