package com.trdz.tasks

interface Leader {
	fun getNavigation():Navigation
	fun getExecutor():Executor
}