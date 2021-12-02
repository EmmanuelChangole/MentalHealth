package com.example.mentalhealthapp

import android.graphics.Color.parseColor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mentalhealthapp.utils.subscribeOnBackground
import com.google.type.Color
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.util.concurrent.Callable


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = null
    private lateinit var webView:WebView;
    private lateinit var progressBar: ProgressBar
    private var TAG="DetailsFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //progressBar=view.findViewById(R.id.progressBar)
        //progressBar.display

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
                val mUrl="https://www.helpguide.org/?x=0&y=0&s=${title}"
                val doc: org.jsoup.nodes.Document = Jsoup.connect(mUrl).get()
                doc.getElementsByClass("banner").remove()
                doc.getElementById("donate-banner").remove()
                doc.getElementsByClass("content-info").remove()
                doc.getElementById("sub-footer").remove()
                doc.getElementsByClass("search-results-pagination").remove()
                Log.d(TAG,doc.toString())
                Handler(mainLooper).post {
                    webView= view.findViewById<WebView>(R.id.webview)
                    val webSettings: WebSettings = webView.settings
                    webSettings.javaScriptEnabled = false
                    webView.loadDataWithBaseURL(mUrl,doc.toString(),"text/html","utf-8","");
                    customTextArcProgress.visibility=View.GONE
                }

            } catch (e: IOException)
            {
                e.printStackTrace();

            }

        }



    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar?.title="Details"
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        fun newInstance(param1: String, param2: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}