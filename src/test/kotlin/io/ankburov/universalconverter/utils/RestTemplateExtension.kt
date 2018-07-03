package io.ankburov.universalconverter.utils

import org.junit.Assert
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.MultiValueMap

inline fun <reified T> ResponseEntity<T?>.isRedirected(errorMessage: String? = null): ResponseEntity<T?> {
    Assert.assertTrue(errorMessage, this.statusCode.is3xxRedirection())
    return this
}

inline fun <reified T> ResponseEntity<T?>.redirectedTo(target: String, errorMessage: String? = null): ResponseEntity<T?> {
    Assert.assertTrue(errorMessage, this.headers.get("Location")?.get(0)?.contains(target) == true)
    return this
}

inline fun <reified T> ResponseEntity<T?>.ok(message: String? = null): ResponseEntity<T?> {
    Assert.assertEquals(message, HttpStatus.OK, this.statusCode)
    return this
}

inline fun <reified T> ResponseEntity<T?>.isBadRequest(message: String? = null): ResponseEntity<T?> {
    Assert.assertEquals(message, HttpStatus.BAD_REQUEST, this.statusCode)
    return this
}

inline fun <reified T> ResponseEntity<T?>.isInternalError(message: String? = null): ResponseEntity<T?> {
    Assert.assertEquals(message, HttpStatus.INTERNAL_SERVER_ERROR, this.statusCode)
    return this
}

inline fun <reified T> ResponseEntity<T?>.bodyNotNull(message: String? = null) = this.body ?: throw AssertionError(message)

inline fun <reified R> TestRestTemplate.send(url: String,
                                                        method: HttpMethod,
                                                        requestEntity: HttpEntity<Any>? = null): ResponseEntity<R?> {
    return this.exchange(url, method, requestEntity, object : ParameterizedTypeReference<R>() {})
}

fun generateBody(file: Resource): MultiValueMap<String, HttpEntity<*>> {
    val builder = MultipartBodyBuilder()
    builder.part("file", file)
    return builder.build()
}