package com.haidev.storyapp.util

import android.util.Patterns

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidName() =
    !isNullOrEmpty() && Regex("^[A-Za-z]+\$").matches(this)

fun CharSequence?.isValidPassword() =
    !isNullOrEmpty() && length >= 6