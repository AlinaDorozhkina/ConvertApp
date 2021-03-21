package ru.alinadorozhkina.convertapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.alinadorozhkina.convertapp.R
import ru.alinadorozhkina.convertapp.databinding.ActivityMainBinding
import ru.alinadorozhkina.convertapp.mvp.presenter.MainPresenter
import ru.alinadorozhkina.convertapp.mvp.view.MainView
import ru.alinadorozhkina.convertapp.ui.AndroidConverter

private const val URI_STRING = "android.resource://ru.alinadorozhkina.convertapp/drawable/note"

class MainActivity : MvpAppCompatActivity(), MainView {

    private val ui: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var dialog: AlertDialog? = null

    private val presenter by moxyPresenter {
        MainPresenter(
            AndroidConverter(this),
            AndroidSchedulers.mainThread(),
            URI_STRING
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)

        ui.buttonConvert.setOnClickListener {
            presenter.buttonConvertClick()
        }
    }

    override fun onSuccess() {
        dialog?.dismiss()
        Snackbar.make(
            ui.root, R.string.success,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onError(t: Throwable) {
        dialog = AlertDialog.Builder(this)
            .setMessage(R.string.error)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                presenter.buttonConvertClick()
                dialog.dismiss()
            }
            .show()
    }

    override fun onCancel() {
        dialog = AlertDialog.Builder(this)
            .setMessage(R.string.cancel)
            .setNegativeButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun showLoading() {
        dialog = AlertDialog.Builder(this)
            .setMessage(R.string.converting)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                presenter.onCancelButtonClick()
                dialog.dismiss()
            }
            .show()
    }

    override fun onPause() {
        dialog?.dismiss()
        super.onPause()
    }
}