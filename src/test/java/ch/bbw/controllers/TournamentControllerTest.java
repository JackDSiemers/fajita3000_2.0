package ch.bbw.controllers;

import ch.bbw.repositories.TournamentRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TournamentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentRepository tournamentRepository;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @MockBean
    private UserDetailsServiceImpl userDetailsService;      // Wird vom SpringBoot Testing Framework benötigt

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;    // Wird vom SpringBoot Testing Framework benötigt


    @Test
    public void checkGet_whenValidId_thenAccordingTournamentReturned() throws Exception {
        Tournament tournament = TestDataUtil.makeTournament(1);
        doReturn(Optional.of(tournament)).when(tournamentRepository).findById(1);

        mockMvc.perform(get("/tournaments/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("fall major"));

    }

    @Test
    public void checkGet_whenNoParameter_thenAllAreReturned() throws Exception {
        Tournament tournament = TestDataUtil.makeTournament(1);
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);
        series.setTournament(tournament);


        doReturn(Collections.singletonList(tournament)).when(tournamentRepository).findAll();

        mockMvc.perform(get("/tournaments")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("fall major"));
    }

    @Test
    public void checkGet_whenSeriesIdSearched_thenTournamentAreReturned() throws Exception {
        Tournament tournament = TestDataUtil.makeTournament(1);
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1);
        series.setTournament(tournament);


        doReturn(Collections.singletonList(tournament)).when(tournamentRepository).findBySeriesId(1);

        mockMvc.perform(get("/tournaments?series=1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("fall major"));
    }

    @Test
    public void checkGet_whenTournamentNameSearched_thenTournamentAreReturned() throws Exception {
        Tournament tournament = TestDataUtil.makeTournament(1);
        tournament.setName("Test");

        doReturn(Collections.singletonList(tournament)).when(tournamentRepository).findByTournamentName("Test");

        mockMvc.perform(get("/tournaments?name=Test")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    public void checkGet_whenTournamentLocationSearched_thenTournamentAreReturned() throws Exception {
        Tournament tournament = TestDataUtil.makeTournament(1);
        tournament.setLocation("Test");

        doReturn(Collections.singletonList(tournament)).when(tournamentRepository).findByTournamentLocation("Test");

        mockMvc.perform(get("/tournaments?location=Test")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("fall major"));
    }

    @Test
    public void checkGet_whenGameIdSearched_thenTournamentAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.getLinkedGames().add(TestDataUtil.makeGame(1, series));

        doReturn(Collections.singletonList(series.getTournament())).when(tournamentRepository).findByGameId(1);

        mockMvc.perform(get("/tournaments?game=1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("fall major"));
    }

    @Test
    public void checkPost_whenValidTournament_thenIsOk() throws Exception {
        mockMvc.perform(post("/tournaments")
                        .contentType("application/json")
                        .content("{\"name\":\"fall major\", \"location\":\"sweden\", \"date\":\"2021-08-08\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkPut_whenValidTournament_thenIsOk() throws Exception {
        mockMvc.perform(put("/tournaments")
                        .contentType("application/json")
                        .content("{\"name\":\"fall major\", \"location\":\"sweden\", \"date\":\"2021-08-08\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/tournaments/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(tournamentRepository).deleteById(eq(1));
    }

    //********************************* Negative Tests *********************************

    @Test
    public void checkGet_whenGameIdSearched_thenExceptionIsThrown() throws Exception {

        doThrow(new EntityNotFoundException()).when(tournamentRepository).findByGameId(1);

        mockMvc.perform(get("/tournaments?game=1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkGet_whenIdSearched_thenExceptionIsThrown() throws Exception {

        doThrow(new EntityNotFoundException()).when(tournamentRepository).findById(1);

        mockMvc.perform(get("/tournaments/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenInValidTournament_thenIsConflict() throws Exception {
        Tournament tournament = TestDataUtil.makeTournament(1);

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(tournamentRepository).save(tournament);

        mockMvc.perform(post("/tournaments")
                        .contentType("application/json")
                        .content(gson.toJson(tournament)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkPut_whenInValidTournament_thenIsConflict() throws Exception {
        Tournament tournament = TestDataUtil.makeTournament(1);

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(tournamentRepository).save(tournament);

        mockMvc.perform(put("/tournaments")
                        .contentType("application/json")
                        .content(gson.toJson(tournament)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkDelete_whenInvalidId_thenIsNotFound() throws Exception {

        doThrow(new EmptyResultDataAccessException("need one element", 1)).when(tournamentRepository).deleteById(2);

        mockMvc.perform(delete("/tournaments/2")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }



}
