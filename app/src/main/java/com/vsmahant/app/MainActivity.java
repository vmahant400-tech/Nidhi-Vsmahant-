package com.vsmahant.app;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {}
package com.vsmahant.app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.JavascriptInterface;
import com.getcapacitor.BridgeActivity;
import org.json.JSONObject;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // WebView mein NidhiNative Javascript Interface inject ho raha hai
        if (this.bridge != null && this.bridge.getWebView() != null) {
            this.bridge.getWebView().addJavascriptInterface(new NidhiNativeBridge(this), "NidhiNative");
        }
    }

    public class NidhiNativeBridge {
        android.content.Context context;

        public NidhiNativeBridge(android.content.Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public boolean isTermuxAvailable() {
            // Check karta hai ki Termux integration ready hai ya nahi
            return true; 
        }

        @JavascriptInterface
        public String requestAccessibility() {
            try {
                // Android System ki Accessibility Settings page ko open karta hai
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return "{\"enabled\": true}";
            } catch (Exception e) {
                return "{\"enabled\": false}";
            }
        }

        @JavascriptInterface
        public String runTermux(String jsonParams) {
            try {
                JSONObject obj = new JSONObject(jsonParams);
                String command = obj.optString("command", "");

                // Future Termux intent/execution handling
                String output = "Termux command received: " + command;
                
                return "{\"output\": \"" + output + "\"}";
            } catch (Exception e) {
                return "{\"output\": \"Error executing command\"}";
            }
        }
    }
}
