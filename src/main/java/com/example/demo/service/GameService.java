package com.example.demo.service;

import com.example.demo.entities.Game;
import com.example.demo.interfaces.GameServiceInterface;
import com.example.demo.status.GameStatus;
import com.example.demo.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService implements GameServiceInterface {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public void updateGameStatus(Long id, GameStatus status) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + id));
        game.setStatus(status);
        gameRepository.save(game);
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Page<Game> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public List<Game> getGamesByUser(Long userId) {
        return List.of();
    }

    @Override
    public Page<Game> getGamesByUser(Long userId, Pageable pageable) {
        return null;
    }
}