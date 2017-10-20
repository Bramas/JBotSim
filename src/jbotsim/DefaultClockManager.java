/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package jbotsim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

public class DefaultClockManager implements ClockManager {


    private ScheduledExecutorService executor;
    private boolean running = false;
    private int nbPauses = 0;
    private boolean isStarted = false;
    private int period = 10;

    private Clock clock;

    public DefaultClockManager(Clock clock) {
      this.clock = clock;
    }

    private void startTimer() {
      executor = Executors.newSingleThreadScheduledExecutor();
      executor.scheduleAtFixedRate(() -> {
          clock.step();
      }, period, period, TimeUnit.MILLISECONDS);

      running = true;
    }
    private void stopTimer() {
      executor.shutdown();
      executor = null;
      running = false;
    }

    public void start() {
      if (! isStarted) {
          isStarted = true;
          startTimer();
      }
    }
    public void pause(){
      if (isStarted) {
          running = false;
          if (nbPauses == 0)
              stopTimer();
          nbPauses++;
      }
    }
    public void resume(){
      if (isStarted) {
          running = true;
          assert (nbPauses > 0);
          nbPauses--;
          if (nbPauses == 0)
              startTimer();
      }
    }
    public boolean isRunning(){
      return running;
    }
    /**
     * Returns the time unit of the clock, in milliseconds.
     */
    public int getTimeUnit(){
        return this.period;
    }

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     * @param delay The desired time unit (1 corresponds to the fastest rate)
     */
    public void setTimeUnit(int period){
        this.period = period;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
