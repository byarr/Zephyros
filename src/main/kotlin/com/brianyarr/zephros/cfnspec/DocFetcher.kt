package com.brianyarr.zephros.cfnspec

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object DocFetcher {

    val pageCache: MutableMap<String, Document> = mutableMapOf()

    private fun getDoc(url: String): Document {
       return pageCache.getOrPut(url, {Jsoup.connect(url).get()})
    }

    fun getDocs(pt: PropertyType): String = getDocs(pt.Documentation)

    fun getDocs(pt: Property): String = getDocs(pt.Documentation)

    fun getDocs(pt: ResourceType): String = getDocs(pt.Documentation)


    private fun getDocs(docUrl: String): String {
        if (docUrl.contains('#')) {
            val parts = docUrl.split("#")
            return getAnchorDocs(parts[0], parts[1])
        } else {
            return getPageDocs(docUrl)
        }
    }

    private fun getPageDocs(docUrl: String): String {
        val document = getDoc(docUrl)
        val select = document.select(".section p")

        return select  .filterNot { it.text().isNullOrBlank() }
                .filterNot { it.classNames().contains("aws-note") }
                .firstOrNull()
                ?.text()
                ?: ""
    }

    private fun getAnchorDocs(url: String, anchor: String): String {
        val doc = getDoc(url)
        val element = doc.select("dt > a[name=$anchor]")

        val parent = element.parents().first()
        val sibling = parent.nextElementSibling()

        return sibling.text()
    }

}