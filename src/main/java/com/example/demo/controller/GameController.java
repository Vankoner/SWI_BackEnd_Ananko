package com.example.demo.controller;

import com.example.demo.entities.Game;
import com.example.demo.status.GameStatus;
import com.example.demo.interfaces.GameServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Game API", description = "API for managing games in the Game Tracker application")
@RestController
@RequestMapping("api/games")
public class GameController {
    private final GameServiceInterface gameManager;

    @Autowired
    public GameController(GameServiceInterface gameManager) {
        this.gameManager = gameManager;
    }

    // REST API методы
    @Operation(summary = "Add a new game", description = "Adds a new game to the user's library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully added"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid game data")
    })
    @PostMapping
    @ResponseBody
    public ResponseEntity<Game> addGameApi(@RequestBody Game game) {
        Game savedGame = gameManager.addGame(game);
        return ResponseEntity.ok(savedGame);
    }

    @Operation(summary = "Update game status", description = "Updates the status of a game (e.g., PLAYING, COMPLETED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @PutMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Void> updateGameStatusApi(@PathVariable Long id, @RequestBody GameStatus status) {
        gameManager.updateGameStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all games", description = "Retrieves all games for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of games retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Game>> getAllGamesApi() {
        List<Game> games = gameManager.getAllGames();
        return ResponseEntity.ok(games);
    }

    // Методы для веб-страниц (не отображаются в Swagger, так как возвращают HTML)
    @GetMapping("/list")
    public String getAllGames(Model model) {
        List<Game> games = gameManager.getAllGames();
        model.addAttribute("games", games);
        return "games";
    }

    @GetMapping("/add")
    public String showAddGameForm(Model model) {
        model.addAttribute("game", new Game());
        model.addAttribute("statuses", GameStatus.values());
        return "add-game";
    }

    @PostMapping("/add")
    public String addGame(@ModelAttribute Game game) {
        gameManager.addGame(game);
        return "redirect:/games/list";
    }

    @GetMapping("/update-status")
    public String updateGameStatus(@RequestParam Long id, @RequestParam GameStatus status) {
        gameManager.updateGameStatus(id, status);
        return "redirect:/games/list";
    }
}