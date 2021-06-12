package learn.codeacademy.digitalcurator_movieortv_seriesinfo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.searchviewdrawable.*
import learn.codeacademy.adapter.RecyclerAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    var actor:String = ""
    var character:String = ""
    var array = JSONArray()
    var characterarray = JSONArray()
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tf = ResourcesCompat.getFont(this, R.font.gilroy_light)
        val id: Int = searchview.context.resources.getIdentifier(
            "android:id/search_src_text",
            null,
            null
        )

        val searchText = searchview.findViewById<TextView>(id)
        searchText.setTypeface(tf)
        val neededtext = searchText.toString()

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("TAG", "$query")
                val neededtxt = query.toString()
                val thread = Thread {
                    kotlin.run {


                        val client = OkHttpClient()

                        val request = Request.Builder()
                            .url("https://imdb-internet-movie-database-unofficial.p.rapidapi.com/film/$query")
                            .get()
                            .addHeader(
                                "x-rapidapi-key",
                                "3cf87b7362mshb0aa4e5238acaaep139e9ajsn60193dcabdd2"
                            )
                            .addHeader(
                                "x-rapidapi-host",
                                "imdb-internet-movie-database-unofficial.p.rapidapi.com"
                            )
                            .build()

                        val response = client.newCall(request).execute()
                        when {
                            response.isSuccessful -> {
                                Log.d("TAG", "$response")
                                Thread {
                                    kotlin.run {
                                        val data = response.body()!!.string()
                                        Log.d("TAG", "$data")
                                        val jsonObject = JSONObject(data)
                                        var title = jsonObject.getString("title")
                                        val year = jsonObject.getString("year")
                                        val length = jsonObject.getString("length")
                                        val imageurl = jsonObject.getString("poster")
                                        val trailer = jsonObject.getJSONObject("trailer")
                                        val rating = jsonObject.getString("rating")
                                        val id = jsonObject.getString("id")
                                        val plot = jsonObject.getString("plot")
                                        val cast = jsonObject.getJSONArray("cast")

                                        for (i in 0 until cast.length()){
                                            val filter = cast.getJSONObject(i)
                                             actor = filter.getString("actor")
                                             character = filter.getString("character")
                                            array.put(actor)
                                            characterarray.put(character)


                                        }
                                        Log.d("TAG","array is $characterarray")

                                        val rv_transaction =
                                            findViewById<RecyclerView>(R.id.recyclerview)
                                        runOnUiThread(Runnable {
                                            rv_transaction.setHasFixedSize(true)
                                            rv_transaction.layoutManager = LinearLayoutManager(
                                                applicationContext!!,
                                                LinearLayout.VERTICAL,
                                                false
                                            )
                                            rv_transaction.adapter = RecyclerAdapter(
                                                title,
                                                year,
                                                length,
                                                imageurl
                                            )
                                            rv_transaction.setOnTouchListener { _, _ ->
                                                val intent = Intent(
                                                    this@MainActivity,
                                                    Movieactivity::class.java
                                                )
                                                intent.putExtra("actor", array.toString())
                                                intent.putExtra("castlength", cast.length())

                                                intent.putExtra("character",characterarray.toString())
                                                intent.putExtra("id", id)
                                                intent.putExtra("title","$title")
                                                intent.putExtra("plot","$plot")
                                                intent.putExtra("length","$length")
                                                intent.putExtra("rating","$rating")
                                                intent.putExtra("imageurl","$imageurl")
                                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                                startActivity(intent)
                                                //Do anyThing you need
                                                false
                                            }


                                        })
                                    }
                                }.start()
                            }
                        }
                    }
                }
                thread.start()
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("TAG", "")
                return true;
            }
        })
    }
}
