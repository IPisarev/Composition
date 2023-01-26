package ru.pisarev.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult (
    val winner: Boolean,
    val countOfrightAnswer: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
    ): Parcelable