package wang.tato.status_bar

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Invoke
import app.tauri.plugin.Plugin


@InvokeArg
class SetStatusBarArgs {
    var overlay: Boolean? = null
    var backgroundColor: String? = null
    var lightStyle: Boolean? = null
}

@TauriPlugin
class StatusBarPlugin(private val activity: Activity) : Plugin(activity) {
    private val implementation = StatusBar(activity)

    @Command
    fun setStatusBar(invoke: Invoke) {
        activity.runOnUiThread {
            val args = invoke.parseArgs(SetStatusBarArgs::class.java)
            implementation.setStatusBar(args)
            invoke.resolve()
        }
    }

    @Command
    fun hide(invoke: Invoke) {
        activity.runOnUiThread {
            implementation.hide()
            invoke.resolve()
        }
    }

    @Command
    fun isVisible(invoke: Invoke) {
        activity.runOnUiThread {
            invoke.resolveObject(implementation.isVisible())
        }
    }
}
