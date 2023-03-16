package com.relaxgao.base.serviceloader

import java.lang.Exception
import java.util.*

object RelaxServiceLoader {

    fun <S> load(service: Class<S>?): S? {
        return try {
            ServiceLoader.load(service).iterator().next()
        } catch (e: Exception) {
            null
        }
    }

}