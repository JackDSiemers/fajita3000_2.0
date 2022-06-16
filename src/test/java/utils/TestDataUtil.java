package utils;

import ch.bbw.models.*;

import java.sql.Date;

public class TestDataUtil {

    public static Series makeSeriesWithValidTeams() {
        Series series = new Series();
        Team teamOne = makeTeamWithValidPlayers();
        Team teamTwo = makeTeamWithValidPlayers();
        series.setTeamOne(teamOne);
        series.setTeamTwo(teamTwo);
        series.setWinner(teamOne);
        series.setTournament(makeTournament(1));
        return series;
    }

    public static Team makeTeamWithValidPlayers(){
        Team team = new Team();
        team.getLinkedPlayers().add(makePlayer(1));
        team.getLinkedPlayers().add(makePlayer(2));
        team.getLinkedPlayers().add(makePlayer(3));
        team.setRating(1);
        team.setName("RandomTeam");
        return team;
    }

    public static Game makeGame(int id, Series series){
        Game game = new Game();
        game.setSeries(series);
        game.setWinner(series.getTeamOne());
        game.setNumber(1);
        game.setId(id);
        return game;
    }

    public static Player makePlayer(int id){
        Player player = new Player();
        player.setName("name");
        player.setId(id);
        player.setSurname("Surname");
        player.setGamertag("Gamertag");
        player.setRating(10);
        return player;
    }

    public static Tournament makeTournament(int id){
        Tournament tournament = new Tournament();
        tournament.setDate(Date.valueOf("2021-08-08"));
        tournament.setName("fall major");
        tournament.setLocation("sweden");
        tournament.setId(id);
        return tournament;
    }
}
