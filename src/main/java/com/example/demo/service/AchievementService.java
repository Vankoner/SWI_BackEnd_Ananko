package com.example.demo.service;

import com.example.demo.entities.Achievement;
import com.example.demo.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementServiceInterface {
    private final AchievementRepository achievementRepository;

    @Autowired
    public AchievementServiceImpl(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    @Override
    public Achievement addAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    @Override
    public List<Achievement> getAchievementsByGame(Long gameId) {
        return achievementRepository.findByGameId(gameId);
    }
}
