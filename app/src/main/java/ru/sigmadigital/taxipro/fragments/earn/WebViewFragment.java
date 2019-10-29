package ru.sigmadigital.taxipro.fragments.earn;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.AcquiringBank;

public class WebViewFragment  extends BaseNavigatorFragment {

    private AcquiringBank.HasResponded hasResponded;
    private ProgressBar progressBar;
    private TextView textError;
    private TextView textBlank;
    private WebView webView;

    static WebViewFragment newInstance(AcquiringBank.HasResponded hasResponded) {
        Bundle args = new Bundle();
        args.putSerializable("HasResponded", hasResponded);

        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("HasResponded")) {
            hasResponded = (AcquiringBank.HasResponded) getArguments().getSerializable("HasResponded");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_web_view, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        progressBar = v.findViewById(R.id.loading);
        textError = v.findViewById(R.id.textError);
        textBlank = v.findViewById(R.id.textBlank);
        webView = v.findViewById(R.id.web_view);
        setWebClient();
        Log.e("url", hasResponded.getFormUrl());
        webView.loadUrl(hasResponded.getFormUrl());

        return v;
    }


    private void setWebClient() {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("OverrideUrlLoading", "load url:" + url);

                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("onPageStarted", url);

                if ((url.contains("taxipro") || url.contains("globaxi")) && !url.contains("merchants")){
                    if (getActivity() != null){
                        getActivity().onBackPressed();
                    }
                }

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("onPageFinished", url);

                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                if (url.equals("about:blank")) {
                    textBlank.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("onReceivedError", hasResponded.getFormUrl());

                progressBar.setVisibility(View.GONE);
                textError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showCloseButton(true);
            ((MainActivity) getActivity()).showBottomNav(false);
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showCloseButton(false);
            ((MainActivity) getActivity()).showBottomNav(true);
        }*/
    }


}
