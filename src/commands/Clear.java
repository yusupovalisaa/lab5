package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;

/**
 * Команда 'clear'. Очищает коллекцию.
 */

public class Clear extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;

    public Clear(StandartConsole console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
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
        for (var ind : collectionManager.clear()) {
            collectionManager.remove(ind);
        }

        collectionManager.update();
        console.println("Коллекция очищена!");
        return true;
    }
}
