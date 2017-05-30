package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.zip.Inflater;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 30-05-2017.
 */

public class WebViewFrag extends Fragment {
    View layout;
    WebView webView;

    public String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.webview_frag, container, false);

        // WebView webView = (WebView) layout.findViewById(R.id.webview);
        //webView.loadUrl(url);
        //--
        webView = (WebView) layout.findViewById(R.id.webview);
        //next line explained below

        //---
        webView.setWebViewClient(new CustomWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        return layout;
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
