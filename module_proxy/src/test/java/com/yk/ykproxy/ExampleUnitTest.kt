package com.yk.ykproxy

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun bitStatusTest() {
        println(WEATHER_A)
        println(WEATHER_B)
        println(W_A_IS_RAIN.toString())
        println(W_B_IS_RAIN.toString())
        println(W_B_IS_SUN.toString())
    }

    @Test
    fun intDefTest() {
        println(getSexChineseStr(SEX.MAN))
        println(getSexChineseStr(SEX.WOMAN))
        println(getSexChineseStr(0x0004))
    }
}
