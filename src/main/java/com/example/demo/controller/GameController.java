package com.example.demo.controller;

import com.example.demo.dto.GameDTO;
import com.example.demo.entities.Game;
import com.example.demo.entities.Users;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.status.GameStatus;
import com.example.demo.interfaces.GameServiceInterface;
import com.example.demo.utils.MapperUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Game API", description = "API for managing games in the Game Tracker application")
@RestController
@RequestMapping("api/games")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameServiceInterface gameManager;

    @Autowired
    public GameController(GameServiceInterface gameManager) {
        this.gameManager = gameManager;
    }

    private Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((CustomUserDetails) principal).getUsers();
        }
        throw new IllegalStateException("User not authenticated");
    }

    @Operation(summary = "Add a new game", description = "Adds a new game to the authenticated user's library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid game data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    @ResponseBody
    public ResponseEntity<GameDTO> addGameApi(@Valid @RequestBody GameDTO gameDTO) {
        logger.info("Received request to add game: {}", gameDTO.getTitle());
        try {
            Users currentUser = getCurrentUser();
            Game game = MapperUtil.toGame(gameDTO, currentUser);
            Game savedGame = gameManager.addGame(game);
            GameDTO responseDTO = MapperUtil.toGameDTO(savedGame);
            logger.info("Game successfully added: {}", savedGame.getTitle());
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error for game data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Unexpected error while adding game: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Update game status", description = "Updates the status of a game (e.g., PLAYING, COMPLETED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @PutMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Void> updateGameStatusApi(@PathVariable Long id, @Valid @RequestBody GameStatus status) {
        logger.info("Updating status for game ID: {} to {}", id, status);
        try {
            Users currentUser = getCurrentUser();
            Game game = gameManager.getAllGames().stream()
                    .filter(g -> g.getId().equals(id) && g.getUser().getId().equals(currentUser.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Game not found or not owned by user"));
            gameManager.updateGameStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Game with ID {} not found or not owned by user: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error while updating game status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all games for the authenticated user", description = "Retrieves all games for the authenticated user with optional pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of games retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<GameDTO>> getAllGamesApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Request to retrieve games list, page: {}, size: {}", page, size);
        try {
            Users currentUser = getCurrentUser();
            Pageable pageable = PageRequest.of(page, size);
            Page<Game> gamesPage = gameManager.getGamesByUser(currentUser.getId(), pageable);
            List<GameDTO> gameDTOs = MapperUtil.toGameDTOList(gamesPage.getContent());
            return ResponseEntity.ok(gameDTOs);
        } catch (Exception e) {
            logger.error("Error while retrieving games list: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @Hidden
    @GetMapping("/list")
    public String getAllGames(Model model) {
        logger.info("Request to display games list page");
        try {
            Users currentUser = getCurrentUser();
            List<Game> games = gameManager.getGamesByUser(currentUser.getId());
            model.addAttribute("games", games);
            return "games";
        } catch (Exception e) {
            logger.error("Error while loading games list: {}", e.getMessage());
            model.addAttribute("error", "Failed to load games list");
            return "games";
        }
    }

    @Hidden
    @GetMapping("/add")
    public String showAddGameForm(Model model) {
        logger.info("Displaying add game form");
        model.addAttribute("game", new Game());
        model.addAttribute("statuses", GameStatus.values());
        return "add-game";
    }

    @Hidden
    @PostMapping("/add")
    public String addGame(@Valid @ModelAttribute Game game, BindingResult result, Model model) {
        logger.info("Attempting to add game: {}", game.getTitle());
        if (result.hasErrors()) {
            logger.warn("Validation errors in form: {}", result.getAllErrors());
            model.addAttribute("statuses", GameStatus.values());
            return "add-game";
        }
        try {
            Users currentUser = getCurrentUser();
            game.setUser(currentUser);
            gameManager.addGame(game);
            return "redirect:/games/list";
        } catch (Exception e) {
            logger.error("Error while adding game: {}", e.getMessage());
            model.addAttribute("error", "Error adding game: " + e.getMessage());
            model.addAttribute("statuses", GameStatus.values());
            return "add-game";
        }
    }

    @Hidden
    @GetMapping("/update-status")
    public String updateGameStatus(@RequestParam Long id, @RequestParam String status, Model model) {
        logger.info("Attempting to update status for game ID: {} to {}", id, status);
        try {
            Users currentUser = getCurrentUser();
            Game game = gameManager.getGamesByUser(currentUser.getId()).stream()
                    .filter(g -> g.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Game not found or not owned by user"));
            GameStatus gameStatus = GameStatus.valueOf(status);
            gameManager.updateGameStatus(id, gameStatus);
            return "redirect:/games/list";
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status or game not found: {}", e.getMessage());
            model.addAttribute("error", "Invalid status or game not found: " + e.getMessage());
            Users currentUser = getCurrentUser();
            List<Game> games = gameManager.getGamesByUser(currentUser.getId());
            model.addAttribute("games", games);
            return "games";
        } catch (Exception e) {
            logger.error("Error while updating game status: {}", e.getMessage());
            model.addAttribute("error", "Error updating status: " + e.getMessage());
            Users currentUser = getCurrentUser();
            List<Game> games = gameManager.getGamesByUser(currentUser.getId());
            model.addAttribute("games", games);
            return "games";
        }
    }
}
