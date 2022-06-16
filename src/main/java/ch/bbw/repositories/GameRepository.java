package ch.bbw.repositories;

import ch.bbw.models.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends CrudRepository<Game, Integer> {

    @Query("SELECT i from Game i where i.id = :id")
    Iterable<Game> findByGameId(@Param("id") int id);

    @Query("SELECT i from Game i where i.winner.id = :winner_id")
    Iterable<Game> findByWinnerId(@Param("winner_id") int winner_id);

    @Query("SELECT i from Game i where i.series.id = :series_id")
    Iterable<Game> findBySeriesId(@Param("series_id") int series_id);

    @Query("SELECT i from Game i " +
            "JOIN i.series t " +
            "WHERE i.number = :gameNumber and t.id = :seriesId")
    Iterable<Game> findByGameNumberAndSeriesId(@Param("gameNumber") int gameNumber, @Param("seriesId") int seriesId);

    @Query("select g from Game g" +
            " join Series s" +
            " join Tournament to" +
            " where to.id = :tournamentId")
    Iterable<Game> findByTournamentId(@Param("tournamentId") int tournamentId);
}
