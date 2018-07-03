package io.ankburov.universalconverter.model

import java.util.*

class TransformationRequest(
        val fileUUID: UUID,
        val mappings: Map<String, String>,
        val properties: Map<String, String> = emptyMap()
) {
}