package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;
import models.Movie;
import models.Request;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command{
    private final StandartConsole console;
    private final CollectionManager collectionManager;


    public Add(StandartConsole console, CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
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
            if (!arguments[1].isEmpty()) {
                console.println("Неправильное количество аргументов!");
                console.println("Использование: '" + getName() + "'");
                return false;
            }

            console.println("Создание нового фильма:");
            Movie m = Request.RequestMovie(console, collectionManager);

            if (m != null && m.validate()) {
                collectionManager.add(m);
                collectionManager.addLog("add " + m.getId(), true);
                console.println("Фильм успешно добавлен!");
                return true;
            } else {
                console.printError("Поля не валидны! Фильм не создан!");
                return false;
            }
        } catch (Request.RequestBreak e) {
            console.println("Отмена...");
            return false;
        }
    }
}

