package com.alex.kotlinrxtest

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import java.io.Serializable

fun Context.applicationComponent() = (applicationContext as com.alex.kotlinrxtest.Application).applicationComponent

fun <T> Fragment.getListener(t: Class<T>): T = listOf(parentFragment, activity).filterIsInstance(t).single()

infix fun <A, B> A.and(that: B): Pair<A, B> = Pair(this, that)

fun Bundle.set(pair: Pair<String, Serializable>): Bundle {
    putSerializable(pair.first, pair.second)
    return this
}
