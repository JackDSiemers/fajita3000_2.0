package ch.bbw.controllers;

import ch.bbw.models.Game;
import ch.bbw.repositories.GameRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameRepository gameRepository;


    package ch.bbcag.controllers;

import ch.bbcag.models.Game;
import ch.bbcag.repositories.GameRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/{id}")
    public Game findById(@PathVariable int id) {
        try {
            return gameRepository.findById(id).orElseThrow();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error! Wasn't able to find Id");
        }
    }

    @GetMapping
    public Iterable<Game> findByDifferentParams(@Parameter(description = "Game winner id, series id, tournament id or game number to search") @Valid @RequestParam(required = false) Integer winner, @Valid @RequestParam(required = false) Integer series, @Valid @RequestParam(required = false) Integer game, @Valid @RequestParam(required = false) Integer tournament) {
        try {
            if (winner != null) {
                return gameRepository.findByWinnerId(winner);
            }
            if (series != null) {
                if (game != null) {
                    return gameRepository.findByGameNumberAndSeriesId(game, series);
                } else {
                    return gameRepository.findBySeriesId(series);
                }
            }
            if (tournament != null) {
                return gameRepository.findByTournamentId(tournament);
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error! Wasn't able to find Team");
        }

        return gameRepository.findAll();
    }

    @PostMapping
    public void insert(@Parameter(description = "The new Game to create") @Valid @RequestBody Game newGame) {
        try {
            gameRepository.save(newGame);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error! Wasn't able to create Game");
        }
    }

    @PutMapping(consumes = "application/json")
    public void update(@Parameter(description = "The Game to update") @Valid @RequestBody Game game) {
        try {
            gameRepository.save(game);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game could not be updated");
        }
    }

    @DeleteMapping("{id}")
    public void delete(@Parameter(description = "Id of Game to delete") @Valid @PathVariable Integer id) {
        try {
            gameRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game could not be deleted");
        }
    }


}






}
