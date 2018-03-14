package com.gmail.aizperm.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gmail.aizperm.automat.Automat;
import com.gmail.aizperm.automat.AutomatHandler;
import com.gmail.aizperm.command.impl.PNGSignCommand;
import com.gmail.aizperm.command.impl.TextSignCommand;

public class CommandParser
{
    private List<String> PNG_SIGN_COMMAND = Arrays.asList("i", "img", "image");
    private List<String> TEXT_SIGN_COMMAND = Arrays.asList("t", "text");

    public CommandArgs parse(String body)
    {
        if (body != null)
        {
            char[] charArray = body.toCharArray();
            ParserHandler handler = new ParserHandler();
            Automat automat = getAutomat(handler);

            for (char c : charArray)
            {
                String comma = String.valueOf(c);
                automat.handle(comma);
            }
            automat.handle(END_COMMAND);

            String commandText = handler.getCommand();
            CommandArgs command = tryInitCommand(commandText);
            if (command != null)
            {
                List<String> args = handler.getArgs();
                for (String arg : args)
                    command.getArgs().addArg(arg);

                return command;
            }
        }
        return null;
    }

    private CommandArgs tryInitCommand(String next)
    {
        if (PNG_SIGN_COMMAND.contains(next.toLowerCase()))
            return new PNGSignCommand();
        if (TEXT_SIGN_COMMAND.contains(next.toLowerCase()))
            return new TextSignCommand();
        return null;
    }

    private static String STATE_COMMAND = "COMMAND";
    private static String STATE_ARGS = "ARGS";
    private static String STATE_ARGS_QUOTE = "ARGS_QUOTE";
    private static String STATE_END = "END";
    private static String END_COMMAND = "{END}";

    private static class ParserHandler implements AutomatHandler
    {
        StringBuilder command = new StringBuilder();
        List<String> args = new ArrayList<>();
        StringBuilder arg = new StringBuilder();

        @Override
        public void handle(Automat automat, String state, String automatCommand)
        {
            if (STATE_COMMAND.equals(state))
                command.append(automatCommand);
            else
            {
                if (!(automatCommand.equals("\"") || (automatCommand.equals(" ") && !STATE_ARGS_QUOTE.equals(state))))
                    arg.append(automatCommand);
            }
        }

        @Override
        public void changeState(Automat automat, String stateBefore, String stateAfter)
        {
            if (!STATE_COMMAND.equals(stateBefore))
            {
                if (arg.length() > 0)
                {
                    args.add(arg.toString());
                    arg = new StringBuilder();
                }
            }
        }

        public String getCommand()
        {
            return command.toString();
        }

        public List<String> getArgs()
        {
            return args;
        }
    }

    private static Automat getAutomat(ParserHandler handler)
    {
        Automat automat = new Automat(STATE_COMMAND, handler);
        automat.addTransition(" ", STATE_COMMAND, STATE_ARGS);
        automat.addTransition("\"", STATE_COMMAND, STATE_ARGS_QUOTE);
        automat.addTransition("\"", STATE_ARGS, STATE_ARGS_QUOTE);
        automat.addTransition("\"", STATE_ARGS_QUOTE, STATE_ARGS);
        automat.addTransition(" ", STATE_ARGS, STATE_ARGS);
        automat.addTransition(END_COMMAND, STATE_ARGS, STATE_END);
        automat.addTransition(END_COMMAND, STATE_ARGS_QUOTE, STATE_END);
        automat.addTransition(END_COMMAND, STATE_COMMAND, STATE_END);
        return automat;
    }

    public static void main(String[] a)
    {
        ParserHandler handler = new ParserHandler();
        Automat automat = getAutomat(handler);

        String text = "t \"2 333";
        char[] charArray = text.toCharArray();
        for (char c : charArray)
        {
            String comma = String.valueOf(c);
            automat.handle(comma);
        }
        automat.handle(END_COMMAND);

        System.out.println(handler.getCommand() + " " + handler.getArgs());
    }

}
