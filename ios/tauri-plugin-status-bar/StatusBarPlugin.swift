//
//  StatusBarPlugin.swift
//  tauri-plugin-status-bar
//
//  Created by wtto on 2024/12/16.
//

import OSLog
import SwiftRs
import Tauri
import UIKit
import WebKit

let log = OSLog(subsystem: "com.tauri.dev", category: "plugin.status_bar")

class SetStatusBarArgs: Decodable {
  let overlay: Bool?
  let backgroundColor: String?
  let lightStyle: Bool?
}

class StatusBarPlugin: Plugin {
  private var backgroundColor: UIColor = .clear
  private var lightStyle: Bool = false
  private var overlay: Bool = true
  private var statusBarWindow: UIWindow?

  override func load(webview: WKWebView) {
    super.load(webview: webview)

    if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
      if let statusBarFrame = windowScene.statusBarManager?.statusBarFrame {
        let window = UIWindow(frame: statusBarFrame)
        statusBarWindow = window
        os_log("statusBarWindow == nil? %{public}@", statusBarWindow == nil)
      }
    }

    if let statusBarFrame = UIApplication.shared.windows.first?.windowScene?.statusBarManager?.statusBarFrame {
      let statusBarView = UIView(frame: statusBarFrame)
      if let backgroundColor = statusBarView.backgroundColor {
        os_log(.debug, log: log, "backgroundColor：%{public}@", backgroundColor)
      } else {
        os_log(.debug, log: log, "backgroundColor not setting or transparent")
      }
    }

    let style = UITraitCollection.current.userInterfaceStyle
    if style == .dark {
      os_log(.debug, log: log, "style light text color")
    } else {
      os_log(.debug, log: log, "style dark text color")
    }

    if let window = UIApplication.shared.windows.first {
      let statusBarHeight = window.windowScene?.statusBarManager?.statusBarFrame.height ?? 0
      if webview.frame.origin.y <= statusBarHeight {
        os_log(.debug, log: log, "overlay true")
      } else {
        os_log(.debug, log: log, "overlay false")
      }
    }
  }

  @objc public func setStatusBar(_ invoke: Invoke) throws {
    let args = try invoke.parseArgs(SetStatusBarArgs.self)
    os_log(.debug, log: log, "setStatusBar-args: backgroundColor-%{public}@, lightStyle-%{public}@, overlay-%{public}@", args.backgroundColor ?? "", args.lightStyle ?? "", args.overlay ?? "")

    if args.overlay != nil {
      overlay = args.overlay!
    }
    if args.lightStyle != nil {
      lightStyle = args.lightStyle!
    }
    if args.backgroundColor != nil {
      if args.backgroundColor!.elementsEqual("transparent") {
        backgroundColor = UIColor.clear
      } else {
        backgroundColor = UIColor(hexString: args.backgroundColor!) ?? backgroundColor
      }
    }
    os_log(.debug, log: log, "setStatusBar: backgroundColor-%{public}@, lightStyle-%{public}@, overlay-%{public}@", backgroundColor, lightStyle, overlay)

    if lightStyle {
      manager.viewController?.overrideUserInterfaceStyle = .light
    } else {
      manager.viewController?.overrideUserInterfaceStyle = .dark
    }
    statusBarWindow?.backgroundColor = backgroundColor
    if !visible() {
      statusBarWindow?.isHidden = true
    }
  }

  @objc public func isVisible(_ invoke: Invoke) throws {
    let _visible = visible()
    os_log(.debug, log: log, "isVisible： %{public}@", _visible)
    invoke.resolve(_visible)
  }

  @objc public func hide(_ invoke: Invoke) throws {
    if #available(iOS 13.0, *) {
      if statusBarWindow != nil {
        statusBarWindow!.backgroundColor = .clear
        statusBarWindow!.windowLevel = .statusBar + 1
        statusBarWindow!.isHidden = false
      } else if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
        if let statusBarFrame = windowScene.statusBarManager?.statusBarFrame {
          let window = UIWindow(frame: statusBarFrame)
          window.backgroundColor = .clear
          window.windowLevel = .statusBar + 1
          window.isHidden = false
          statusBarWindow = window
        }
      }
    } else {
      UIApplication.shared.isStatusBarHidden = true
    }
  }

  func visible() -> Bool {
    if #available(iOS 13.0, *) {
      if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
        return !(windowScene.statusBarManager!.isStatusBarHidden)
      }
      return true
    } else {
      return !UIApplication.shared.isStatusBarHidden
    }
  }
}

@_cdecl("init_plugin_status_bar")
func initPlugin() -> Plugin {
  return StatusBarPlugin()
}

extension UIColor {
  /// - Parameters:
  ///   - hexString: `#RGB`、`#RRGGBB`、`#AARRGGBB`
  convenience init?(hexString: String) {
    var hex = hexString.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()

    if hex.hasPrefix("#") {
      hex.removeFirst()
    }

    var red: CGFloat = 0, green: CGFloat = 0, blue: CGFloat = 0, alpha: CGFloat = 1
    switch hex.count {
      case 3: // #RGB
        red = CGFloat(Int(String(hex[hex.startIndex]), radix: 16) ?? 0) / 15
        green = CGFloat(Int(String(hex[hex.index(hex.startIndex, offsetBy: 1)]), radix: 16) ?? 0) / 15
        blue = CGFloat(Int(String(hex[hex.index(hex.startIndex, offsetBy: 2)]), radix: 16) ?? 0) / 15
      case 6: // #RRGGBB
        red = CGFloat(Int(hex.prefix(2), radix: 16) ?? 0) / 255
        green = CGFloat(Int(hex.dropFirst(2).prefix(2), radix: 16) ?? 0) / 255
        blue = CGFloat(Int(hex.dropFirst(4).prefix(2), radix: 16) ?? 0) / 255
      case 8: // #AARRGGBB
        alpha = CGFloat(Int(hex.prefix(2), radix: 16) ?? 0) / 255
        red = CGFloat(Int(hex.dropFirst(2).prefix(2), radix: 16) ?? 0) / 255
        green = CGFloat(Int(hex.dropFirst(4).prefix(2), radix: 16) ?? 0) / 255
        blue = CGFloat(Int(hex.dropFirst(6).prefix(2), radix: 16) ?? 0) / 255
      default:
        os_log(.error, log: log, "Invalid hexString argument, use f.i. '#999999'")
        return nil
    }

    self.init(red: red, green: green, blue: blue, alpha: alpha)
  }
}
