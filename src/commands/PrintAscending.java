package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;

/**
 * Команда 'print_ascending'. Выводит элементы коллекции в порядке возрастания ID.
 */
public class PrintAscending extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;
    public PrintAscending(StandartConsole console, CollectionManager collectionManager) {
        super("print_ascending", "вывести элементы коллекции в порядке возрастания ID");
        this.console = console;
        this.collectionManager = collectionManager;
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
        console.println(collectionManager);
        console.println("Элементы коллекции выстроены в порядке возрастания их ID!");
        return true;
    }
}
