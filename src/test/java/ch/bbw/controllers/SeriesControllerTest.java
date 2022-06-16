package ch.bbw.controllers;

import ch.bbw.repositories.SeriesRepository;
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

@WebMvcTest(controllers = SeriesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeriesRepository seriesRepository;


    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @MockBean
    private UserDetailsServiceImpl userDetailsService;      // Wird vom SpringBoot Testing Framework benötigt

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;    // Wird vom SpringBoot Testing Framework benötigt


    @Test
    public void checkGet_whenValidIdSearched_thenAccordingSeriesReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);
        doReturn(Optional.of(series)).when(seriesRepository).findById(1);

        mockMvc.perform(get("/series/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    public void checkGet_whenTournamentIdSearched_thenAllAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);

        doReturn(Collections.singletonList(series)).when(seriesRepository).findAll();

        mockMvc.perform(get("/series")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }



    @Test
    public void checkGet_whenTournamentIdSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByTournamentId(1);

        mockMvc.perform(get("/series?tournament=1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
    @Test
    public void checkGet_whenTournamentWinnerSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);
        series.getWinner().setId(2345);

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByWinner(2345);

        mockMvc.perform(get("/series?winner=2345")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].winner.id").value(2345));
    }

    @Test
    public void checkGet_whenTeamIdSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);
        series.getTeamOne().setId(2);

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByTeamId(2);

        mockMvc.perform(get("/series?teamId=2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamOne.id").value(2));
    }

    @Test
    public void checkGet_whenTeamNameSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);
        series.getTeamOne().setName("Test");

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByTeamName("Test");

        mockMvc.perform(get("/series?teamName=Test")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamOne.name").value("Test"));
    }

    @Test
    public void checkGet_whenPlayerIdSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);
        series.getTeamOne().getLinkedPlayers().add(TestDataUtil.makePlayer(2));

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByPlayerId(2);

        mockMvc.perform(get("/series?playerId=2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void checkGet_whenGameIdSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(01134);
        series.getLinkedGames().add(TestDataUtil.makeGame(2, series));

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByGameId(2);

        mockMvc.perform(get("/series?game=2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(01134));
    }

    @Test
    public void checkGet_whenPlayerNameSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(01134);
        series.getTeamOne().getLinkedPlayers().add(TestDataUtil.makePlayer(2));

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByPlayerName("name");

        mockMvc.perform(get("/series?playerName=name")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(01134));
    }

    @Test
    public void checkGet_whenPlayerGamerTagSearched_thenSeriesAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(01134);
        series.getTeamOne().getLinkedPlayers().add(TestDataUtil.makePlayer(2));

        doReturn(Collections.singletonList(series)).when(seriesRepository).findByPlayerGamerTag("gamertag");

        mockMvc.perform(get("/series?playerGamerTag=gamertag")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(01134));
    }

    @Test
    public void checkPost_whenValidSeries_thenIsOk() throws Exception {
        Team teamOne = TestDataUtil.makeTeamWithValidPlayers();
        Team teamTwo = TestDataUtil.makeTeamWithValidPlayers();
        teamOne.setId(1);
        teamTwo.setId(2);

        mockMvc.perform(post("/series")
                        .contentType("application/json")
                        .content("{\"tournament\":" + gson.toJson(TestDataUtil.makeTournament(1)) + ", \"winner\":" + gson.toJson(teamOne) + ", \"teamOne\":" + gson.toJson(teamOne) + ", \"teamTwo\":" + gson.toJson(teamTwo) + "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkPut_whenValidSeries_thenIsOk() throws Exception {
        Team teamOne = TestDataUtil.makeTeamWithValidPlayers();
        Team teamTwo = TestDataUtil.makeTeamWithValidPlayers();
        teamOne.setId(1);
        teamTwo.setId(2);

        mockMvc.perform(put("/series")
                        .contentType("application/json")
                        .content("{\"tournament\":" + gson.toJson(TestDataUtil.makeTournament(1)) + ", \"winner\":" + gson.toJson(teamOne) + ", \"teamOne\":" + gson.toJson(teamOne) + ", \"teamTwo\":" + gson.toJson(teamTwo) + "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/series/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(seriesRepository).deleteById(eq(1));
    }

    //********************************* Negative Tests *********************************

    @Test
    public void checkGet_whenIdSearched_thenExceptionIsThrown() throws Exception {

        doThrow(new EntityNotFoundException()).when(seriesRepository).findById(1);

        mockMvc.perform(get("/series/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenInValidSeries_thenIsConflict() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(seriesRepository).save(series);

        mockMvc.perform(post("/series")
                        .contentType("application/json")
                        .content(gson.toJson(series)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkPut_whenInValidSeries_thenIsConflict() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(seriesRepository).save(series);

        mockMvc.perform(put("/series")
                        .contentType("application/json")
                        .content(gson.toJson(series)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkDelete_whenInvalidId_thenIsNotFound() throws Exception {

        doThrow(new EmptyResultDataAccessException("need one element", 1)).when(seriesRepository).deleteById(2);

        mockMvc.perform(delete("/series/2")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}
