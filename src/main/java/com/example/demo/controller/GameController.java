package com.example.demo.Controller;

import com.example.demo.entities.Game;
import com.example.demo.interfaces.GameServiceInterface;
import com.example.demo.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameServiceInterface gameService;

    @Autowired
    public GameController(GameServiceInterface gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody Game game) {
        Game savedGame = gameService.addGame(game);
        return ResponseEntity.ok(savedGame);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateGameStatus(@PathVariable Long id, @RequestBody GameStatus status) {
        gameService.updateGameStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }
}
