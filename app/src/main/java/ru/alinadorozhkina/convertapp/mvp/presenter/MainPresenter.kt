package ru.alinadorozhkina.convertapp.mvp.presenter

import android.util.Log
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter
import ru.alinadorozhkina.convertapp.mvp.model.IConverter
import ru.alinadorozhkina.convertapp.mvp.view.MainView

class MainPresenter(
    val converter: IConverter,
    val uiSchedulers: Scheduler,
    val uriString: String
): MvpPresenter<MainView>() {

    private val compositeDisposable = CompositeDisposable()

    fun buttonConvertClick(){
        viewState.showLoading()
        val disposable =converter.convert(uriString)
            .observeOn(uiSchedulers)
            .subscribe({
                       viewState.onSuccess()
            },{
                if (it is InterruptedException){
                    viewState.onCancel()
                } else{
                    viewState.onError(it)
                }
            })
        compositeDisposable.add(disposable)
    }

    fun onCancelButtonClick(){
        Log.v("MainPresenter", "onCancelButtonClick()")
        compositeDisposable.dispose()
        viewState.onCancel()
    }

    override fun onDestroy() {
        Log.v("MainPresenter", "onDestroy()")
        compositeDisposable.dispose()
        super.onDestroy()
    }
}