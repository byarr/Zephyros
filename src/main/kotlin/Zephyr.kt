import java.util.*
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.databind.ObjectMapper

class CloudFormation() {

    var description: String? = null
    val resources: MutableList<Resource> = LinkedList()


    fun addResource(res: Resource, init: Resource.() -> Unit): Resource {
        resources.add(res)
        res.init()
        return res
    }

    fun Resource(id: String, init: Resource.() -> Unit) = addResource(Resource(id), init)

    fun Description(desc: String) {
        description = desc
    }

    override fun toString(): String {
        return super.toString()
    }

    fun toMap(): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        result["AWSTemplateFormatVersion"] = "2010-10-10"
        description?.let{ result["Description"] = it}

        if (resources.isNotEmpty()) {
            val resourceList = mutableMapOf<String, Any>()
            result["Resources"] = resourceList
            for (resource in resources) {
                resourceList[resource.id] = resource.toMap()
            }
        }



        return result
    }
}

class Resource(val id: String) {

    var type: String? = null
    var properties: MutableMap<String, Any> = HashMap()

    fun Type(type: String) {
        this.type = type
    }

    fun Property(k: String, v: Any) {
        properties.put(k, v)
    }

    fun toMap(): Map<String, Any> {
        return hashMapOf(Pair("Type", "SomeType"))
    }

}

fun CloudFormation(init: CloudFormation.() -> Unit): CloudFormation {
    val html = CloudFormation()
    html.init()
    return html
}

fun main(args: Array<String>) {

    val cfn = CloudFormation {
        Resource("MyLambda") {
            Type("AWS::Lambda::Function")
            Property("Name", "BadAssFunction")
        }
    }

    val mapper = ObjectMapper(YAMLFactory())
    val s = mapper.writeValueAsString(cfn.toMap())
    print(s)




}