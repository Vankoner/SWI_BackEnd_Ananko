package com.example.demo.interfaces;

import com.example.demo.entities.Game;
import com.example.demo.status.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GameServiceInterface {
    Game addGame(Game game);
    void updateGameStatus(Long id, GameStatus status);
    List<Game> getAllGames();
    Page<Game> getAllGames(Pageable pageable);
    List<Game> getGamesByUser(Long userId);
    Page<Game> getGamesByUser(Long userId, Pageable pageable);
}