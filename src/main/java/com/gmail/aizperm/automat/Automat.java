package com.gmail.aizperm.automat;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Automat
{
    private AutomatHandler handler;
    private String state;
    private Map<String, Map<String, String>> переходы = new TreeMap<>();
    private Set<String> allCommands = new TreeSet<>();

    public Automat(String state, AutomatHandler handler)
    {
        super();
        this.state = state;
        this.handler = handler;
    }

    public void handle(String command)
    {
        Map<String, String> кудаМожноПерейти = переходы.get(state);
        if (кудаМожноПерейти != null)
        {
            String состояниеКуда = кудаМожноПерейти.get(command);
            if (состояниеКуда != null)
            {
                String stateBefore = state;
                state = состояниеКуда;
                handler.changeState(this, stateBefore, state);
            }
        }
        handler.handle(this, state, command);
    }

    public void addTransition(String command, String stateFrom, String stateTo)
    {
        allCommands.add(command);
        Map<String, String> map = переходы.get(stateFrom);
        if (map == null)
            map = new TreeMap<>();
        переходы.put(stateFrom, map);
        map.put(command, stateTo);
    }
}
