use tauri::{AppHandle, Runtime};

use crate::models::*;
use crate::Result;
use crate::StatusBarExt;

#[tauri::command]
pub(crate) async fn overlays_web_view<R: Runtime>(
    app: AppHandle<R>,
    payload: OverlaysWebViewRequest,
) -> Result<()> {
    app.status_bar().overlays_web_view(payload)
}

#[tauri::command]
pub(crate) async fn style_default<R: Runtime>(app: AppHandle<R>) -> Result<()> {
    app.status_bar().style_default()
}

#[tauri::command]
pub(crate) async fn style_light_content<R: Runtime>(app: AppHandle<R>) -> Result<()> {
    app.status_bar().style_light_content()
}

#[tauri::command]
pub(crate) async fn background_color_by_hex_string<R: Runtime>(
    app: AppHandle<R>,
    payload: BackgroundColorByHexStringRequest,
) -> Result<()> {
    app.status_bar().background_color_by_hex_string(payload)
}

#[tauri::command]
pub(crate) async fn hide<R: Runtime>(app: AppHandle<R>) -> Result<()> {
    app.status_bar().hide()
}

#[tauri::command]
pub(crate) async fn show<R: Runtime>(app: AppHandle<R>) -> Result<()> {
    app.status_bar().show()
}

#[tauri::command]
pub(crate) async fn is_visible<R: Runtime>(app: AppHandle<R>) -> Result<bool> {
    app.status_bar().is_visible()
}
