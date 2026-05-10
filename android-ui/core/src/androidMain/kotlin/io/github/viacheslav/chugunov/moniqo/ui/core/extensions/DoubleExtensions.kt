package io.github.viacheslav.chugunov.moniqo.ui.core.extensions

import java.util.Locale

val Double.asPrice: String
    get() = String.format(Locale.US, "%,.2f", this).let { if (it == "-0.00") "0.00" else it }
