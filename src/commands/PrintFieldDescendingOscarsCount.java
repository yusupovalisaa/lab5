package commands;

import checks.managers.CollectionManager;
import checks.managers.Command;
import checks.StandartConsole;


/**
 * Команда 'print_field_descending_oscars_count'. Выводит значение поля oscarsCount всех элементов в порядке убывания.
 */
public class PrintFieldDescendingOscarsCount extends Command {
    private final StandartConsole console;
    private final CollectionManager collectionManager;
    public PrintFieldDescendingOscarsCount(StandartConsole console, CollectionManager collectionManager) {
        super("print_field_descending_oscars_count ", "вывести значения поля oscarsCount всех элементов в порядке убывания");
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
        for (var ind : collectionManager.FieldOscars()) {
            console.println(ind);
        }
        console.println("Значения поля oscarsCount всех элементов выстроены в порядке убывания!");
        return true;
    }
}
