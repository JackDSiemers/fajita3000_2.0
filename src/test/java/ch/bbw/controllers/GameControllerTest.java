package ch.bbw.controllers;

import ch.bbw.models.Game;
import ch.bbw.repositories.GameRepository;
import ch.bbw.security.UserDetailsServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import utils.TestDataUtil;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;      // Wird vom SpringBoot Testing Framework benötigt

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;    // Wird vom SpringBoot Testing Framework benötigt


    @Test
    public void checkGet_whenValidId_thenAccordingGameReturned() throws Exception {
        Game game = new Game();
        game.setWinner(TestDataUtil.makeTeamWithValidPlayers());
        game.setSeries(TestDataUtil.makeSeriesWithValidTeams());
        game.setNumber(1);
        game.setId(1);
        doReturn(Optional.of(game)).when(gameRepository).findById(1);

        mockMvc.perform(get("/games/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(game.getNumber()));

    }


    @Test
    public void checkGet_whenWinnerIdSearched_thenGamesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.getWinner().setId(2);
        Game game = TestDataUtil.makeGame(1, series);

        doReturn(Collections.singletonList(game)).when(gameRepository).findByWinnerId(2);

        mockMvc.perform(get("/games?winner=2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(1));
    }

    @Test
    public void checkGet_whenGameNumberAndSeriesIdSearched_thenGamesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(2);
        Game game = TestDataUtil.makeGame(1, series);

        doReturn(Collections.singletonList(game)).when(gameRepository).findByGameNumberAndSeriesId(1,2);

        mockMvc.perform(get("/games?game=1&series=2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(1));
    }

    @Test
    public void checkGet_whenSeriesIdSearched_thenGamesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(2);
        Game game = TestDataUtil.makeGame(1, series);

        doReturn(Collections.singletonList(game)).when(gameRepository).findBySeriesId(2);

        mockMvc.perform(get("/games?series=2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(1));
    }

    @Test
    public void checkGet_whenTournamentIdSearched_thenGamesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        Tournament tournament = TestDataUtil.makeTournament(2);
        series.setId(1);
        series.setTournament(tournament);
        Game game = TestDataUtil.makeGame(1, series);

        doReturn(Collections.singletonList(game)).when(gameRepository).findByTournamentId(2);

        mockMvc.perform(get("/games?tournament=2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(1));
    }

    @Test
    public void checkPost_whenValidGame_thenIsOk() throws Exception {

        mockMvc.perform(post("/games")
                        .contentType("application/json")
                        .content("{\"number\":1, \"series\":" + gson.toJson(TestDataUtil.makeSeriesWithValidTeams()) + ", \"winner\":" + gson.toJson(TestDataUtil.makeTeamWithValidPlayers()) + "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkPut_whenValidGame_thenIsOk() throws Exception {

        mockMvc.perform(put("/games")
                        .contentType("application/json")
                        .content("{\"number\":1, \"series\":" + gson.toJson(TestDataUtil.makeSeriesWithValidTeams()) + ", \"winner\":" + gson.toJson(TestDataUtil.makeTeamWithValidPlayers()) + "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/games/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(gameRepository).deleteById(eq(1));
    }

    //********************************* Negative Tests *********************************

    @Test
    public void checkGet_whenIdSearched_thenExceptionIsThrown() throws Exception {

        doThrow(new EntityNotFoundException()).when(gameRepository).findById(1);

        mockMvc.perform(get("/games/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenInValidGame_thenIsConflict() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        Game game = TestDataUtil.makeGame(1, series);

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(gameRepository).save(game);

        mockMvc.perform(post("/games")
                        .contentType("application/json")
                        .content(gson.toJson(game)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkPut_whenInValidGame_thenIsConflict() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        Game game = TestDataUtil.makeGame(1, series);

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(gameRepository).save(game);

        mockMvc.perform(put("/games")
                        .contentType("application/json")
                        .content(gson.toJson(game)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkDelete_whenInvalidId_thenIsNotFound() throws Exception {

        doThrow(new EmptyResultDataAccessException("need one element", 1)).when(gameRepository).deleteById(2);

        mockMvc.perform(delete("/games/2")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

}
