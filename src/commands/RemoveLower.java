package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;
import models.Movie;
import models.Request;


/**
 * Команда 'remove_lower'. Удаляет из коллекции все элементы, меньшие, чем заданный.
 */
public class RemoveLower extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;

    public RemoveLower(StandartConsole console, CollectionManager collectionManager) {
        super("remove_lower <element>", "удалить из коллекции все элементы, меньшие, чем заданный");
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
                int count = 0;
                for (var ind : collectionManager.deleteMins()) {
                    collectionManager.remove(ind);
                    if (ind != 0){
                        count += 1;
                    }
                }
                if (count == 0){
                    console.println("Не существует элементов коллекции, меньших, чем заданный!");
                }
                else{
                    console.println("Все элементы из коллекции, меньшие, чем заданный удалены!");
                }
                return true;
            }
            else {
                console.printError("Поля не валидны! Фильм не создан!");
                return false;
            }
        } catch (Request.RequestBreak e) {
            console.println("Отмена...");
            return false;
        }
    }
}