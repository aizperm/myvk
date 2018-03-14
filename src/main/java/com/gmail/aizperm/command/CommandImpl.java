package com.gmail.aizperm.command;

public abstract class CommandImpl implements CommandArgs
{
    protected Arguments args;

    public CommandImpl()
    {
        this(new Arguments());
    }

    public CommandImpl(Arguments args)
    {
        super();
        this.args = args;
    }

    public Arguments getArgs()
    {
        return args;
    }

}
