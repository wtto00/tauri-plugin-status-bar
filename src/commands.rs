use tauri::{AppHandle, Runtime};

use crate::models::*;
use crate::Result;
use crate::StatusBarExt;

#[tauri::command]
pub(crate) async fn set_status_bar<R: Runtime>(
    app: tauri::AppHandle<R>,
    payload: Option<SetStatusBarRequest>,
) -> Result<()> {
    app.status_bar().set_status_bar(payload)
}

#[tauri::command]
pub(crate) async fn hide<R: Runtime>(app: AppHandle<R>) -> Result<()> {
    app.status_bar().hide()
}

#[tauri::command]
pub(crate) async fn is_visible<R: Runtime>(app: AppHandle<R>) -> Result<bool> {
    app.status_bar().is_visible()
}
