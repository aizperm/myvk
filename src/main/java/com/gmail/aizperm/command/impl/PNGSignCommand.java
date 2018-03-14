package com.gmail.aizperm.command.impl;

import java.util.List;

import com.gmail.aizperm.command.CommandImpl;
import com.gmail.aizperm.sign.PhotoSignerImpl;
import com.gmail.aizperm.util.PNGArgs;

public class PNGSignCommand extends CommandImpl
{

    public PNGSignCommand()
    {
        super();
    }

    @Override
    public Object execute() throws Exception
    {
        byte[] sourceBytes = getBytes();
        String locale = getLocale();
        Integer type = getType();

        byte[] signedBytes = new PhotoSignerImpl().sign(locale, type, sourceBytes);
        return signedBytes;
    }

    private Integer getType()
    {
        List<Object> args = getArgs().getArgs();
        for (int i = 0; i < args.size() - 2; i++)
        {
            String arg = String.valueOf(args.get(i));
            if (String.valueOf(PNGArgs.TYPE_1).equalsIgnoreCase(arg))
                return PNGArgs.TYPE_1;
            if (String.valueOf(PNGArgs.TYPE_2).equalsIgnoreCase(arg))
                return PNGArgs.TYPE_2;
        }
        return PNGArgs.TYPE_1;
    }

    private String getLocale()
    {
        List<Object> args = getArgs().getArgs();
        for (int i = 0; i < args.size() - 2; i++)
        {
            String arg = String.valueOf(args.get(i));
            if (PNGArgs.LOCALE_RU.equalsIgnoreCase(arg))
                return PNGArgs.LOCALE_RU;
            if (PNGArgs.LOCALE_EN.equalsIgnoreCase(arg))
                return PNGArgs.LOCALE_EN;
        }
        return PNGArgs.LOCALE_RU;
    }

    private byte[] getBytes()
    {
        List<Object> args = getArgs().getArgs();
        return (byte[]) args.get(args.size() - 1);
    }

}
