package com.trdz.tasks

import android.content.Context
import android.widget.Toast

class Executor() {

	private var toast: Toast? = null

	fun stop() {
		toast?.cancel()
	}

	fun showToast(context: Context, text: String?, length: Int) {
		toast?.cancel()
		toast = Toast.makeText(context, text, length)
		toast?.show()
	}

}