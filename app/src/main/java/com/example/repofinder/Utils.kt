package com.example.repofinder

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

/* Avoid showing integer too long on view */
fun Int.formatNumber(): String {
    if (this < 1000) return toString()
    // Subtract the remainder of 100. Ex: 1021 -> 1000
    val round = minus(rem(100))
    if (round.rem(1000) == 0) {
        return round.div(1000).toString() + "k"
    }
    return "%.1fk".format(round.div(1000f))
}

/* Hide keyboard when input box lost focus */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/* Adapter to bind image from response url */
@BindingAdapter("imgUrl")
fun image(img: ImageView, url: String) {
    Picasso.get().load(url).into(img)
}