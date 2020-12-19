package com.github.karsaig.approvalcrest.kotlin

import com.github.karsaig.approvalcrest.jupiter.Junit5InfoBasedTestMeta
import com.github.karsaig.approvalcrest.kotlin.KotlinTestMeta.Companion.SRC_TEST_KOTLIN_PATH
import org.junit.jupiter.api.TestInfo
import java.nio.file.Path

class KotlinInfoBasedTestMeta: Junit5InfoBasedTestMeta {

    constructor(testInfo: TestInfo) : super(testInfo)

    internal constructor(testClassPath: Path, testClassName: String, testMethodName: String, approvedDirectory: Path) :
            super(testClassPath, testClassName, testMethodName, approvedDirectory)

    override fun getSourceRoutePathString(): String = SRC_TEST_KOTLIN_PATH

}