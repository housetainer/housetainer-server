package com.housetainer.configuration.context

import com.housetainer.common.utils.CurrentContextInfoUtils.copyContextMapToMdc
import org.reactivestreams.Subscription
import reactor.core.CoreSubscriber

class MdcContextLifter<T>(private val coreSubscriber: CoreSubscriber<T>) : CoreSubscriber<T> {

    override fun onSubscribe(s: Subscription) {
        coreSubscriber.onSubscribe(s)
    }

    override fun onNext(t: T) {
        currentContext().copyContextMapToMdc()
        coreSubscriber.onNext(t)
    }

    override fun onError(t: Throwable?) {
        currentContext().copyContextMapToMdc()
        coreSubscriber.onError(t)
    }

    override fun onComplete() {
        coreSubscriber.onComplete()
    }

    override fun currentContext() = coreSubscriber.currentContext()
}
