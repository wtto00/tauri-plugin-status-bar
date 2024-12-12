import { addPluginListener, invoke } from "@tauri-apps/api/core";

/**
 * Make the statusbar overlay or not overlay the WebView.
 *
 * Set to true to make the statusbar overlay on top of your app.
 * Ensure that you adjust your styling accordingly so that
 * your app's title bar or content is not covered.
 * Set to false to make the statusbar solid and not overlay your app.
 * You can then set the style and background color to suit using the other functions.
 *
 * **Support iOS, Android@5+**
 * @param overlay
 */
export async function overlaysWebView(overlay: boolean) {
  return await invoke<void>("plugin:status-bar|overlays_web_view", {
    payload: { overlay },
  });
}

/**
 * Use the default statusbar (dark text, for light backgrounds).
 *
 * **Support iOS, Android@6+**
 */
export async function styleDefault() {
  return await invoke<void>("plugin:status-bar|style_default");
}

/**
 * Use the lightContent statusbar (light text, for dark backgrounds).
 *
 * **Support iOS, Android@6+**
 */
export async function styleLightContent() {
  return await invoke<void>("plugin:status-bar|style_light_content");
}

export enum NameColor {
  black = "#000000",
  darkGray = "#A9A9A9",
  lightGray = "#D3D3D3",
  white = "#FFFFFF",
  gray = "#808080",
  red = "#FF0000",
  green = "#00FF00",
  blue = "#0000FF",
  cyan = "#00FFFF",
  yellow = "#FFFF00",
  magenta = "#FF00FF",
  orange = "#FFA500",
  purple = "#800080",
  brown = "#A52A2A",
}

/**
 * On iOS, when you set StatusBar.overlaysWebView to false,
 * you can set the background color of the statusbar by color name.
 *
 * **Support iOS, Android**
 * @param colorName [NameColor]
 */
export async function backgroundColorByName(colorName: NameColor) {
  return await backgroundColorByHexString(colorName);
}

/**
 * Sets the background color of the statusbar by a hex string.
 * CSS shorthand properties are also supported.
 * On iOS, when you set StatusBar.overlaysWebView to false,
 * you can set the background color of the statusbar by a hex string (#RRGGBB).
 * On Android, when StatusBar.overlaysWebView is true,
 * you can also specify values as #AARRGGBB, where AA is an alpha value.
 *
 * **Support iOS, Android**
 * @param hexColor eg: '#C0C0C0', '#333', '3f333333'
 */
export async function backgroundColorByHexString(hexColor: string) {
  if (!hexColor) return;
  let color = hexColor;
  if (color.charAt(0) !== "#") {
    color = `#${color}`;
  }
  if (color.length === 4) {
    const [, r, g, b] = color.split("");
    color = `#${r}${r}${g}${g}${b}${b}`;
  }
  return await invoke<void>("plugin:status-bar|background_color_by_hex_string", {
    payload: { color },
  });
}

/**
 * Hide the statusbar.
 *
 * **Support iOS, Android**
 */
export async function hide() {
  return await invoke<void>("plugin:status-bar|hide");
}

/**
 * Shows the statusbar.
 *
 * **Support iOS, Android**
 */
export async function show() {
  return await invoke<void>("plugin:status-bar|show");
}

/**
 * Read this property to see if the statusbar is visible or not.
 *
 * **Support iOS, Android**
 */
export async function isVisible() {
  return await invoke<boolean>("plugin:status-bar|is_visible");
}

let _onStatusTap: (available: boolean) => void;
function onStatusTapCallback(available: boolean) {
  _onStatusTap?.(available);
}
/**
 * Listen for this event to know if the statusbar was tapped.
 *
 * **Only support iOS**
 * @param callback unlisten if callback is empty.
 */
export function onStatusTap(callback: () => void = () => {}) {
  if (!_onStatusTap) {
    addPluginListener("status-bar", "statusTap", onStatusTapCallback);
  }
  _onStatusTap = callback;
}
