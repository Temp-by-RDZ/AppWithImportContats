package com.trdz.tasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), Leader {

	private val navigation = Navigation(R.id.container_fragment_base)
	private val executor = Executor()
	override fun getNavigation() = navigation
	override fun getExecutor() = executor

	override fun onDestroy() {
		executor.stop()
		super.onDestroy()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		if (savedInstanceState == null) navigation.add(supportFragmentManager, WindowStart.newInstance(), false)
	}

}