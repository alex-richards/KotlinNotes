package com.alex.kotlinrxtest

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import rx.Observable
import java.io.Serializable
import java.util.concurrent.TimeUnit

fun Context.applicationComponent() = (applicationContext as com.alex.kotlinrxtest.Application).applicationComponent

fun <T> Fragment.getListener(t: Class<T>): T = listOf(parentFragment, activity).filterIsInstance(t).single()

infix fun <A, B> A.and(that: B): Pair<A, B> = Pair(this, that)

fun Bundle.set(pair: Pair<String, Serializable>): Bundle {
    putSerializable(pair.first, pair.second)
    return this
}

fun <T> Observable<T>.uiThrottle(): Observable<T> {
    return throttleFirst(800, TimeUnit.MILLISECONDS)
}

fun <T> Observable<T>.editThrottle(): Observable<T> {
    return throttleFirst(2, TimeUnit.SECONDS)
}
