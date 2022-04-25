package com.example.films.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import api_key
import com.example.films.model.apis.ApiInterface
import com.example.films.data.MovieDetails
import com.example.films.databinding.ActivityMoviesDetailsBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesDetailsActivity : AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var releaseDate: TextView
    private lateinit var score: TextView
    private lateinit var overview: TextView
    private lateinit var banner: ImageView

    lateinit var binding: ActivityMoviesDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra("id", 0)

        Log.d("testing", "id $id")

        title = binding.moviesDetailsTittle
        releaseDate = binding.moviesDetailsDate
        score = binding.moviesDetailsScore
        overview = binding.moviesDetailsBodyOverview
        banner = binding.moviesDetailsImageBanner

        val apiInterface = id?.let { ApiInterface.create().getMovieDetails(it, api_key) }
        apiInterface.enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>?, response: Response<MovieDetails>?) {
                title.text = response?.body()?.title
                releaseDate.text = response?.body()?.release_date.toString()
                score.text = response?.body()?.vote_average.toString()
                overview.text = response?.body()?.overview

                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + response?.body()?.backdrop_path).into(banner)

            }

            override fun onFailure(call: Call<MovieDetails>?, t: Throwable?) {
                Log.d("testLogs", "onFailure  ${t?.message}")
            }


        })
    }
}