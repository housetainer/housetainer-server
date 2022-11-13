package com.housetainer.configuration.context

import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Hooks
import reactor.core.publisher.Operators
import javax.annotation.PostConstruct

@Configuration
class MdcContextLifterConfiguration {

    val mdcContextReactorKey: String = MdcContextLifterConfiguration::class.java.name

    @PostConstruct
    fun contextOperatorHook() = Hooks.onEachOperator(
        mdcContextReactorKey,
        Operators.lift { _, subscriber ->
            MdcContextLifter(subscriber)
        }
    )
}
