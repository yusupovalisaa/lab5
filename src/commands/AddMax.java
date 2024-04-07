package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;
import models.Movie;
import models.Request;

/**
 * Команда 'add_if_max'. Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
 * Работает по полям oscarsCount.
 */
public class AddMax extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;

    public AddMax (StandartConsole console, CollectionManager collectionManager) {
        super("addMax", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
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
                if (m.getOscarsCount()> collectionManager.FindMax()){
                    collectionManager.add(m);
                    collectionManager.addLog("addMax " + m.getId(), true);
                    console.println("Фильм успешно добавлен!");
                    return true;
                }
                else{
                    console.println("Количество оскаров не превышает значение наибольшего элемента! Фильм не создан!");
                    return false;
                }

            }
            else {
                console.println("Поля не валидны! Фильм не создан!");
                return false;
            }
        } catch (Request.RequestBreak e) {
            console.println("Отмена...");
            return false;
        }
    }


}
