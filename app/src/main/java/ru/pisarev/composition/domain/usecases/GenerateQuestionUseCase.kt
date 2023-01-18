package ru.pisarev.composition.domain.usecases

import android.media.session.MediaSession.QueueItem
import ru.pisarev.composition.domain.entity.Question
import ru.pisarev.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTION)
    }

    private companion object {
        private const val COUNT_OF_OPTION = 6
    }
}