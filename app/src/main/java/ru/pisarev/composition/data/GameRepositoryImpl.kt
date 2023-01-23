package ru.pisarev.composition.data

import ru.pisarev.composition.domain.entity.GameSettings
import ru.pisarev.composition.domain.entity.Level
import ru.pisarev.composition.domain.entity.Question
import ru.pisarev.composition.domain.repository.GameRepository
import kotlin.random.Random

object GameRepositoryImpl: GameRepository {
    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOption: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE,maxSumValue)
        val vivsibleNumber = Random.nextInt(MIN_ANSWER_VALUE,sum)
        val rightAnswer = sum-vivsibleNumber
        val options = HashSet<Int>()
        options.add(rightAnswer)

        while (options.size<countOfOption){
            options.add(Random.nextInt(MIN_ANSWER_VALUE,sum))
        }

        return Question(sum,vivsibleNumber,options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
       return when(level) {
           Level.TEST -> {
               GameSettings(
                   10,3,50,10
               )
           }
           Level.EASY ->{
               GameSettings(10,10,50,60)
           }
           Level.NORMAL -> {
               GameSettings(20,20,80,60)
           }
           Level.HARD -> {
               GameSettings(30,30,90,60)
           }
       }
    }
}