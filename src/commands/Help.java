package commands;

import checks.managers.Command;
import checks.managers.CommandsManager;
import checks.StandartConsole;


/**
 * Команда 'help'. Выводит справку по доступным командам.
 */
public class Help extends Command{
    private final StandartConsole console;
    private final CommandsManager commandsManager;

    public Help(StandartConsole console, CommandsManager commandsManager) {
        super("help", "вывести справку по доступным командам");
        this.commandsManager = commandsManager;
        this.console = console;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        commandsManager.getCommands().values().forEach(command -> {
            console.printTable(command.getName(), command.getDescription());
        });
        return true;
    }

}
