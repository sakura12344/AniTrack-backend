package com.anitrack.anitrack.repository;

import com.anitrack.anitrack.entity.UserAnime;
import com.anitrack.anitrack.entity.WatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAnimeRepository extends JpaRepository<UserAnime, UUID> {
    Optional<UserAnime> findByUser_IdAndAnime_Id(Long user_Id, UUID anime_Id);

    boolean existsByUser_IdAndAnime_Id(Long userId, UUID animeId);

    List<UserAnime> findByUser_Id(Long userId);

    List<UserAnime> findByUser_IdAndIsFavorite(Long userId, Boolean isFavorite);

    List<UserAnime> findByUser_IdAndWatchStatus(Long userId, WatchStatus watchStatus);

    List<UserAnime> findByUser_IdOrderByLastWatchedAtDesc(Long userId);
    

}
