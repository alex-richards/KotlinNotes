package com.alex.kotlinrxtest.notes.ui

import com.alex.kotlinrxtest.notes.storage.NoteRevision
import rx.Observable
import rx.Subscriber
import java.util.ArrayList

class NoteChosenOnSubscribe : Observable.OnSubscribe<NoteRevision> {

    val subscribers = ArrayList<Subscriber<in NoteRevision>>()

    override fun call(subscriber: Subscriber<in NoteRevision>) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber)
        }
    }

    fun noteChosen(note: NoteRevision) {
        subscribers.forEach { subscriber ->
            if (subscriber.isUnsubscribed) {
                subscribers.remove(subscriber)
            } else {
                subscriber.onNext(note)
            }
        }
    }
}