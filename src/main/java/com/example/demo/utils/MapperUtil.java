package com.example.demo.utils;

import com.example.demo.dto.AchievementDTO;
import com.example.demo.dto.GameDTO;
import com.example.demo.entities.Achievement;
import com.example.demo.entities.Game;
import com.example.demo.entities.Users;
import com.example.demo.status.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtil {

    // Convert GameDTO to Game entity
    public static Game toGame(GameDTO gameDTO, Users users) {
        if (gameDTO == null) return null;

        Game game = new Game();
        game.setTitle(gameDTO.getTitle());
        game.setStatus(gameDTO.getStatus() != null ? GameStatus.valueOf(gameDTO.getStatus()) : GameStatus.PLANNED);
        game.setRating(gameDTO.getRating());
        game.setComment(gameDTO.getComment());
        game.setHoursPlayed(gameDTO.getHoursPlayed());
        game.setGenre(gameDTO.getGenre());
        game.setUser(users);

        // Convert list of AchievementDTO to list of Achievement
        List<Achievement> achievements = new ArrayList<>();
        if (gameDTO.getAchievements() != null) {
            for (AchievementDTO achDTO : gameDTO.getAchievements()) {
                Achievement achievement = new Achievement();
                achievement.setTitle(achDTO.getTitle());
                achievement.setDescription(achDTO.getDescription());
                achievement.setStatus(achDTO.getStatus());
                achievement.setGame(game);
                achievement.setUser(users);
                achievements.add(achievement);
            }
        }
        game.setAchievements(achievements);

        return game;
    }

    // Convert Game entity to GameDTO
    public static GameDTO toGameDTO(Game game) {
        if (game == null) return null;

        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setTitle(game.getTitle());
        gameDTO.setStatus(game.getStatus() != null ? game.getStatus().name() : null);
        gameDTO.setRating(game.getRating());
        gameDTO.setComment(game.getComment());
        gameDTO.setHoursPlayed(game.getHoursPlayed());
        gameDTO.setGenre(game.getGenre());
        gameDTO.setUserId(game.getUser() != null ? game.getUser().getId() : null);

        // Convert list of Achievement to list of AchievementDTO
        List<AchievementDTO> achievementDTOs = new ArrayList<>();
        if (game.getAchievements() != null) {
            achievementDTOs = game.getAchievements().stream().map(MapperUtil::toAchievementDTO).collect(Collectors.toList());
        }
        gameDTO.setAchievements(achievementDTOs);

        return gameDTO;
    }

    // Convert Achievement entity to AchievementDTO
    public static AchievementDTO toAchievementDTO(Achievement achievement) {
        if (achievement == null) return null;

        AchievementDTO achDTO = new AchievementDTO();
        achDTO.setId(achievement.getId());
        achDTO.setTitle(achievement.getTitle());
        achDTO.setDescription(achievement.getDescription());
        achDTO.setStatus(achievement.getStatus());
        achDTO.setGame(achievement.getGame() != null ? achievement.getGame().getTitle() : null);
        achDTO.setUserId(achievement.getUser() != null ? achievement.getUser().getId() : null);

        return achDTO;
    }

    // Convert List<Game> to List<GameDTO>
    public static List<GameDTO> toGameDTOList(List<Game> games) {
        if (games == null) return new ArrayList<>();
        return games.stream().map(MapperUtil::toGameDTO).collect(Collectors.toList());
    }
}