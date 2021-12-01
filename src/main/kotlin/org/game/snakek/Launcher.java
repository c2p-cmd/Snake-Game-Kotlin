package org.game.snakek;

import org.game.snakek.gui.Main;

public class Launcher implements Runnable {
    @Override
    public void run() {
        new Thread(new Main(), "Main Thread").start();
    }

    public static void main(String[] args) {
        new Thread(new Launcher(), "Launcher Thread").start();
    }
}
