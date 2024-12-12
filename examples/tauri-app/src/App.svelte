<script>
import Greet from "./lib/Greet.svelte";
import {
	backgroundColorByHexString,
	backgroundColorByName,
	hide,
	isVisible,
	NameColor,
	overlaysWebView,
	show,
	styleDefault,
	styleLightContent,
} from "tauri-plugin-status-bar-api";

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
	overlaysWebView(!isOverlaysWebView)
		.then(() => {
			updateResponse(`Change overlaysWebView to ${!isOverlaysWebView}`);
			isOverlaysWebView = !isOverlaysWebView;
		})
		.catch((err) => {
			updateResponse(`Change overlaysWebView error: ${err}`);
		});
}

function _styleDefault() {
	styleDefault()
		.then(() => {
			updateResponse("styleDefault OK");
		})
		.catch((err) => {
			updateResponse(`styleDefault error: ${err}`);
		});
}
function _styleLightContent() {
	styleLightContent()
		.then(() => {
			updateResponse("styleLightContent OK");
		})
		.catch((err) => {
			updateResponse(`styleLightContent error: ${err}`);
		});
}
const colorNames = Object.values(NameColor);
let i = 0;
function _backgroundColorByName() {
	if (i >= colorNames.length) i = 0;
	const color = colorNames[i];
	backgroundColorByName(color)
		.then(() => {
			updateResponse(`backgroundColorByName(${color}) OK`);
		})
		.catch((err) => {
			updateResponse(`backgroundColorByName(${color}) error: ${err}`);
		});
}
function _backgroundColorByHexString() {
	backgroundColorByHexString("#A76")
		.then(() => {
			updateResponse("backgroundColorByHexString OK");
		})
		.catch((err) => {
			updateResponse(`backgroundColorByHexString error: ${err}`);
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
	show()
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
  <div class="row">
    <Greet />
  </div>

  <div>
    <button on:click="{_overlaysWebView}">overlaysWebView</button>
    <button on:click="{_styleDefault}">styleDefault</button>
    <button on:click="{_styleLightContent}">styleLightContent</button>
    <button on:click="{_backgroundColorByName}">backgroundColorByName</button>
    <button on:click="{_backgroundColorByHexString}">backgroundColorByHexString</button>
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
</style>
