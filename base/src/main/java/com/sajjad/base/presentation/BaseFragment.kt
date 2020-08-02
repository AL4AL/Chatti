package com.sajjad.base.presentation

import android.content.Context
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseFragment : Fragment() {

    protected lateinit var parentContext: Context
    protected val onStopCompositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.parentContext = context
    }

    override fun onStop() {
        onStopCompositeDisposable.dispose()
        super.onStop()
    }
}