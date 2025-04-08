package com.example.demo.service;

import com.example.demo.entities.Game;
import com.example.demo.status.GameStatus;

import java.util.List;

public interface GameServiceInterface {
    Game addGame(Game game);
    void updateGameStatus(Long id, GameStatus status);
    List<Game> getAllGames();
}
