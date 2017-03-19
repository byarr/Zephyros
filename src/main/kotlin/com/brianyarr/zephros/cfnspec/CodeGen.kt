package com.brianyarr.zephros.cfnspec

object CodeGen {

    fun Property.getKotlinType(itemTypePrefix: String): String {

        var type = chooseType(Type, PrimitiveType, itemTypePrefix)
        if (ItemType != null || PrimitiveItemType != null) {
            type = type + chooseType(ItemType, PrimitiveItemType, itemTypePrefix) + ">"
        }

        if (!Required) {
            type += "?"
        }
        return type
    }

    fun chooseType(Type: String?, PrimitiveType: String?, prefix: String): String {
        if (Type != null && PrimitiveType != null) {
            throw IllegalArgumentException()
        }
        if (Type != null) {
            if (Type.equals("List")) {
                return Type + "<"
            } else if (Type.equals("Map")) {
                return Type + "<String, "
            } else{
                return prefix + Type
            }
        }
        if (PrimitiveType != null){
            return PrimitiveType
        }
        throw IllegalArgumentException()
    }

    fun dataClass(name: String, pt: PropertyType): StringBuilder {
        val result = StringBuilder()
        val className = name.replace("::", "_").replace(".", "_")

        val prefix = name.split('.')[0].replace("::", "_") + "_"

        val docs = DocFetcher.getDocs(pt)
        result.append("/**").append(docs).append("*/\n")

        result.append("data class ").append(className).append("(")
        result.append(pt.Properties.map {
            "val " + it.key + ": " + it.value.getKotlinType(prefix)
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
