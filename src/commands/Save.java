package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;

/**
 * Команда 'save'. Сохраняет коллекцию в файл.
 */
public class Save extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;

    public Save(StandartConsole console, CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
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

        collectionManager.saveCollection();
        return true;
    }
}
