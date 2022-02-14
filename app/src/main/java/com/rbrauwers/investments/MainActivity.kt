package com.rbrauwers.investments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var repository: TransactionsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}