package com.example.nfctictac.utils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

fun <VM: AndroidViewModel> ViewModelProvider.get(clazz: KClass<VM>) = get(clazz.java)

inline fun <reified VM: AndroidViewModel> ViewModelProvider.get() = get(VM::class)