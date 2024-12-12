#[cfg(mobile)]
use tauri::{
    plugin::{Builder, TauriPlugin},
    Manager, Runtime,
};

#[cfg(mobile)]
pub use models::*;

#[cfg(mobile)]
mod mobile;

#[cfg(mobile)]
mod commands;
#[cfg(mobile)]
mod error;
#[cfg(mobile)]
mod models;

#[cfg(mobile)]
pub use error::{Error, Result};

#[cfg(mobile)]
use mobile::StatusBar;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the status-bar APIs.
#[cfg(mobile)]
pub trait StatusBarExt<R: Runtime> {
    fn status_bar(&self) -> &StatusBar<R>;
}

#[cfg(mobile)]
impl<R: Runtime, T: Manager<R>> crate::StatusBarExt<R> for T {
    fn status_bar(&self) -> &StatusBar<R> {
        self.state::<StatusBar<R>>().inner()
    }
}

/// Initializes the plugin.
#[cfg(mobile)]
pub fn init<R: Runtime>() -> TauriPlugin<R, Option<Config>> {
    Builder::<R, Option<Config>>::new("status-bar")
        .invoke_handler(tauri::generate_handler![
            commands::overlays_web_view,
            commands::style_default,
            commands::style_light_content,
            commands::background_color_by_hex_string,
            commands::hide,
            commands::show,
            commands::is_visible,
        ])
        .setup(|app, api| {
            let status_bar = mobile::init(app, api)?;
            app.manage(status_bar);
            Ok(())
        })
        .build()
}
