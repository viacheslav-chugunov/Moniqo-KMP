package io.github.viacheslav.chugunov.moniqo.ui.core

import android.content.Context
import androidx.annotation.StringRes

interface StringProvider {
    fun get(
        @StringRes resId: Int,
    ): String

    fun get(
        @StringRes resId: Int,
        vararg args: Any,
    ): String
}

class StringProviderImpl(
    private val context: Context,
) : StringProvider {
    override fun get(
        @StringRes resId: Int,
    ): String = context.getString(resId)

    override fun get(
        @StringRes resId: Int,
        vararg args: Any,
    ) = context.getString(resId, *args)
}
