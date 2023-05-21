package com.fmi.kentrack

fun Boolean.toOracleBoolean() = when (this) {
    true -> "Y"
    false -> "N"
}