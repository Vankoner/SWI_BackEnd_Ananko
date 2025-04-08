package com.example.demo.service;

import com.example.demo.entities.Game;
import com.example.demo.interfaces.StatisticsServiceInterface;
import com.example.demo.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService implements StatisticsServiceInterface {
    private final GameRepository gameRepository;

    @Autowired
    public StatisticsService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Double getTotalHoursPlayed() {
        List<Game> games = gameRepository.findAll();
        return games.stream()
                .mapToDouble(Game::getHoursPlayed)
                .sum();
    }

    @Override
    public String getMostPlayedGenre() {
        List<Game> games = gameRepository.findAll();
        if (games.isEmpty()) {
            return null;
        }

        Map<String, Double> genreHours = games.stream()
                .collect(Collectors.groupingBy(
                        Game::getGenre,
                        Collectors.summingDouble(Game::getHoursPlayed)
                ));

        return genreHours.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
