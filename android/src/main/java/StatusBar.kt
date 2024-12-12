package wang.tato.status_bar


import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.WindowCompat

class StatusBar(private val activity: Activity) {
    val TAG: String = "StatusBar"
    val STYLE_DEFAULT: String = "default"
    val STYLE_LIGHT_CONTENT: String = "lightcontent"

    fun setStatusBarBackgroundColor(colorPref: String) {
        if (colorPref.isEmpty()) return

        val color: Int
        try {
            color = Color.parseColor(colorPref)
        } catch (ignore: IllegalArgumentException) {
            Log.e(TAG, "Invalid hexString argument, use f.i. '#999999'")
            return
        }

        activity.window!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // SDK 19-30
        activity.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // SDK 21
        activity.window!!.statusBarColor = color
    }

    fun setStatusBarTransparent(isTransparent: Boolean) {
        val visibility = if (isTransparent)
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        else
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_VISIBLE

        activity.window.decorView.systemUiVisibility = visibility

        if (isTransparent) {
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun setStatusBarStyle(style: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && style.isNotEmpty()) {
            val decorView = activity.window.decorView
            val windowInsetsControllerCompat = WindowCompat.getInsetsController(
                activity.window, decorView
            )

            when (style) {
                STYLE_DEFAULT -> windowInsetsControllerCompat.isAppearanceLightStatusBars = true
                STYLE_LIGHT_CONTENT -> windowInsetsControllerCompat.isAppearanceLightStatusBars = false
                else -> Log.e(TAG, "Invalid style, must be either 'default' or 'lightcontent'")
            }
        }
    }
}
