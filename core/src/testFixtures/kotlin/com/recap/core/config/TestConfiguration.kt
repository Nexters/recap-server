package com.recap.core.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode

class TestConfiguration : AbstractProjectConfig() {
    override val isolationMode: IsolationMode = IsolationMode.InstancePerLeaf
}
