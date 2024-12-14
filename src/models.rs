use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct SetStatusBarRequest {
    pub overlay: Option<bool>,
    pub background_color: Option<String>,
    pub light_style: Option<bool>,
}
