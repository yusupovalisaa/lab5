package checks;

import checks.managers.CommandsManager;
import checks.StandartConsole;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

/**
 * Класс работы приложения.
 */
public class Run {

    public enum ExitCode {
        OK,
        ERROR,
        EXIT
    }

    private StandartConsole console;
    private final CommandsManager commandsManager;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    public Run(StandartConsole console, CommandsManager commandsManager) {
        this.console = console;
        this.commandsManager = commandsManager;
    }


    /**
     * Интерактивный режим.
     */
    public void interactiveMode() {
        try {
            ExitCode commandStatus;
            String[] userCommand = {"", ""};
            do {
                console.prompt();
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandsManager.addToHistory(userCommand[0]);
                commandStatus = launchCommand(userCommand);

            } while (commandStatus != ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }

    /**
     * Режим для запуска скрипта.
     */
    public ExitCode scriptMode(String argument) {
        String[] userCommand = {"", ""};
        ExitCode commandStatus;
        scriptStack.add(argument);
        if (!new File(argument).exists()) {
            console.printError("Файл не существет!");
            return ExitCode.ERROR;
        }
        if (!Files.isReadable(Paths.get(argument))) {
            console.printError("Прав для чтения нет!");
            return ExitCode.ERROR;
        }
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);

            do {
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(console.getPrompt() + String.join(" ", userCommand));
                var needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    var recStart = -1;
                    var i = 0;
                    for (String script : scriptStack) {
                        i++;
                        if (userCommand[1].equals(script)) {
                            if (recStart < 0) recStart = i;
                            if (lengthRecursion < 0) {
                                console.selectConsoleScanner();
                                console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..500)");
                                while (lengthRecursion < 0 || lengthRecursion > 500) {
                                    try { console.print("> "); lengthRecursion = Integer.parseInt(console.readln().trim()); } catch (NumberFormatException e) { console.println("длина не распознана"); }
                                }
                                console.selectFileScanner(scriptScanner);
                            }
                            if (i > recStart + lengthRecursion || i > 500)
                                needLaunch = false;
                        }
                    }
                }
                commandStatus = needLaunch ? launchCommand(userCommand) : ExitCode.OK;
            } while (commandStatus == ExitCode.OK && console.isCanReadln());

            console.selectConsoleScanner();
            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;
        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
            console.printError(exception.toString());
        } catch (NoSuchElementException exception) {
            console.printError("Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }

    /**
     * Запуск команд.
     */
    private ExitCode launchCommand(String[] userCommand) {
        if (userCommand[0].equals("")) return ExitCode.OK;
        var command = commandsManager.getCommands().get(userCommand[0]);
        if (command == null) {
            console.printError("Команда '" + userCommand[0] + "' не найдена. Введите команду 'help' для справки");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit" -> {
                if (!commandsManager.getCommands().get("exit").apply(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!commandsManager.getCommands().get("execute_script").apply(userCommand)) return ExitCode.ERROR;
                else return scriptMode(userCommand[1]);
            }
            default -> { if (!command.apply(userCommand)) return ExitCode.ERROR; }
        };

        return ExitCode.OK;
    }
}