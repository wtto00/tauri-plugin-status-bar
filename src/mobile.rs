use serde::de::DeserializeOwned;
use tauri::{
    plugin::{PluginApi, PluginHandle},
    AppHandle, Runtime,
};

use crate::models::*;

#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_status_bar);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
    _app: &AppHandle<R>,
    api: PluginApi<R, C>,
) -> crate::Result<StatusBar<R>> {
    #[cfg(target_os = "android")]
    let handle = api.register_android_plugin("wang.tato.status_bar", "StatusBarPlugin")?;
    #[cfg(target_os = "ios")]
    let handle = api.register_ios_plugin(init_plugin_status_bar)?;
    Ok(StatusBar(handle))
}

/// Access to the status-bar APIs.
pub struct StatusBar<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> StatusBar<R> {
    pub fn overlays_web_view(&self, payload: OverlaysWebViewRequest) -> crate::Result<()> {
        self.0
            .run_mobile_plugin("overlaysWebView", payload)
            .map_err(Into::into)
    }

    pub fn style_default(&self) -> crate::Result<()> {
        self.0
            .run_mobile_plugin("styleDefault", ())
            .map_err(Into::into)
    }

    pub fn style_light_content(&self) -> crate::Result<()> {
        self.0
            .run_mobile_plugin("styleLightContent", ())
            .map_err(Into::into)
    }

    pub fn background_color_by_hex_string(
        &self,
        payload: BackgroundColorByHexStringRequest,
    ) -> crate::Result<()> {
        self.0
            .run_mobile_plugin("backgroundColorByHexString", payload)
            .map_err(Into::into)
    }

    pub fn hide(&self) -> crate::Result<()> {
        self.0.run_mobile_plugin("hide", ()).map_err(Into::into)
    }

    pub fn show(&self) -> crate::Result<()> {
        self.0.run_mobile_plugin("show", ()).map_err(Into::into)
    }

    pub fn is_visible(&self) -> crate::Result<bool> {
        self.0
            .run_mobile_plugin("isVisible", ())
            .map_err(Into::into)
    }
}