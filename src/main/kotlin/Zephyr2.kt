package cfn2

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule

@JsonPropertyOrder("AWSTemplateFormatVersion", "Description", "Resources")
class CloudFormation() {

    @JsonProperty("AWSTemplateFormatVersion")
    val AWSTemplateFormatVersion = "2011-10-01"

    @JsonProperty("Description")
    var description: String? = null

    @JsonProperty("Resources")
    val resources: MutableMap<String, Resource> = mutableMapOf()

    fun Lambda_Function(id: String, init: Lambda_Function.() -> Unit): Lambda_Function {
        val function = Lambda_Function()
        resources.put(id, function)
        function.init()
        return function
    }

    fun Description(desc: String) {
        description = desc
    }

}

data class Lambda_Function_Code(val S3Bucket: String? = null, val S3Key: String? = null, val S3ObjectVersion: String? = null, val ZipFile: String? = null)

@JsonPropertyOrder("Type", "Properties")
class Lambda_Function(): Resource("AWS::Lambda::Function") {

    class Props {
        var Description: String? = null
        var FunctionName: String? = null
        val Code: Lambda_Function_Code = Lambda_Function_Code(S3Bucket = "Bucket", S3Key = "MyKey")
    }

    val Properties = Props()

    fun Description(desc: String) { Properties.Description = desc }
    fun FunctionName(s: String) { Properties.FunctionName = s }
}

abstract class Resource(val t: String) {

    @JsonProperty("Type")
    val Type: String

    init {
        this.Type = t
    }
}

fun CloudFormation(init: CloudFormation.() -> Unit): CloudFormation {
    val html = CloudFormation()
    html.init()
    return html
}

fun main(args: Array<String>) {

    val cfn = CloudFormation {
        Description("This is my cloudformation description")
        Lambda_Function("MyLambda") {
            FunctionName("MyFuncName")
            Description("This is some description")
        }
    }

    val mapper = ObjectMapper(YAMLFactory())
    mapper.registerModule(KotlinModule())
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
//    mapper.propertyNamingStrategy = PropertyNamingStrategy.UpperCamelCaseStrategy()
    val s = mapper.writeValueAsString(cfn)
    print(s)
}