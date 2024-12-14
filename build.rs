const COMMANDS: &[&str] = &[
    "set_status_bar",
    "hide",
    "is_visible",
    "registerListener",
];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
