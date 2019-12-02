package com.example.mypayrollactivity.myFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mypayrollactivity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StandfordAccessFragment extends Fragment {

	private WebView webView;
	public StandfordAccessFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_standford_access, container, false);
		webView =  view.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());

		webView.loadUrl("https://axess.sahr.stanford.edu/");
		return view;


	}

}
