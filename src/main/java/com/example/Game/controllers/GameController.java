package com.example.Game.controllers;

import com.example.Game.wrappers.ApiBoardWrapper;
import com.example.Game.wrappers.ApiGameWrapper;
import com.example.Game.wrappers.ApiLocationWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * The class that defined endpoints for the backend requests of the application.
 */
@RestController
@RequestMapping("/api")
public class GameController {
    public final int MAX_CORNER = 9;
    public final int MIN_CORNER = 0;
    private List<ApiGameWrapper> gameWrappers = new ArrayList<>();
    private List<ApiBoardWrapper> boards = new ArrayList<>();
    private AtomicInteger nextId = new AtomicInteger();
    @GetMapping("/about")
    public String getMyName(){
        return "Kian Hosseinkhani";
    }

    @GetMapping("/games")
    public List<ApiGameWrapper> getGames(){
        return gameWrappers;
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper createGame(){
        //-1 to make it the same index
        int index = nextId.incrementAndGet();
        ApiGameWrapper newGame = new ApiGameWrapper(index);
        ApiBoardWrapper newBoard = new ApiBoardWrapper(index);
        boards.add(newBoard);
        gameWrappers.add(newGame);
        return newGame;
    }

    @GetMapping("/games/{id}")
    public ApiGameWrapper getOneGame(@PathVariable("id") int gameId) throws IllegalAccessException {
        if(gameId > gameWrappers.size() || gameId < 0){
            throw new IllegalAccessException();
        }
        return gameWrappers.get(gameId-1);

    }

    @GetMapping("/games/{id}/board")
    public ApiBoardWrapper getTheBoard(@PathVariable("id") int gameId) throws IllegalAccessException {
        if(gameId > gameWrappers.size() || gameId < 0){
            throw new IllegalAccessException();
        }
        ApiBoardWrapper board = boards.get(gameId-1);
        board.updateGameBoard(gameWrappers.get(board.getId()-1).isCheat());
        return board;
    }

    @PostMapping("/games/{id}/moves")
    public void getLocation(@PathVariable("id") int gameId,
                              @RequestBody ApiLocationWrapper location
    ) throws IllegalAccessException {
        if(gameId > gameWrappers.size() || gameId < 0){
            throw new IllegalAccessException();
        }
        if(location.getRow() > MAX_CORNER || location.col > MAX_CORNER || location.getRow() < MIN_CORNER || location.getCol() < MIN_CORNER){
            throw new IllegalArgumentException();
        }
        ApiLocationWrapper saveLocation = new
                ApiLocationWrapper(location.getRow(),
                location.getCol());

        ApiGameWrapper game = gameWrappers.get(gameId-1);
        game.userTurn(saveLocation.getCoordinates());

    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/games/{id}/cheatstate")
    public void getCheat(@PathVariable("id") int gameId,
                            @RequestBody String msg
    ) throws IllegalAccessException {
        if(gameId > gameWrappers.size() || gameId < 0){
            throw new IllegalAccessException();
        }
        if(!"SHOW_ALL".equals(msg) || msg == null || msg.equals("")){
            throw new IllegalArgumentException();
        }
        else{
            gameWrappers.get(gameId-1).setCheat(true);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "ID does not exist")
    @ExceptionHandler(IllegalAccessException.class)
    public void badIdExceptionHandler(){
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request for location or Cheat")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badBodyArgument(){
    }
}
