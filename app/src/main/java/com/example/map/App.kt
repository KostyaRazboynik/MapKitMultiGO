package com.example.map

import android.app.Application
import com.yandex.mapkit.MapKitFactory


// API lite привязана к моему аккаунту (https://yandex.ru/dev/mapkit/doc/ru/android/quickstart)
const val MAP_KIT_API_KEY_BY_KOSTYA_RAZBOYNIK = "eae0b6e7-f77f-4f86-a33a-8bf67ab73d11"

// рандомное API full нашел в интернетах
const val MAP_KIT_API_KEY_BY_MIRUS_META = "18c3a22c-a43b-4e3b-a5e3-4cf4da322159"


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAP_KIT_API_KEY_BY_KOSTYA_RAZBOYNIK)
    }
}