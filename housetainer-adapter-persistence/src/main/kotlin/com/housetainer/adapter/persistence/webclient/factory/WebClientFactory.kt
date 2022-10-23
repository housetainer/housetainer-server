package com.housetainer.adapter.persistence.http.webclient.factory

import com.housetainer.adapter.persistence.http.webclient.factory.properties.WebClientProperties
import com.housetainer.adapter.persistence.webclient.filter.WebClientFilter
import com.housetainer.common.log.logger
import com.housetainer.common.utils.CommonUtils
import io.netty.channel.ChannelOption
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties(prefix = "web-client")
class WebClientFactory(
    val defaultProperties: WebClientProperties = WebClientProperties(),
    val services: Map<String, WebClientProperties>
) : BeanFactoryAware {

    private val log = logger()

    private lateinit var beanFactory: ConfigurableBeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {
        Assert.state(beanFactory is ConfigurableBeanFactory, "wrong bean factory type")
        this.beanFactory = beanFactory as ConfigurableBeanFactory
    }

    @PostConstruct
    fun configure() {
        services.entries.forEach { (name, properties) ->
            properties.overrideProperties(name, defaultProperties)
            registerPropertyBean(name, properties)
            registerWebClientBean(name, properties)
        }
    }

    private fun registerPropertyBean(name: String, properties: WebClientProperties) {
        val propertyBeanName = "${name.convertToBeanName()}WebClientProperties"
        beanFactory.registerSingleton(propertyBeanName, properties)
        log.info(
            "create-webClient-property, name={}, beanName={}, properties={}",
            name,
            propertyBeanName,
            properties
        )
    }

    private fun registerWebClientBean(name: String, properties: WebClientProperties) {
        val webClientBeanName = "${name.convertToBeanName()}WebClientPair"
        val webClientPair = createWebClient(properties)
        beanFactory.registerSingleton(webClientBeanName, webClientPair)
        log.info("create-webClient, name={}, beanName={}", name, webClientBeanName)
    }

    fun createWebClient(
        properties: WebClientProperties,
        block: WebClient.Builder.() -> Unit = {}
    ): WebClientPair = WebClientPair(
        WebClient.builder()
            .clientConnector(properties.toConnector())
            .baseUrl(properties.url)
            .filter(WebClientFilter.loggingErrorResult())
            .codecs {
                it.defaultCodecs().apply {
                    this.jackson2JsonEncoder(Jackson2JsonEncoder(CommonUtils.mapper, MediaType.APPLICATION_JSON))
                    this.jackson2JsonDecoder(Jackson2JsonDecoder(CommonUtils.mapper, MediaType.APPLICATION_JSON))
                }
            }
            .apply(block)
            .build(),
        properties
    )

    private fun WebClientProperties.toConnector(): ReactorClientHttpConnector {
        log.info("initial-web-client-properties, {}", this)

        val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()

        val provider = ConnectionProvider.builder("$name-provider")
            .apply {
                if (maxConnections != null) {
                    maxConnections(maxConnections!!)
                }
                if (maxIdleTime != null) {
                    maxIdleTime(maxIdleTime!!)
                }
            }
            .build()

        log.info("initial-web-client-properties, name={}, maxConnections={}", name, provider.maxConnections())

        val httpClient = HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
            .secure { it.sslContext(sslContext) }
            .doOnConnected {
                it
                    .addHandlerLast(ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))
            }
        return ReactorClientHttpConnector(httpClient)
    }

    fun String.convertToBeanName(): String {
        val tokens = this.split("_", "-", " ")

        val capitalizing: String = tokens
            .drop(1)
            .joinToString("") { word ->
                word.replaceFirstChar { char ->
                    char.uppercaseChar()
                }
            }

        return tokens.first() + capitalizing
    }
}
