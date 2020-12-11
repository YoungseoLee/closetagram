package com.example.closetagram.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("bind:onFabClick")
fun setOnFabButtonClickListener(
    view: FloatingActionButton,
    listener: View.OnClickListener?
) {
    listener ?: return
    view.setOnClickListener(listener)
}