package com.gmail.aizperm.command.impl;

import java.util.List;

import com.gmail.aizperm.command.CommandImpl;
import com.gmail.aizperm.sign.PhotoSignerImpl;
import com.gmail.aizperm.util.FontUtils;

public class TextSignCommand extends CommandImpl
{

    @Override
    public Object execute() throws Exception
    {
        byte[] sourceBytes = getBytes();
        String text = getText();
        String font = getFont();
        byte[] signedBytes = new PhotoSignerImpl().sign(text, font, sourceBytes);
        return signedBytes;
    }

    private String getText()
    {
        List<Object> args = getArgs().getArgs();
        if (args.size() > 1)
        {
            String text = (String) args.get(0);
            return text;
        }
        return "Не указан текст";
    }

    private String getFont()
    {
        List<Object> args = getArgs().getArgs();
        if (args.size() > 2)
        {
            String font = (String) args.get(1);
            if (FontUtils.isExists(font))
                return font;
            else
            {
                try
                {
                    int number = Integer.parseInt(font);
                    return FontUtils.getByNumber(number);
                }
                catch (NumberFormatException e)
                {
                }
            }
        }
        return FontUtils.MAYA;
    }

    private byte[] getBytes()
    {
        List<Object> args = getArgs().getArgs();
        return (byte[]) args.get(args.size() - 1);
    }
}
