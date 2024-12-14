package wang.tato.status_bar

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.view.WindowCompat
import kotlin.properties.Delegates


class StatusBar(private val activity: Activity) {
    val TAG: String = "StatusBar"

    private var backgroundColor by Delegates.notNull<Int>()
    private var lightStyle by Delegates.notNull<Boolean>()
    private var overlay by Delegates.notNull<Boolean>()

    init {
        activity.runOnUiThread {
            val windowInsetsControllerCompat = WindowCompat.getInsetsController(
                activity.window, activity.window.decorView
            )
            backgroundColor = activity.window.statusBarColor
            lightStyle = !windowInsetsControllerCompat.isAppearanceLightStatusBars
            overlay =
                (activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) != 0
            Log.v(
                TAG,
                "init: backgroundColor-$backgroundColor, lightStyle-$lightStyle, overlay-$overlay"
            )
        }
    }

    fun setStatusBar(options: SetStatusBarArgs?) {
        Log.v(
            TAG,
            "setStatusBar-args: backgroundColor-${options?.backgroundColor}, lightStyle-${options?.lightStyle}, overlay-${options?.overlay}"
        )
        if (options?.overlay !== null) {
            overlay = options.overlay!!
        }
        if (options?.lightStyle !== null) {
            lightStyle = options.lightStyle!!
        }
        if (options?.backgroundColor != null) {
            if (options.backgroundColor.equals("transparent")) {
                backgroundColor = Color.TRANSPARENT
            } else {
                try {
                    backgroundColor = Color.parseColor(options.backgroundColor)
                } catch (ignore: IllegalArgumentException) {
                    Log.e(TAG, "Invalid hexString argument, use f.i. '#999999'")
                }
            }
        }

        Log.v(
            TAG,
            "setStatusBar: backgroundColor-$backgroundColor, lightStyle-$lightStyle, overlay-$overlay"
        )

        if (!isVisible()) {
            show()
        }

        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (overlay) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = backgroundColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val windowInsetsControllerCompat = WindowCompat.getInsetsController(
                activity.window, activity.window.decorView
            )
            windowInsetsControllerCompat.isAppearanceLightStatusBars = !lightStyle
        }
    }

    private fun show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            activity.window.decorView.systemUiVisibility =
                activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv() and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    fun hide() {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
            activity.window.insetsController!!.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    fun isVisible(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.decorView.rootWindowInsets.isVisible(WindowInsets.Type.statusBars())
        } else {
            activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
        }
    }
}
