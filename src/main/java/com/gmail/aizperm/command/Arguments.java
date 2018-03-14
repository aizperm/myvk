package com.gmail.aizperm.command;

import java.util.ArrayList;
import java.util.List;

public class Arguments
{
    private List<Object> args = new ArrayList<>();

    public List<Object> getArgs()
    {
        return args;
    }

    public void addArg(Object arg)
    {
        this.args.add(arg);
    }

}
