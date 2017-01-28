package com.brianyarr.zephros.cfnspec

object CodeGen {

    fun Property.getKotlinType(): String {
        if (Type == null && PrimitiveType == null) {
            throw IllegalStateException()
        }
        if (Type != null && PrimitiveType != null) {
            throw IllegalStateException()
        }

        if (ItemType != null && PrimitiveItemType != null) {
            throw IllegalStateException()
        }

        var type = Type ?: "" + PrimitiveType?: ""
        if (ItemType != null || PrimitiveItemType != null) {
            type = type + "<" + (ItemType ?: "") + (PrimitiveItemType ?: "") + ">"
        }
        if (!Required) {
            type += "?"
        }
        return type
    }

    fun dataClass(name: String, pt: PropertyType): StringBuilder {
        val result = StringBuilder()
        val className = name.replace("::", "_").replace(".", "_")


        val docs = DocFetcher.getDocs(pt)
        result.append("/**").append(docs).append("*/\n")

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

    }

}
