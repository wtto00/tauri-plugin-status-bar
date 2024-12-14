import { addPluginListener, invoke } from "@tauri-apps/api/core";

export interface SetStatusBarOptions {
  /** Make the statusbar overlay or not overlay the WebView. */
  overlay?: boolean;
  /**
   * Sets the background color of the statusbar by a hex string.
   *
   * @example
   * - 'transparent'
   * - '000'
   * - '#f34'
   * - '#f3d74b'
   * - '#3ff3d74b' (AARRGGBB)
   */
  backgroundColor?: string;
  /**
   * Use the lightContent statusbar (light text, for dark backgrounds).
   *
   * **Note**: In Android, only support version `>=6`.
   * In higher versions, the foreground color will automatically adapt to light or dark
   * based on the background color, which is also ineffective.
   */
  lightStyle?: boolean;
}

/**
 * Set and change the status bar.
 *
 * **Support iOS, Android**
 */
export async function setStatusBar(options?: SetStatusBarOptions) {
  let color = options?.backgroundColor;
  if (color && color !== "transparent") {
    if (color.charAt(0) !== "#") {
      color = `#${color}`;
    }
    if (color.length === 4) {
      const [, r, g, b] = color.split("");
      color = `#${r}${r}${g}${g}${b}${b}`;
    }
  }
  return await invoke<void>("plugin:status-bar|set_status_bar", {
    payload: { overlay: options?.overlay, lightStyle: options?.lightStyle, backgroundColor: color },
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
