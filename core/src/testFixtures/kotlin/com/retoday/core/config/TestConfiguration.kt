package com.retoday.core.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.extensions.spring.SpringExtension

class TestConfiguration : AbstractProjectConfig() {
    override val isolationMode: IsolationMode = IsolationMode.InstancePerLeaf

    override fun extensions(): List<Extension> = listOf(SpringExtension)
}
