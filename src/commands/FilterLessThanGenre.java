package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;
import models.Movie;
import models.Request;

/**
 * Команда 'filter_less_than_genre'. Выводит элементы, значения поля genre которых меньше заданного.
 */
public class FilterLessThanGenre extends Command{
    private final StandartConsole console;
    private final CollectionManager collectionManager;

    public FilterLessThanGenre(StandartConsole console, CollectionManager collectionManager) {
        super("filter_less_than_genre", "вывести элементы, значение поля genre которых меньше заданного");
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

            var genre = Request.RequestGenre(console);


            if (genre != null) {
                int count = 0;
                for (var id: collectionManager.minGenre(genre)) {
                    if (id != 0) {
                        console.println(collectionManager.byId(id));
                        count += 1;
                    }
                }
                if (count == 0){
                    console.println("Не существует элементов коллекции, со значением поля genre, меньшим, чем заданный!");
                }
                else{
                    console.println("Все элементы из коллекции, со значением поля genre, меньшим, чем заданный выведены!");
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
