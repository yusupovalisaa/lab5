package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;


/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по ID.
 */
public class RemoveByID extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;

    public RemoveByID(StandartConsole console, CollectionManager collectionManager) {
        super("remove_by_id Id", "удалить элемент из коллекции по ID");
        this.console = console;
        this.collectionManager = collectionManager;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }
        long id = -1;
        try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { console.println("ID не распознан"); return false; }

        if (collectionManager.byId((int)id) == null || !collectionManager.getCollection().contains(collectionManager.byId((int)id))) {
            console.println("не существующий ID");
            return false;
        }
        collectionManager.remove(id);
        collectionManager.addLog("remove " + id, true);
        collectionManager.update();
        console.println("Фильм успешно удалён!");
        return true;
    }
}