package com.example.films.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import api_key
import com.example.films.*
import com.example.films.data.Movies
import com.example.films.databinding.ActivityMoviesBinding
import com.example.films.model.apis.ApiInterface
import com.example.films.view.adapters.CustomAdapter
import com.example.films.viewmodel.MoviesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivity : AppCompatActivity() {
    private lateinit var  mViewModel: MoviesViewModel
    lateinit var binding: ActivityMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // getting the recyclerview by its id
        val recyclerview = binding.recyclerview

        // this creates a vertical layout Manager
        recyclerview.layoutManager = GridLayoutManager(this,2)

        // ArrayList of class ItemsViewModel

        mViewModel = MoviesViewModel()
        val result = mViewModel.getMovies()
        // This loop will create 20 Views containing
        // the image with the count of view
//        for (i in 1..20) {
//            data.add(
//                ItemsViewModel(
//                    R.drawable.common_full_open_on_phone,
//                    "Item " + i
//                )
//            )
//        }



        val apiInterface = ApiInterface.create().getMovies(api_key)

        //apiInterface.enqueue( Callback<List<Movie>>())
        apiInterface.enqueue(object : Callback<Movies>, CustomAdapter.ItemClicklistener {
            override fun onResponse(call: Call<Movies>?, response: Response<Movies>?) {
                Log.d("testLogs","OnResponse Success ${response?.body()?.results}")
                // This will pass the ArrayList to our Adapter
                val adapter = CustomAdapter(response?.body()?.results, this)

                // Setting the Adapter with the recyclerview
                recyclerview.adapter = adapter


            }

            override fun onFailure(call: Call<Movies>?, t: Throwable?) {
                Log.d("testLogs","onFailure  ${t?.message}")
            }

            override fun onItemClick(id: Int) {
               val intent = Intent(this@MoviesActivity, MoviesDetailsActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}