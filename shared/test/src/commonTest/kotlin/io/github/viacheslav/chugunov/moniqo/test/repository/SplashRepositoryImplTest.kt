@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.github.viacheslav.chugunov.moniqo.test.repository

import io.github.viacheslav.chugunov.moniqo.core.repository.SplashRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SplashRepositoryImplTest {
    @Test
    fun `isReady emits false initially`() =
        runTest {
            assertFalse(SplashRepositoryImpl().isReady().first())
        }

    @Test
    fun `isReady emits true after setReady`() =
        runTest {
            val repo = SplashRepositoryImpl()
            repo.setReady()

            assertTrue(repo.isReady().first())
        }

    @Test
    fun `setReady is idempotent`() =
        runTest {
            val repo = SplashRepositoryImpl()
            repo.setReady()
            repo.setReady()

            assertTrue(repo.isReady().first())
        }
}
