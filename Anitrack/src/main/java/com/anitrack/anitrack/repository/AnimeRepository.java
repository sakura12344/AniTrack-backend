package com.anitrack.anitrack.repository;

import com.anitrack.anitrack.entity.Anime;
import com.anitrack.anitrack.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimeRepository extends JpaRepository<Anime, UUID> {
    Optional<Anime> findByTitle(String title);

    List<Anime> findByTitleContainingIgnoreCase(String title);

    List<Anime> findByOriginalTitleContainingIgnoreCase(String title);

    List<Anime> findByTitleContainingIgnoreCaseOrOriginalTitleContainingIgnoreCase(String title, String originalTitle);

    @Query(value = """
            SELECT *
            FROM anime
            WHERE JSON_CONTAINS(aliases,JSON_QUOTE(:alias))
            """,
            nativeQuery = true
    )
    List<Anime> fidByAlias(@Param("alias") String alias);

    List<Anime> findByStatus(String status);

    List<Anime> findByType(String type);

    List<Anime> findByReleaseYear(Integer releaseYear);

    List<Anime> findByIsAdult(Boolean isAdult);

    List<Anime> findByIsOfficial(Boolean isOfficial);

    List<Anime> findByExternalScoreGreaterThanEqual(BigDecimal score);

    @Query("""
            SELECT u
            FROM User u
            WHERE (:username IS NULL OR u.username LIKE %:username%)
            AND (:email IS NULL OR u.email LIKE %:email)
            """)
    List<User> searchByUsernameAndEmail(@Param("username") String username,
                                        @Param("email") String email);

    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g.id = :genreId")
    List<Anime> findByGenreId(@Param("genreId") UUID genreId);

    @Query("SELECT count(a) FROM Anime a JOIN a.genres g WHERE g.id=:genreId ")
    Long countAnimeByGenreId(@Param("genreId") UUID genreId);

    Optional<Anime> findByOriginalTitle(String originalTitle);

    List<Anime> findBySeason(String season);

    List<Anime> findByReleaseYearAndSeason(Integer releaseYear, String season);

    boolean existsByTitle(String title);

    boolean existsByOriginalTitle(String originalTitle);

    @Query("SELECT a FROM Anime a WHERE a.externalScore BETWEEN :min AND :max ORDER BY a.externalScore DESC")
    List<Anime> findByExternalScoreBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Query("SELECT a FROM Anime a ORDER BY a.externalScore DESC")
    List<Anime> findTopRated(Pageable pageable);

    @Query("SELECT a.releaseYear,COUNT(a) FROM Anime a GROUP BY a.releaseYear")
    List<Object[]> countByReleaseYearGrouped();

    @Query("SELECT a.status,COUNT(a) FROM Anime a GROUP BY a.status")
    List<Object[]> countByStatusGrouped();

    @Query("SELECT a.season, COUNT(a) FROM Anime a GROUP BY a.season")
    List<Object[]> countBySeasonGrouped();

    @Query("SELECT a.releaseYear, a.season, COUNT(a) FROM Anime a GROUP BY a.releaseYear, a.season ORDER BY a.releaseYear")
    List<Object[]> countByYearAndSeasonGrouped();

    @Query("SELECT a.type, COUNT(a) FROM Anime a GROUP BY a.type")
    List<Object[]> countByTypeGrouped();

    @Modifying
    @Query("DELETE FROM Anime a WHERE a.id IN :ids")
    void deleteByIdIn(@Param("ids") List<UUID> ids);

    @Query(value = """
                    SELECT * FROM anime
                    WHERE (:title is NULL OR title LIKE CONCAT('%',title,'%') )
                    AND (:type is NULL OR anime_type=:type )
                    AND (:year is NULL OR release_year=:year )
                    AND (:season is NULL OR season=:season)
                    AND (:status is NULL OR anime_status=:status)
                    AND (:minScore IS NULL OR external_score>= :minScore)
                    AND (:maxScore IS NULL OR external_score<= :maxScore)
                    AND (:isAdult IS NULL OR is_adult=:isAdult) 
                    AND (:isOfficial IS NULL OR is_official=:isOfficial )         
                    ORDER BY
                         CASE
                         WHEN :sortBy = 'score' THEN external_score
                             WHEN :sortBy = 'year' THEN release_year
                             ELSE title
                         END DESC
            """, nativeQuery = true)
    List<Anime> searchCombined(@Param("title") String title,
                               @Param("type") String type,
                               @Param("year") Integer year,
                               @Param("season") String season,
                               @Param("status") String status,
                               @Param("minScore") BigDecimal minScore,
                               @Param("maxScore") BigDecimal maxScore,
                               @Param("isAdult") Boolean isAdult,
                               @Param("isOfficial") Boolean isOfficial,
                               @Param("sortBy") String sortBy
    );


}