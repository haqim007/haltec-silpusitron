package com.haltec.silpusitron.core.ui.util

import androidx.compose.runtime.Composable
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.core.module.Module
import org.koin.mp.KoinPlatformTools

@Composable
fun KoinPreviewWrapper(
    modules: List<Module>,
    content: @Composable () -> Unit
){
    val alreadyExists = KoinPlatformTools.defaultContext().getOrNull() != null
    if (!alreadyExists){
        KoinApplication(application = {
            modules(modules)
        }) {
            content()
        }
    }else{
        KoinContext {
            content()
        }
    }
}
