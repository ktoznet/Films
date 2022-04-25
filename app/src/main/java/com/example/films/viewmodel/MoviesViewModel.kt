package com.example.films.viewmodel

import com.example.films.model.repository.MoviesDBRepository
import com.example.films.model.repository.MoviesDBRepositoryImpl

class MoviesViewModel {

    private val mMoviesRepository : MoviesDBRepository = MoviesDBRepositoryImpl()

    fun getMovies(): String{
        return mMoviesRepository.getMovies()
    }
}