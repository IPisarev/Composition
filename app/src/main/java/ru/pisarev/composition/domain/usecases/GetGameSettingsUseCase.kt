package ru.pisarev.composition.domain.usecases

import ru.pisarev.composition.domain.entity.GameSettings
import ru.pisarev.composition.domain.entity.Level
import ru.pisarev.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings{
        return repository.getGameSettings(level)
    }
}