package ru.pisarev.composition.domain.entity

data class GameResult (
    val winner: Boolean,
    val countOfrightAnswer: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
    )