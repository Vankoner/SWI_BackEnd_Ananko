package com.example.demo.service;

import com.example.demo.entities.Game;
import com.example.demo.repository.GameRepository;
import com.example.demo.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameServiceInterface {
    private final GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public void updateGameStatus(Long id, GameStatus status) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            game.setStatus(status);
            gameRepository.save(game);
        } else {
            throw new IllegalArgumentException("Game with ID " + id + " not found");
        }
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
}
