package commands;

import checks.managers.Command;
import checks.StandartConsole;

/**
 * Команда 'execute_script'. Исполняет скрипт.
 */
public class ExecuteScript extends Command {
    private final StandartConsole console;

    public ExecuteScript(StandartConsole console) {
        super("execute_script file_name", "исполнить скрипт из указанного файла");
        this.console = console;
    }

    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        String currentDir = System.getProperty("user.dir");
        arguments[1] = currentDir + "/" + arguments[1];
        if (arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        console.println("Выполнение скрипта ...");
        return true;
    }
}
