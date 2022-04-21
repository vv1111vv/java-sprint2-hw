


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    public static String logica_amr;
    public static String logica_yr;
    private static Path filePath1 = Paths.get("resources/m.202101.csv");
    private static Path filePath2 = Paths.get("resources/m.202102.csv");
    private static Path filePath3 = Paths.get("resources/m.202103.csv");
    private static Path filePath = Paths.get("resources/y.2021.csv");


    public static void main(String[] args) throws Exception {
        Main.logica_amr = "Доступ закрыт";
        Main.logica_yr = "Доступ закрыт";
        printMenu();

        int userInput;
        do
        {
            userInput = scanner.nextInt();

            switch (userInput){
                case 1:
                    readMonthlyReports();
                    logicaAMR();
                    printMenu();
                    break;
                case 2:
                    readYearlyReports();
                    logicaYR();
                    printMenu();
                    break;
                case 3:
                    if (Main.logica_amr == "allMonthlyReports доступен" && Main.logica_yr == "yearReport доступен") {
                        reconciliationOfReports();
                    }
                    printMenu();
                    break;
                case 4:
                    if (Main.logica_amr == "allMonthlyReports доступен") {
                        allMonthlyReports();
                    }
                    printMenu();
                    break;
                case 5:
                    if (Main.logica_yr == "yearReport доступен") {
                        yearReport();
                    }
                    printMenu();
                    break;
                case 0:
                    System.out.println("Выход");
                    return;
                default:
                    System.out.println("Извините, такой команды нет.");
                    printMenu();
                    break;
            }
        }
        while (true);
    }

    private static void logicaAMR() {
        Main.logica_amr = "allMonthlyReports доступен";
    }

    private static void logicaYR() {
        Main.logica_yr = "yearReport доступен";
    }

    private static void printMenu() {
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Считать все месячные отчёты");
        System.out.println("2 - Считать годовой отчёт");
        System.out.println("3 - Сверить отчёты");
        System.out.println("4 - Вывести информацию о всех месячных отчётах");
        System.out.println("5 - Вывести информацию о годовом отчёте");
        System.out.println("0 - Выход");
    }


    private static void readMonthlyReports() throws Exception {

        Path fileName1 = filePath1.getFileName();
        System.out.println(fileName1);

        Path fileName2 = filePath2.getFileName();
        System.out.println(fileName2);

        Path fileName3 = filePath3.getFileName();
        System.out.println(fileName3);
    }

    private static void readYearlyReports() throws Exception {

        Path fileName = filePath.getFileName();
        System.out.println(fileName);
    }


    public static void oneMonthlyReports() throws Exception {
        Parser parser = new Parser(",");

        Path fileName1 = filePath1.getFileName();
        System.out.println(fileName1);
        System.out.println("------------");

        List <MonthlyReport> items = parser.load(new FileInputStream(new File(String.valueOf(filePath1))), MonthlyReport.class);
        OptionalInt maxProfit1 = items.stream().filter(p->p.getIsExpense() == false).mapToInt(MonthlyReport:: getTotal).max();
        items.stream().filter(p->p.getIsExpense() == false).filter(p->p.getTotal() == maxProfit1.orElseThrow()).forEach(p->System.out.println("Самый прибыльный товар | " + p.getName() + " | " + maxProfit1.orElseThrow()));


        OptionalInt maxExpenses1 = items.stream().filter(p->p.getIsExpense() == true).mapToInt(MonthlyReport:: getTotal).max();
        items.stream().filter(p->p.getIsExpense() == true).filter(p->p.getTotal() == maxExpenses1.orElseThrow()).forEach(p->System.out.println("Самый большая трата | " + p.getName() + " | " + maxExpenses1.orElseThrow()));
        System.out.println("--------------------------------------------------");
    }

    private static void reconciliationOfReports() throws Exception {
        Parser parser = new Parser(",");

        List<MonthlyReport> items1 = parser.load(new FileInputStream(new File(String.valueOf(filePath1))), MonthlyReport.class);
        int sumProfit1 = items1.stream().filter(p -> p.getIsExpense() == false).mapToInt(MonthlyReport::getTotal).sum();
        int sumExpenses1 = items1.stream().filter(p -> p.getIsExpense() == true).mapToInt(MonthlyReport::getTotal).sum();

        List<MonthlyReport> items2 = parser.load(new FileInputStream(new File(String.valueOf(filePath2))), MonthlyReport.class);
        int sumProfit2 = items2.stream().filter(p -> p.getIsExpense() == false).mapToInt(MonthlyReport::getTotal).sum();
        int sumExpenses2 = items2.stream().filter(p -> p.getIsExpense() == true).mapToInt(MonthlyReport::getTotal).sum();

        List<MonthlyReport> items3 = parser.load(new FileInputStream(new File(String.valueOf(filePath3))), MonthlyReport.class);
        int sumProfit3 = items3.stream().filter(p -> p.getIsExpense() == false).mapToInt(MonthlyReport::getTotal).sum();
        int sumExpenses3 = items3.stream().filter(p -> p.getIsExpense() == true).mapToInt(MonthlyReport::getTotal).sum();

        System.out.println("Месячный отчет | Общие доходы  |  январь: " + sumProfit1 + " |  февраль: " + sumProfit2 + " |  март: " + sumProfit3);
        System.out.println("Месячный отчет | Общие расходы |  январь: " + sumExpenses1 + " |  февраль: " + sumExpenses2 + " |  март: " + sumExpenses3);
        System.out.println("-------------------------------------------------------------------");


        List<YearlyReport> items = parser.load(new FileInputStream(new File(String.valueOf(filePath))), YearlyReport.class);
        int sumYearProfit1 = items.stream().filter(p -> p.getIsExpense() == false).filter(p -> p.getMonth() == 01).mapToInt(YearlyReport::getAmount).sum();
        int sumYearExpenses1 = items.stream().filter(p -> p.getIsExpense() == true).filter(p -> p.getMonth() == 01).mapToInt(YearlyReport::getAmount).sum();

        int sumYearProfit2 = items.stream().filter(p -> p.getIsExpense() == false).filter(p -> p.getMonth() == 02).mapToInt(YearlyReport::getAmount).sum();
        int sumYearExpenses2 = items.stream().filter(p -> p.getIsExpense() == true).filter(p -> p.getMonth() == 02).mapToInt(YearlyReport::getAmount).sum();

        int sumYearProfit3 = items.stream().filter(p -> p.getIsExpense() == false).filter(p -> p.getMonth() == 03).mapToInt(YearlyReport::getAmount).sum();
        int sumYearExpenses3 = items.stream().filter(p -> p.getIsExpense() == true).filter(p -> p.getMonth() == 03).mapToInt(YearlyReport::getAmount).sum();

        System.out.println("Годовой отчет | Доход  |  Январь: " + sumYearProfit1 + "  |  февраль: " + sumYearProfit2 + "  |  март: " + sumYearProfit3);
        System.out.println("Годовой отчет | Расход |  Январь: " + sumYearExpenses1 + "  |  февраль: " + sumYearExpenses2 + "  |  март: " + sumYearExpenses3);
        System.out.println("-------------------------------------------------------------------");
        System.out.println("** При успешной сверке отчетов вывести '0'");
        System.out.println("Сверка отчетов | Доход  |  Январь: " + (sumYearProfit1 - sumProfit1) + "  |  февраль: " + (sumYearProfit2 - sumProfit2) + "  |  март: " + (sumYearProfit3 - sumProfit3));
        System.out.println("Сверка отчетов | Доход  |  Январь: " + (sumYearExpenses1 - sumExpenses1) + "  |  февраль: " + (sumYearExpenses2 - sumExpenses2) + "  |  март: " + (sumYearExpenses3 - sumExpenses3));
    }


    private static void thoMonthlyReports() throws Exception {
        Parser parser = new Parser(",");
        Path fileName2 = filePath2.getFileName();
        System.out.println(fileName2);
        System.out.println("------------");

        List <MonthlyReport> items1 = parser.load(new FileInputStream(new File(String.valueOf(filePath2))), MonthlyReport.class);
        OptionalInt maxProfit1 = items1.stream().filter(p->p.getIsExpense() == false).mapToInt(MonthlyReport:: getTotal).max();
        items1.stream().filter(p->p.getIsExpense() == false).filter(p->p.getTotal() == maxProfit1.orElseThrow()).forEach(p->System.out.println("Самый прибыльный товар | " + p.getName() + " | " + maxProfit1.orElseThrow()));

        OptionalInt maxExpenses1 = items1.stream().filter(p->p.getIsExpense() == true).mapToInt(MonthlyReport:: getTotal).max();
        items1.stream().filter(p->p.getIsExpense() == true).filter(p->p.getTotal() == maxExpenses1.orElseThrow()).forEach(p->System.out.println("Самый большая трата | " + p.getName() + " | " + maxExpenses1.orElseThrow()));
        System.out.println("--------------------------------------------------");
    }

    private static void threeMonthlyReports() throws Exception {
        Parser parser = new Parser(",");
        Path fileName3 = filePath3.getFileName();
        System.out.println(fileName3);
        System.out.println("------------");

        List <MonthlyReport> items1 = parser.load(new FileInputStream(new File(String.valueOf(filePath3))), MonthlyReport.class);
        OptionalInt maxProfit1 = items1.stream().filter(p->p.getIsExpense() == false).mapToInt(MonthlyReport:: getTotal).max();
        items1.stream().filter(p->p.getIsExpense() == false).filter(p->p.getTotal() == maxProfit1.orElseThrow()).forEach(p->System.out.println("Самый прибыльный товар | " + p.getName() + " | " + maxProfit1.orElseThrow()));

        OptionalInt maxExpenses1 = items1.stream().filter(p->p.getIsExpense() == true).mapToInt(MonthlyReport:: getTotal).max();
        items1.stream().filter(p->p.getIsExpense() == true).filter(p->p.getTotal() == maxExpenses1.orElseThrow()).forEach(p->System.out.println("Самый большая трата | " + p.getName() + " | " + maxExpenses1.orElseThrow()));
        System.out.println("--------------------------------------------------");
    }

    private static void allMonthlyReports() throws Exception {
        oneMonthlyReports();
        thoMonthlyReports();
        threeMonthlyReports();
    }

    private static void yearReport() throws Exception {
        Parser parser = new Parser(",");
        Path fileName = filePath.getFileName();
        System.out.println(fileName);
        System.out.println("------------");

        List <YearlyReport> items = parser.load(new FileInputStream(new File(String.valueOf(filePath))), YearlyReport.class);
        int sumProfit1 = items.stream().filter(p->p.getIsExpense() == false).filter(p->p.getMonth() == 01).mapToInt(YearlyReport:: getAmount).sum();
        int sumExpenses1 = items.stream().filter(p->p.getIsExpense() == true).filter(p->p.getMonth() == 01).mapToInt(YearlyReport:: getAmount).sum();
        System.out.println("Прибыль за Январь | " + (sumProfit1 - sumExpenses1));
        System.out.println("--------------------------------------------------");


        int sumProfit2 = items.stream().filter(p->p.getIsExpense() == false).filter(p->p.getMonth() == 02).mapToInt(YearlyReport:: getAmount).sum();
        int sumExpenses2 = items.stream().filter(p->p.getIsExpense() == true).filter(p->p.getMonth() == 02).mapToInt(YearlyReport:: getAmount).sum();
        System.out.println("Прибыль за Февраль | " + (sumProfit2 - sumExpenses2));
        System.out.println("--------------------------------------------------");

        int sumProfit3 = items.stream().filter(p->p.getIsExpense() == false).filter(p->p.getMonth() == 03).mapToInt(YearlyReport:: getAmount).sum();
        int sumExpenses3 = items.stream().filter(p->p.getIsExpense() == true).filter(p->p.getMonth() == 03).mapToInt(YearlyReport:: getAmount).sum();
        System.out.println("Прибыль за Март | " + (sumProfit3 - sumExpenses3));
        System.out.println("--------------------------------------------------");

        int averageProfit = (sumProfit1 + sumProfit2 + sumProfit3) / 3;
        int averageExpenses = (sumExpenses1 + sumExpenses2 + sumExpenses3) / 3;
        System.out.println("Средний расход за все месяцы в году | " + averageExpenses);
        System.out.println("Средний доход за все месяцы в году | " + averageProfit);
        System.out.println("--------------------------------------------------");
    }
}