package com.example.demo.interfaces;

import com.example.demo.entities.Achievement;

import java.util.List;

public interface AchievementServiceInterface {
    Achievement addAchievement(Achievement achievement);
    List<Achievement> getAchievementsByGame(Long gameId);
}