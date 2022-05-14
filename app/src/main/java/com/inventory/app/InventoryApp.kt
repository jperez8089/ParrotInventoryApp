package com.inventory.app

import android.app.Application
import com.inventory.app.data.DatabaseHandler

class InventoryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        DatabaseHandler.init(this)
    }
}