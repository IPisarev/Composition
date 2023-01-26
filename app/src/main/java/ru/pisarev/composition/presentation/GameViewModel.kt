package ru.pisarev.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.pisarev.composition.R
import ru.pisarev.composition.data.GameRepositoryImpl
import ru.pisarev.composition.domain.entity.GameResult
import ru.pisarev.composition.domain.entity.GameSettings
import ru.pisarev.composition.domain.entity.Level
import ru.pisarev.composition.domain.entity.Question
import ru.pisarev.composition.domain.repository.GameRepository
import ru.pisarev.composition.domain.usecases.GenerateQuestionUseCase
import ru.pisarev.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    private val repository: GameRepository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings

    private var timer: CountDownTimer? = null

    private var countRightAnswers = 0
    private var countQuestions = 0

    private var _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private var _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var _percentRightAnswer = MutableLiveData<Int>()
    val percentRightAnswer: LiveData<Int>
        get() = _percentRightAnswer

    private var _progressAnswer = MutableLiveData<String>()
    val progressAnswer: LiveData<String>
        get() = _progressAnswer

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    fun startGame(level: Level) {
        getGameSettings(level)
        generateQuestion()
        startTimer()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updatePercent()
        generateQuestion()
    }

    private fun checkAnswer(number: Int) {
        val righAnswer = question.value?.righrAnswer
        if (righAnswer == number) {
            countRightAnswers++
        }
        countQuestions++
    }

    private fun updatePercent() {
        val percent = calculatePercentRightAnswers()
        _percentRightAnswer.value = percent
        _progressAnswer.value = String.format(
            context.getString(R.string.progress_answers),
            countRightAnswers,
            gameSettings.minCountOfRightAnswer
        )
        _enoughCount.value = countRightAnswers >= gameSettings.minCountOfRightAnswer
        _enoughPercent.value = percent >= gameSettings.minCountOfRightAnswer
    }

    private fun calculatePercentRightAnswers(): Int {
        return ((countRightAnswers * 100) / countQuestions.toDouble()).toInt()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswer
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            MILES_IN_SECOND * gameSettings.gameTimeInSecond,
            MILES_IN_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                stopGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILES_IN_SECOND
        val minutes = seconds / SECOND_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECOND_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun stopGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countRightAnswers,
            countQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILES_IN_SECOND = 1000L
        private const val SECOND_IN_MINUTES = 60
    }
}