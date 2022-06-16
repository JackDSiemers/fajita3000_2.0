package ch.bbw.controllers;

import ch.bbw.repositories.PlayerRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRepository playerRepository;

    @Autowired
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @MockBean
    private UserDetailsServiceImpl userDetailsService;      // Wird vom SpringBoot Testing Framework benötigt

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;    // Wird vom SpringBoot Testing Framework benötigt


    @Test
    public void checkGet_whenValidId_thenAccordingPlayerReturned() throws Exception {
        Player player = new Player();
        player.setId(1);
        player.setName("Liam");
        doReturn(Optional.of(player)).when(playerRepository).findById(1);

        mockMvc.perform(get("/players/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(player.getName()));

    }

    @Test
    public void checkGet_whenPlayerNameSearched_thenAllAreReturned() throws Exception {
        Player player = TestDataUtil.makePlayer(1);

        doReturn(Collections.singletonList(player)).when(playerRepository).findAll();

        mockMvc.perform(get("/players")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    public void checkGet_whenPlayerNameSearched_thenPlayersAreReturned() throws Exception {
        Player player = TestDataUtil.makePlayer(1);

        doReturn(Collections.singletonList(player)).when(playerRepository).findByPlayerName("name");

        mockMvc.perform(get("/players?name=name")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    public void checkGet_whenPlayerGamerTagSearched_thenPlayersAreReturned() throws Exception {
        Player player = TestDataUtil.makePlayer(1);

        doReturn(Collections.singletonList(player)).when(playerRepository).findByPlayerGamertag("gamertag");

        mockMvc.perform(get("/players?gamertag=gamertag")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    public void checkGet_whenPlayerRatingSearched_thenPlayersAreReturned() throws Exception {
        Player player = TestDataUtil.makePlayer(1);

        doReturn(Collections.singletonList(player)).when(playerRepository).findByPlayerRating(10);

        mockMvc.perform(get("/players?rating=10")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    public void checkGet_whenSeriesIdSearched_thenPlayersAreReturned() throws Exception {
        Player player = TestDataUtil.makePlayer(1);

        doReturn(Collections.singletonList(player)).when(playerRepository).findBySeriesId(1);

        mockMvc.perform(get("/players?series=1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    public void checkGet_whenTournamentIdSearched_thenPlayersAreReturned() throws Exception {
        Player player = TestDataUtil.makePlayer(1);
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        Tournament tournament = TestDataUtil.makeTournament(5);
        series.setTournament(tournament);
        series.getTeamOne().getLinkedPlayers().add(player);

        doReturn(Collections.singletonList(player)).when(playerRepository).findByTournamentId(5);

        mockMvc.perform(get("/players?tournament=5")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    public void checkGet_whenGamesIdSearched_thenPlayersAreReturned() throws Exception {
        Player player = TestDataUtil.makePlayer(1);
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.getTeamOne().getLinkedPlayers().add(player);
        series.getLinkedGames().add(TestDataUtil.makeGame(5,series));

        doReturn(Collections.singletonList(player)).when(playerRepository).findByGameId(5);

        mockMvc.perform(get("/players?game=5")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    public void checkPost_whenValidPlayer_thenIsOk() throws Exception {
        mockMvc.perform(post("/players")
                        .contentType("application/json")
                        .content("{\"name\":\"andreas\", \"gamertag\":\"ReasDerBre\", \"surname\":\"joergenson\", \"team\":" + gson.toJson(TestDataUtil.makeTeamWithValidPlayers()) + "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkPut_whenValidPlayer_thenIsOk() throws Exception {
        mockMvc.perform(put("/players")
                        .contentType("application/json")
                        .content("{\"name\":\"andreas\", \"gamertag\":\"ReasDerBre\", \"surname\":\"joergenson\", \"team\":" + gson.toJson(TestDataUtil.makeTeamWithValidPlayers()) + "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/players/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(playerRepository).deleteById(eq(1));
    }

    //********************************* Negative Tests *********************************

    @Test
    public void checkGet_whenIdSearched_thenExceptionIsThrown() throws Exception {

        doThrow(new EntityNotFoundException()).when(playerRepository).findById(1);

        mockMvc.perform(get("/players/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenInValidPlayer_thenIsOk() throws Exception {
        Player player = TestDataUtil.makePlayer(1);
        player.setTeam(TestDataUtil.makeTeamWithValidPlayers());

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(playerRepository).save(player);

        mockMvc.perform(post("/players")
                        .contentType("application/json")
                        .content(gson.toJson(player)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkPut_whenInValidPlayer_thenIsOk() throws Exception {
        Player player = TestDataUtil.makePlayer(1);
        player.setTeam(TestDataUtil.makeTeamWithValidPlayers());

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(playerRepository).save(player);

        mockMvc.perform(put("/players")
                        .contentType("application/json")
                        .content(gson.toJson(player)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkDelete_whenInvalidId_thenIsNotFound() throws Exception {

        doThrow(new EmptyResultDataAccessException("need one element", 1)).when(playerRepository).deleteById(2);

        mockMvc.perform(delete("/players/2")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }


}
