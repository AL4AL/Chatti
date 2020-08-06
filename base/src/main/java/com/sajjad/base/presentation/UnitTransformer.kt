package com.sajjad.base.presentation

import android.content.Context
import android.util.TypedValue


object UnitTransformer {

    @JvmStatic
    fun dp2px(context: Context, dp: Float): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}