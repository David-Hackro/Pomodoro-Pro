package com.david.hackro.pomodoropro

val animationTime = getSecondsInMilliSeconds(10)

const val MAX_TIME_IN_MIN = 25

const val MINUTE_TO_SECONDS = 60

fun getMinutesInMilliSeconds(min: Int) = min * 60 * 1000

fun getSecondsInMilliSeconds(sec: Int) = (sec * 1000)
