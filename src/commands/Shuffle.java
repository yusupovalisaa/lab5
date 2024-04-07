package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;


/**
 * Команда 'shffle'. Мешает элементы коллекции в случайном порядке.
 */
public class Shuffle extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;
    public Shuffle(StandartConsole console, CollectionManager collectionManager) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке");
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
        collectionManager.shuffle();
        console.println("Элементы коллекции перемешаны в случайном порядке!");
        return true;
    }
}
