package com.retoday.api.extension

import com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import kotlin.reflect.KProperty

data class Field(
    val name: String,
    val description: String,
    val isOptional: Boolean = false
)

fun optional(field: Field): Field = field.copy(isOptional = true)

infix fun String.desc(description: String): Field =
    Field(
        name = this,
        description = description
    )

infix fun <T> KProperty<T>.desc(description: String): Field =
    Field(
        name = name,
        description = description
    )

fun fieldsOf(vararg fields: Field): Array<Field> = fields as Array<Field>

fun listFieldsOf(
    listField: Field,
    vararg fields: Field
): Array<Field> =
    fields
        .map { "${listField.name}[].${it.name}" desc it.description }
        .plus(listField)
        .toTypedArray()

fun objectFieldsOf(
    objectField: Field,
    vararg fields: Field
): Array<Field> =
    fields
        .map { "${objectField.name}.${it.name}" desc it.description }
        .plus(objectField)
        .toTypedArray()

fun <T> BodySpec<T, *>.document(
    identifier: String,
    init: (DocumentDsl<T>.() -> Unit)? = null
): BodySpec<T, *> =
    DocumentDsl(identifier, this)
        .apply { init?.let { it() } }
        .build()

class DocumentDsl<T>(
    private val identifier: String,
    private val contentSpec: BodySpec<T, *>
) {
    private val snippets: MutableList<Snippet> = mutableListOf()

    fun requestBody(fields: Array<Field>) {
        snippets.add(
            requestFields(
                fields.map {
                    fieldWithPath(it.name)
                        .description(it.description)
                        .apply { if (it.isOptional) optional() }
                }
            )
        )
    }

    fun requestForm(fields: Array<Field>) {
        snippets.add(
            requestParts(
                fields.map {
                    partWithName(it.name)
                        .description(it.description)
                        .apply { if (it.isOptional) optional() }
                }
            )
        )
    }

    fun pathParams(vararg fields: Field) {
        snippets.add(
            pathParameters(
                fields.map {
                    parameterWithName(it.name)
                        .description(it.description)
                        .apply { if (it.isOptional) optional() }
                }
            )
        )
    }

    fun queryParams(fields: Array<Field>) {
        snippets.add(
            queryParameters(
                fields.map {
                    parameterWithName(it.name)
                        .description(it.description)
                        .apply { if (it.isOptional) optional() }
                }
            )
        )
    }

    fun responseBody(fields: Array<Field>) {
        snippets.add(
            responseFields(
                fields.map {
                    fieldWithPath(it.name)
                        .description(it.description)
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
