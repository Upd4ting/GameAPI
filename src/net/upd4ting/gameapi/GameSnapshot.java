package net.upd4ting.gameapi;

public interface GameSnapshot {
    void restore(Game game);
    void restore(String world, String name);
    void save(Game game);
}
