package io.ankburov.kotlinxsltdsl.model

import java.util.*

class TransformationRequest(
        val fileUUID: UUID,
        val mappings: Map<String, String>
) {
}