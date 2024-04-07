package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;
import commands.Executable;
import models.Request;


/**
 * Команда 'update ID'. Обновляет элемент коллекции по ID.
 */
public class UpdateID extends Command{
    private final StandartConsole console;
    private final CollectionManager collectionManager;

    public UpdateID(StandartConsole console, CollectionManager collectionManager) {
        super("update Id", "обновить значение элемента коллекции по ID");
        this.console = console;
        this.collectionManager = collectionManager;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            if (arguments[1].isEmpty()) {
                console.println("Неправильное количество аргументов!");
                console.println("Использование: '" + getName() + "'");
                return false;
            }
            long id = -1;
            try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { console.println("ID не распознан"); return false; }

            if (collectionManager.byId((int) id) == null || !collectionManager.getCollection().contains(collectionManager.byId((int) id))) {
                console.println("не существующий ID");
                return false;
            }

            console.println("Создание нового Фильма:");
            var d = Request.RequestMovie(console, collectionManager);
            if (d != null && d.validate()) {
                collectionManager.add(d);
                collectionManager.addLog("add " + d.getId(), true);
                collectionManager.update();

                var old = collectionManager.byId((int)id);
                collectionManager.swap(d.getId(), id);
                collectionManager.addLog("swap " + old.getId() + " " + id, false);
                collectionManager.update();

                collectionManager.remove(old.getId());
                collectionManager.addLog("remove " + old.getId(), false);
                collectionManager.update();
                console.println("Фильм успешно изменен!");
                return true;
            } else {
                console.println("Поля не валидны! Фильм не создан!");
                return false;
            }
        } catch (Request.RequestBreak e) {
            console.println("Отмена...");
            return false;
        }
    }
}
