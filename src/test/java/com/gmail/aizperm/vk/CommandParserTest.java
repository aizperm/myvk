package com.gmail.aizperm.vk;

import org.junit.Assert;
import org.junit.Test;

import com.gmail.aizperm.command.Command;
import com.gmail.aizperm.command.CommandParser;
import com.gmail.aizperm.command.impl.PNGSignCommand;
import com.gmail.aizperm.command.impl.TextSignCommand;

public class CommandParserTest
{
    @Test
    public void testNull() throws Exception
    {
        Command command = new CommandParser().parse(null);
        Assert.assertNull(command);
    }

    @Test
    public void testEmpty() throws Exception
    {
        Command command = new CommandParser().parse("");
        Assert.assertNull(command);
    }

    @Test
    public void testNotCommand() throws Exception
    {
        Command command = new CommandParser().parse("a");
        Assert.assertNull(command);
    }

    @Test
    public void testPNG() throws Exception
    {
        Command command = new CommandParser().parse("i");
        Assert.assertNotNull(command);
        Assert.assertTrue(command instanceof PNGSignCommand);
        Assert.assertEquals(0, ((PNGSignCommand) command).getArgs().getArgs().size());
    }

    @Test
    public void testPNGArgs() throws Exception
    {
        Command command = new CommandParser().parse("i 2 333");
        Assert.assertNotNull(command);
        Assert.assertTrue(command instanceof PNGSignCommand);
        Assert.assertEquals(2, ((PNGSignCommand) command).getArgs().getArgs().size());
    }

    @Test
    public void testText() throws Exception
    {
        Command command = new CommandParser().parse("t \"2 333\"");
        Assert.assertNotNull(command);
        Assert.assertTrue(command instanceof TextSignCommand);
        Assert.assertEquals(1, ((TextSignCommand) command).getArgs().getArgs().size());
    }
    
    @Test
    public void testTextFont() throws Exception
    {
        Command command = new CommandParser().parse("text \"2 333\" 1");
        Assert.assertNotNull(command);
        Assert.assertTrue(command instanceof TextSignCommand);
        Assert.assertEquals(2, ((TextSignCommand) command).getArgs().getArgs().size());
    }
}
