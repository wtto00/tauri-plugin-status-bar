package wang.tato.status_bar

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Invoke
import app.tauri.plugin.Plugin
import kotlin.properties.Delegates


@InvokeArg
class Config {
    var overlaysWebView: Boolean = true
    var backgroundColor: String = "#000000"
    var style: String = "lightcontent"
}

@InvokeArg
class OverlaysWebViewArgs {
    var overlay by Delegates.notNull<Boolean>()
}

@InvokeArg
class BackgroundColorByHexStringArgs {
    lateinit var color: String
}

@TauriPlugin
class StatusBarPlugin(private val activity: Activity) : Plugin(activity) {
    private val implementation = StatusBar(activity)

    override fun load(webView: WebView) {
        Log.v(implementation.TAG, "StatusBar: initialization");
        super.load(webView)

        activity.runOnUiThread {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            getConfig(Config::class.java).let {
                // Read 'overlaysWebView' from tauri.conf.json, default is true.
                implementation.setStatusBarTransparent(it?.overlaysWebView ?: true);

                // Read 'StatusBarBackgroundColor' from config.xml, default is #000000.
                implementation.setStatusBarBackgroundColor(it?.backgroundColor ?: "#000000");

                // Read 'StatusBarStyle' from config.xml, default is 'lightcontent'.
                implementation.setStatusBarStyle(
                    (it?.style ?: implementation.STYLE_LIGHT_CONTENT).lowercase()
                );
            }
        }
    }

    @Command
    fun overlaysWebView(invoke: Invoke) {
        activity.runOnUiThread {
            val args = invoke.parseArgs(OverlaysWebViewArgs::class.java)
            implementation.setStatusBarTransparent(args.overlay)
            invoke.resolve()
        }
    }

    @Command
    fun styleDefault(invoke: Invoke) {
        activity.runOnUiThread {
            implementation.setStatusBarStyle(implementation.STYLE_DEFAULT)
            invoke.resolve()
        }
    }

    @Command
    fun styleLightContent(invoke: Invoke) {
        activity.runOnUiThread {
            implementation.setStatusBarStyle(implementation.STYLE_LIGHT_CONTENT)
            invoke.resolve()
        }
    }

    @Command
    fun backgroundColorByHexString(invoke: Invoke) {
        activity.runOnUiThread {
            val args = invoke.parseArgs(BackgroundColorByHexStringArgs::class.java)
            implementation.setStatusBarBackgroundColor(args.color)
            invoke.resolve()
        }
    }

    @Command
    fun hide(invoke: Invoke) {
        activity.runOnUiThread {
            val uiOptions: Int = (activity.window.decorView.systemUiVisibility
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            activity.window.decorView.systemUiVisibility = uiOptions

            // CB-11197 We still need to update LayoutParams to force status bar
            // to be hidden when entering e.g. text fields
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            invoke.resolve()
        }
    }

    @Command
    fun show(invoke: Invoke) {
        activity.runOnUiThread {
            var uiOptions: Int = activity.window.decorView.systemUiVisibility
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()

            activity.window.decorView.systemUiVisibility = uiOptions

            // CB-11197 We still need to update LayoutParams to force status bar
            // to be hidden when entering e.g. text fields
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    @Command
    fun isVisible(invoke: Invoke) {
        val statusBarVisible =
            activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN === 0
        invoke.resolveObject(statusBarVisible)
    }
}
