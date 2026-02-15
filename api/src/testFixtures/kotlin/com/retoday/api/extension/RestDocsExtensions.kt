package com.retoday.api.extension

import com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import kotlin.reflect.KProperty

private typealias Field = Pair<String, String>

infix fun String.desc(description: String): Field = this to description

infix fun <T> KProperty<T>.desc(description: String): Field = name to description

fun fieldsOf(vararg fields: Field): Array<Field> = fields as Array<Field>

fun listFieldsOf(
    listField: Field,
    vararg fields: Field
): Array<Field> =
    fields
        .map { "${listField.first}[].${it.first}" desc it.second }
        .plus(listField)
        .toTypedArray()

fun objectFieldsOf(
    objectField: Field,
    vararg fields: Field
): Array<Field> =
    fields
        .map { "${objectField.first}.${it.first}" desc it.second }
        .plus(objectField)
        .toTypedArray()

fun <T> BodySpec<T, *>.document(
    identifier: String,
    nullableFields: Set<String> = emptySet(),
    init: (DocumentDsl<T>.() -> Unit)? = null
): BodySpec<T, *> =
    DocumentDsl(identifier, this, nullableFields)
        .apply { init?.let { it() } }
        .build()

class DocumentDsl<T>(
    private val identifier: String,
    private val contentSpec: BodySpec<T, *>,
    private val nullableFields: Set<String>
) {
    private val snippets: MutableList<Snippet> = mutableListOf()

    fun requestBody(fields: Array<Field>) {
        snippets.add(
            requestFields(
                fields.map {
                    fieldWithPath(it.first)
                        .description(it.second)
                }
            )
        )
    }

    fun requestForm(fields: Array<Field>) {
        snippets.add(
            requestParts(
                fields.map {
                    partWithName(it.first)
                        .description(it.second)
                }
            )
        )
    }

    fun pathParams(vararg fields: Field) {
        snippets.add(
            pathParameters(
                fields.map {
                    parameterWithName(it.first)
                        .description(it.second)
                }
            )
        )
    }

    fun queryParams(fields: Array<Field>) {
        snippets.add(
            queryParameters(
                fields.map {
                    parameterWithName(it.first)
                        .description(it.second)
                }
            )
        )
    }

    fun responseBody(fields: Array<Field>) {
        snippets.add(
            responseFields(
                fields.map {
                    val descriptor =
                        fieldWithPath(it.first)
                            .description(it.second)

                    if (nullableFields.contains(it.first)) {
                        descriptor.optional()
                    } else {
                        descriptor
                    }
                }
            )
        )
    }

    fun build(): BodySpec<T, *> =
        contentSpec.consumeWith(
            WebTestClientRestDocumentationWrapper.document(
                identifier,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                *snippets.toTypedArray()
            )
        )
}
