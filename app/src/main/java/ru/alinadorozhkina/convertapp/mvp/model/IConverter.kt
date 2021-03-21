package ru.alinadorozhkina.convertapp.mvp.model

import io.reactivex.rxjava3.core.Completable

interface IConverter {
    fun convert(data: String): Completable
}