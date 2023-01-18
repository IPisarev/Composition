package ru.pisarev.composition.domain.repository

import ru.pisarev.composition.domain.entity.GameSettings
import ru.pisarev.composition.domain.entity.Level
import ru.pisarev.composition.domain.entity.Question

interface GameRepository {
    fun generateQuestion(
        maxSumValue: Int,
        countOfOption: Int
    ): Question
    fun getGameSettings(level: Level): GameSettings
}