package com.goodrequest.hiring.cats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.goodrequest.hiring.databinding.ActivityCatsBinding
import com.goodrequest.hiring.CatsApi
import com.goodrequest.hiring.Result
import com.goodrequest.hiring.viewModel

class CatsActivity: AppCompatActivity() {

    private val vm by viewModel { CatsModel(it, null, CatsApi) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screen = ActivityCatsBinding.inflate(layoutInflater)
        setContentView(screen.root)

        screen.refresh.setOnRefreshListener { vm.loadCats() }
        screen.retry.setOnClickListener { vm.loadCats() }

        vm.loadCats()

        vm.cats.observe(this) { cats: Result<List<Cat>>? ->
            when(cats) {
                is Result.Failure -> {
                    screen.loading.visibility = GONE
                    screen.failure.visibility = VISIBLE
                }
                is Result.Success -> {
                    screen.loading.visibility = GONE
                    val adapter = CatsAdapter()
                    screen.catsList.adapter = adapter
                    adapter.show(cats.value)
                }
                null -> {}
            }
        }
    }
}

