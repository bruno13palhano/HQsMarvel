package com.bruno13palhano.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatchers: HQsMarvelDispatchers)

enum class HQsMarvelDispatchers {
    IO,
    DEFAULT
}