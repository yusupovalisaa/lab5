package commands;

import checks.managers.Command;
import checks.StandartConsole;

/**
 * Команда 'exit'. Завершает программу без сохранения в файл.
 */
public class Exit extends Command{
    private final StandartConsole console;

    public Exit(StandartConsole console) {
        super("exit", "завершить программу (без сохранения в файл)");
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

        console.println("Завершение выполнения...");
        return true;
    }
}
