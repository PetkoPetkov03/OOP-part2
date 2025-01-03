package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

/**
 * Enum representing the state of an arrival process for goods.
 * The arrival process can be either:
 * - {@link #Open} : The arrival process is currently open and ongoing.
 * - {@link #Closed} : The arrival process has been closed and is complete.
 */
public enum ArrivalState {

    /**
     * The arrival process is currently open and ongoing.
     */
    Open,

    /**
     * The arrival process has been closed and is complete.
     */
    Closed
}
