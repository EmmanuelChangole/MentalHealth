package com.example.mentalhealthapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.switchMap
import com.example.mentalhealthapp.database.mood.Mood
import com.example.mentalhealthapp.database.mood.MoodDao
import com.example.mentalhealthapp.database.mood.MoodDatabase
import com.example.mentalhealthapp.utils.subscribeOnBackground
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.EntryXComparator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticsFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar?.title="Statistics"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawMoodGraph(view)
    }

    private fun drawMoodGraph(view: View)
    {
        val moodDataBase=MoodDatabase.getInstance(view.context)
        val moodDao:MoodDao=moodDataBase.moodDao
        var allMoods:ArrayList<Mood>?=null
        var pastMoods:ArrayList<Mood> = ArrayList()
        var count:Int=0
        moodDao.getAllMood().observe(requireActivity(), Observer {
          for(item in it)
          {
              pastMoods.add(item)
              count++
              if(count ==5)
              {
                  break
              }

          }
            var entries:ArrayList<Entry> = ArrayList()
            for (mood in pastMoods)
            {
                entries.add(Entry(mood.value.toFloat(),mood.date.toFloat()))

            }
            Collections.sort(entries,EntryXComparator())

            var dataset=LineDataSet(entries,"Mood history")
            dataset.setColor(R.color.colorBottomNavActive)
            dataset.setValueTextColor(R.color.colorBottomNavActive)
            dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER)
            dataset.setDrawFilled(true)
            dataset.setLineWidth(4.0f)
            dataset.setHighlightLineWidth(4f)
            //dataset.setColor(ContextCompat.getColor(requireContext(), R.color.colorBottomNavActive))

            var chart:LineChart=view.findViewById(R.id.mood_chart)
            chart.getDescription().setEnabled(false)
            chart.getAxisLeft().setTextColor(Color.BLACK)
            chart.getXAxis().setTextColor(Color.BLACK)
            chart.getXAxis().setAxisMinimum(1f)
            chart.getXAxis().setAxisMaximum(12f)
            chart.getLegend().setEnabled(true)
            chart.getAxisLeft().setDrawGridLines(false)
            chart.getAxisRight().setDrawGridLines(false)
            val xAxis: XAxis = chart.getXAxis()
            val yAxis:YAxis=chart.axisLeft
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.disableAxisLineDashedLine()
            xAxis.setDrawLabels(true)

            //val mood = arrayOf("Sun", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
           val mood=arrayOf("Sad","Sad", "Disgusted", "Angry", "Fearful", "Bad", "Surprised","Happy")

            yAxis.valueFormatter=IndexAxisValueFormatter(mood)


            val yAxisRight = chart.axisRight
            yAxisRight.setDrawLabels(true)

            val lineData = LineData(dataset)
            chart.data = lineData
            chart.invalidate()




           // Toast.makeText(requireContext(),"${pastMoods.size}",Toast.LENGTH_LONG).show()
        })











    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatisticsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatisticsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}