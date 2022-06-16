package ch.bbw.controllers;

import ch.bbw.repositories.TeamRepository;
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

@WebMvcTest(controllers = TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamRepository teamRepository;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @MockBean
    private UserDetailsServiceImpl userDetailsService;      // Wird vom SpringBoot Testing Framework benötigt

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;    // Wird vom SpringBoot Testing Framework benötigt


    @Test
    public void checkGet_whenValidIdSearched_thenAccordingTeamReturned() throws Exception {
        Team team = TestDataUtil.makeTeamWithValidPlayers();
        team.setId(1);
        doReturn(Optional.of(team)).when(teamRepository).findById(1);

        mockMvc.perform(get("/teams/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("RandomTeam"));

    }

    @Test
    public void checkGet_whenPlayerNameSearched_thenAllAreReturned() throws Exception {
        Team team = TestDataUtil.makeTeamWithValidPlayers();


        doReturn(Collections.singletonList(team)).when(teamRepository).findAll();

        mockMvc.perform(get("/teams")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("RandomTeam"));
    }

    @Test
    public void checkGet_whenPlayerNameSearched_thenTeamAreReturned() throws Exception {
        Team team = TestDataUtil.makeTeamWithValidPlayers();


        doReturn(Collections.singletonList(team)).when(teamRepository).findByPlayerName("playerOne");

        mockMvc.perform(get("/teams?playerName=playerOne")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("RandomTeam"));
    }

    @Test
    public void checkGet_whenTeamNameSearched_thenTeamAreReturned() throws Exception {
        Team team = TestDataUtil.makeTeamWithValidPlayers();


        doReturn(Collections.singletonList(team)).when(teamRepository).findByTeamName("RandomName");

        mockMvc.perform(get("/teams?name=RandomName")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("RandomTeam"));
    }

    @Test
    public void checkGet_whenPlayerGamerTagSearched_thenTeamAreReturned() throws Exception {
        Team team = TestDataUtil.makeTeamWithValidPlayers();
        team.getLinkedPlayers().add(TestDataUtil.makePlayer(1));

        doReturn(Collections.singletonList(team)).when(teamRepository).findByPlayerGamerTag("gamertag");

        mockMvc.perform(get("/teams?playerGamerTag=gamertag")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("RandomTeam"));
    }

    @Test
    public void checkGet_whenTournamentIdSearched_thenTeamAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();

        doReturn(Collections.singletonList(series.getTeamOne())).when(teamRepository).findByTournamentId(1);

        mockMvc.perform(get("/teams?tournament=1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("RandomTeam"));
    }

    @Test
    public void checkGet_whenSeriesIdSearched_thenTeamAreReturned() throws Exception {
        Series series = TestDataUtil.makeSeriesWithValidTeams();
        series.setId(1134);

        doReturn(Collections.singletonList(series.getTeamOne())).when(teamRepository).findBySeriesId(1134);

        mockMvc.perform(get("/teams?series=1134")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("RandomTeam"));
    }

    @Test
    public void checkPost_whenValidTeam_thenIsOk() throws Exception {
        mockMvc.perform(post("/teams")
                        .contentType("application/json")
                        .content("{\"name\":\"nrg\", \"rating\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkPut_whenValidTeam_thenIsOk() throws Exception {
        mockMvc.perform(put("/teams")
                        .contentType("application/json")
                        .content("{\"name\":\"nrg\", \"rating\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkDelete_whenValidId_thenIsOk() throws Exception {
        mockMvc.perform(delete("/teams/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Mockito.verify(teamRepository).deleteById(eq(1));
    }

    //********************************* Negative Tests *********************************

    @Test
    public void checkGet_whenIdSearched_thenExceptionIsThrown() throws Exception {

        doThrow(new EntityNotFoundException()).when(teamRepository).findById(1);

        mockMvc.perform(get("/teams/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkPost_whenInValidTeam_thenIsConflict() throws Exception {
        Team team = TestDataUtil.makeTeamWithValidPlayers();

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(teamRepository).save(team);

        mockMvc.perform(post("/teams")
                        .contentType("application/json")
                        .content(gson.toJson(team)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkPut_whenInValidTeam_thenIsConflict() throws Exception {
        Team team = TestDataUtil.makeTeamWithValidPlayers();

        doThrow(new DataIntegrityViolationException("Tournament could not be updated")).when(teamRepository).save(team);

        mockMvc.perform(put("/teams")
                        .contentType("application/json")
                        .content(gson.toJson(team)))
                .andExpect(status().isConflict());
    }

    @Test
    public void checkDelete_whenInvalidId_thenIsNotFound() throws Exception {

        doThrow(new EmptyResultDataAccessException("need one element", 1)).when(teamRepository).deleteById(2);

        mockMvc.perform(delete("/teams/2")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

}
