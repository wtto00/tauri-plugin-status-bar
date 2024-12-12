const COMMANDS: &[&str] = &[
    "overlays_web_view",
    "style_default",
    "style_light_content",
    "background_color_by_name",
    "background_color_by_hex_string",
    "hide",
    "show",
    "is_visible",
    "registerListener",
];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
