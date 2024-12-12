use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct Config {
    pub overlays_web_view: bool,
    pub background_color: String,
    pub style: String,
    #[cfg(target_os = "ios")]
    pub default_scroll_to_top: bool,
}

#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct OverlaysWebViewRequest {
    pub overlay: bool,
}

#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct BackgroundColorByHexStringRequest {
    pub color: String,
}
