package learn.codeacademy.digitalcurator_movieortv_seriesinfo

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movieactivity.*
import kotlinx.android.synthetic.main.floating_dialog.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject


class Movieactivity : AppCompatActivity() {
    var actor:String = ""
    var character:String = ""
    var jsonarrayactor = JSONArray()
    var jsonarraycharacter = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movieactivity)
        val background = ratingtxt.background
        val id = intent.extras!!.get("id")
        val title = intent.extras!!.get("title")
        val plot = intent.extras!!.get("plot")
        val length = intent.extras!!.get("length")
        val rating = intent.extras!!.get("rating")
        val imageurl = intent.extras!!.get("imageurl")

    actor = intent.extras!!.get("actor").toString()
       character = intent.extras!!.get("character").toString()
        val castlength = intent.extras!!.get("castlength")
         jsonarrayactor = JSONArray(actor)
         jsonarraycharacter = JSONArray(character)
        Log.d("TAG","length$castlength")
        for (i in 0 until castlength as Int){
            actor = jsonarrayactor.get(i).toString()
            character = jsonarraycharacter.get(i).toString()
        }
        Log.d("TAG", "actors are${actor.toString()}")

        val drawable = circlerating.getBackground() as GradientDrawable
        titlemovie.text = title.toString()
        descriptiontxt.text = plot.toString()
        lengthtxt.text = length.toString()
        ratingtxt.text = rating.toString()
        if (ratingtxt.text.toString()>"7.0"){
            ratingtxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))

            drawable.setStroke(15, ContextCompat.getColor(applicationContext, R.color.green))

        }
        if (ratingtxt.text.toString()<"7.0"){
            ratingtxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
            drawable.setStroke(15, ContextCompat.getColor(applicationContext, R.color.red))
        }
Thread{
    kotlin.run {
        val client = OkHttpClient().newBuilder()
                .build()
        val request: Request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/$id/videos?api_key=6d19f3db2501382231cb022fbd2c7207")
                .method("GET", null)
                .build()
        val response: Response = client.newCall(request).execute()
        when {
            response.isSuccessful -> {
                val data = response.body()!!.string()
                Log.d("TAG", "$data")
                val jsonObject = JSONObject(data)
                val results  = jsonObject.getJSONArray("results")
                if (results.length() == 0){
                    runOnUiThread(Runnable {
                        Toast.makeText(this, "Trailer not availaible", Toast.LENGTH_SHORT).show()
                        Picasso.get().load(imageurl.toString()).resize(410, 230).into(youtubeimg)
                    })
                } else {
                    val filter = results.getJSONObject(0)
                    val key = filter.getString("key")
                    Log.d("TAG", "$key")
                    youtubeplayer.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            val videoId = "$key"
                            youTubePlayer.loadVideo(videoId, 0f)
                            youTubePlayer.play()
                        }
                    })
                }
            }

        }
    }
}.start()
        Thread{
            kotlin.run {
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/$id?api_key=6d19f3db2501382231cb022fbd2c7207&language=en-US")
                    .method("GET", null)
                    .build()
                val response: Response = client.newCall(request).execute()
                when {
                    response.isSuccessful -> {
                        val data = response.body()!!.string()
                        val jsonObject = JSONObject(data)
                        val budget = jsonObject.getString("budget")
                        val popularity = jsonObject.getString("popularity")
                        val genres = jsonObject.getJSONArray("genres")
                        runOnUiThread(Runnable {
                       budgettxt.text = "${budget} USD"
                       popultxt.text = "$popularity %"
                       genretxt.setOnClickListener {
                           val mDialog = LayoutInflater.from(this).inflate(R.layout.floating_dialog,null)
                           val mBuilder = AlertDialog.Builder(this).setView(mDialog)
                          for (i in 0 until genres.length()){
                              val dialogjson = genres.getJSONObject(i)
                              val name = dialogjson.getString("name")
                              Log.d("TAG","$name")
                              mDialog.textViewdialog.append("\n" + "$name")
                          }
                           mBuilder.show()

                       }

                        })
                    }
                }

            }
        }.start()


    }
}
