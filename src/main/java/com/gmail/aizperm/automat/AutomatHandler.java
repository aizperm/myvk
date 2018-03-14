package com.gmail.aizperm.automat;

public interface AutomatHandler
{
    void changeState(Automat automat, String stateBefore, String stateAfter);

    void handle(Automat automat, String state, String command);
}
