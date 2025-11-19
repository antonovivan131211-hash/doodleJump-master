package io.github.some_example_name;

public class GameSession {
    public enum GameState {
        PLAYING,
        PAUSED,
        GAME_OVER
    }

    public GameState state;

    public GameSession() {
        this.state = GameState.PLAYING;
    }

    public void startGame() {
        this.state = GameState.PLAYING;
    }

    public void pauseGame() {
        this.state = GameState.PAUSED;
    }

    public void resumeGame() {
        this.state = GameState.PLAYING;
    }

    public void gameOver() {
        this.state = GameState.GAME_OVER;
    }
}
