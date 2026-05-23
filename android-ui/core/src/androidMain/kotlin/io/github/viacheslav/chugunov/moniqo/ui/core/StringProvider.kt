package io.github.viacheslav.chugunov.moniqo.ui.core

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

interface StringProvider {
    fun get(resource: StringResource): String

    fun get(resource: StringResource, vararg args: Any): String
}

class StringProviderImpl(
    private val context: Context,
) : StringProvider {
    override fun get(resource: StringResource): String =
        StringDesc.Resource(resource).toString(context)

    override fun get(resource: StringResource, vararg args: Any): String =
        StringDesc.ResourceFormatted(resource, *args).toString(context)
}
