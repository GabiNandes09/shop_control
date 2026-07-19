package com.rogue.shopcontrol.di

import com.rogue.shopcontrol.data.parser.NfceHtmlParser
import org.koin.dsl.module

val parseModule = module {

    single {
        NfceHtmlParser()
    }

}