package com.example.mentalhealthapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException
import java.util.jar.Manifest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    lateinit var webView:WebView
    val MY_PERMISSIONS_REQUEST_LOCATION=101
    var mGeoLocationCallback:GeolocationPermissions.Callback? = null
    var mGeoLocationRequestOrigin:String ?= null
    private val mWebClient=object :WebChromeClient()
    {
        override fun onGeolocationPermissionsShowPrompt(
            origin: String,
            callback: GeolocationPermissions.Callback
        ) {


            // Do We need to ask for permission?
            if (getActivity()?.let {
                    ContextCompat.checkSelfPermission(
                        it.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity?.applicationContext as Activity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {

                    AlertDialog.Builder(activity?.applicationContext as Activity)
                        .setMessage(R.string.permission_location_rationale)
                        .setNeutralButton(android.R.string.ok) { _, _ ->
                            mGeoLocationRequestOrigin = origin
                            mGeoLocationCallback = callback

                            ActivityCompat.requestPermissions(
                                activity?.applicationContext as Activity,
                                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        }
                        .show()

                } else {
                    // No explanation needed, we can request the permission.

                    mGeoLocationRequestOrigin = origin
                    mGeoLocationCallback = callback
                    ActivityCompat.requestPermissions(
                        activity?.applicationContext as Activity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
            } else {
                // Tell the WebView that permission has been granted
                callback.invoke(origin, true, false)
            }
        }
    }


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar?.title="Search"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customTextArcProgress=view.findViewById<ArchedImageProgressBar>(R.id.linkedin_progressBar)

        customTextArcProgress.apply{
            setProgressText(arrayOf("Loading"), "#c5cae9")
            setProgressTextSize(13.0f)
            setArchSize(43.0f)
            setArchLength(120)
            setArchStroke(9.0f)
            setArchSpeed(3)

        }
        val mainLooper = Looper.getMainLooper()
        GlobalScope.launch{

            try {
                val mUrl="https://www.therapyroute.com/"
                val doc: org.jsoup.nodes.Document = Jsoup.connect(mUrl).get()
               doc.getElementsByClass("fb_reset").remove()
               doc.getElementsByClass("topholder curved-shadow hide-mobile").remove()
                doc.getElementsByClass("navigation-holder").remove()
                doc.getElementsByClass("bottomBanner").remove()
                doc.getElementsByTag("<header>").remove()

                Handler(mainLooper).post {
                 //   Toast.makeText(getActivity()?.applicationContext, doc.toString(), Toast.LENGTH_SHORT).show()
                    webView= view.findViewById<WebView>(R.id.webView)
                    //webView.webChromeClient=mWebClient
                    webView.webViewClient=SSLWebViewClient()
                    val webSettings: WebSettings = webView.settings
                    webSettings.javaScriptEnabled = true
                    webView.settings.setDomStorageEnabled(true)
                   // webView.settings.builtInZoomControls = true
                    webView.loadDataWithBaseURL(mUrl,doc.toString(),"text/html","utf-8","");
                    customTextArcProgress.visibility=View.GONE
                }

            } catch (e: IOException)
            {
                e.printStackTrace();

            }

        }


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, true, false)
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, false, false)
                }
            }
        }
        // other 'case' lines to check for other
        // permissions this app might request
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

private class SSLWebViewClient:WebViewClient()
{
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        if (handler != null) {
            handler.proceed()
        }

    }
}