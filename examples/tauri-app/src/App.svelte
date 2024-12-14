<script>
import { setStatusBar, hide, isVisible } from "tauri-plugin-status-bar-api";

let response = "";

function updateResponse(returnValue) {
	response += `[${new Date().toLocaleTimeString()}] ${
		typeof returnValue === "string" ? returnValue : JSON.stringify(returnValue)
	}<br>`;
}

function _clearLogs() {
	response = "";
}

let isOverlaysWebView = false;

function _overlaysWebView() {
	setStatusBar({
		overlay: !isOverlaysWebView,
		backgroundColor: !isOverlaysWebView ? "transparent" : "#000",
		lightStyle: isOverlaysWebView,
	})
		.then(() => {
			updateResponse(`Change overlaysWebView to ${!isOverlaysWebView}`);
			isOverlaysWebView = !isOverlaysWebView;
		})
		.catch((err) => {
			updateResponse(`Change overlaysWebView error: ${err}`);
		});
}

function _styleDarkContent() {
	setStatusBar({ lightStyle: false })
		.then(() => {
			updateResponse("styleDarkContent OK");
		})
		.catch((err) => {
			updateResponse(`styleDarkContent error: ${err}`);
		});
}
function _styleLightContent() {
	setStatusBar({ lightStyle: true })
		.then(() => {
			updateResponse("styleLightContent OK");
		})
		.catch((err) => {
			updateResponse(`styleLightContent error: ${err}`);
		});
}
function _background() {
	const color = Math.random() > 0.5 ? "#000" : "#A76";
	setStatusBar({ backgroundColor: color })
		.then(() => {
			updateResponse(`backgroundColor ${color} OK`);
		})
		.catch((err) => {
			updateResponse(`backgroundColor error: ${err}`);
		});
}
function _hide() {
	hide()
		.then(() => {
			updateResponse("hide OK");
		})
		.catch((err) => {
			updateResponse(`hide error: ${err}`);
		});
}
function _show() {
	setStatusBar()
		.then(() => {
			updateResponse("show OK");
		})
		.catch((err) => {
			updateResponse(`show error: ${err}`);
		});
}
function _isVisible() {
	isVisible()
		.then((res) => {
			updateResponse(`isVisible OK: ${res}`);
		})
		.catch((err) => {
			updateResponse(`isVisible error: ${err}`);
		});
}
function _onStatusTap() {}
</script>

<main class="container">
  <h1>Welcome to Tauri!</h1>

  <div class="row">
    <a href="https://vitejs.dev" target="_blank">
      <img src="/vite.svg" class="logo vite" alt="Vite Logo" />
    </a>
    <a href="https://tauri.app" target="_blank">
      <img src="/tauri.svg" class="logo tauri" alt="Tauri Logo" />
    </a>
    <a href="https://svelte.dev" target="_blank">
      <img src="/svelte.svg" class="logo svelte" alt="Svelte Logo" />
    </a>
  </div>

  <p>
    Click on the Tauri, Vite, and Svelte logos to learn more.
  </p>

	<button on:click="{_clearLogs}">clear logs</button>
  <div>
    <button on:click="{_overlaysWebView}">overlaysWebView</button>
    <button on:click="{_styleDarkContent}">styleDarkContent</button>
    <button on:click="{_styleLightContent}">styleLightContent</button>
    <button on:click="{_background}">backgroundColor</button>
    <button on:click="{_hide}">hide</button>
    <button on:click="{_show}">show</button>
    <button on:click="{_isVisible}">isVisible</button>
    <button on:click="{_onStatusTap}">onStatusTap</button>
    <div>{@html response}</div>
  </div>

</main>

<style>
  .logo.vite:hover {
    filter: drop-shadow(0 0 2em #747bff);
  }

  .logo.svelte:hover {
    filter: drop-shadow(0 0 2em #ff3e00);
  }

	button {
		margin-bottom: 12px;
	}
</style>
