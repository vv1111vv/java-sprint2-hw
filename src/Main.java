


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    public static String logica_amr;
    public static String logica_yr;
    public static String logica_M;
    public static String logica_Y;


    public static void main(String[] args) throws Exception {
        Main.logica_amr = "Доступ закрыт";
        Main.logica_yr = "Доступ закрыт";
        Main.logica_M = "Проверка наличия файлов";
        Main.logica_Y = "Проверка наличия файлов";

        Path path = Paths.get("resources");
        List<Path> paths = listFiles(path);
        HashMap<Integer, ArrayList<Integer>> mapMonthlyReport = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> mapAnnualReport = new HashMap<>();
        printMenu();

        int userInput;
        do
        {
            userInput = scanner.nextInt();

            switch (userInput){
                case 1:
                    readMonthlyReports(paths);
                    logicaAMR();
                    howManyFiles(paths);
                    printMenu();
                    break;
                case 2:
                    readYearlyReports(paths);
                    logicaYR();
                    printMenu();
                    break;
                case 3:
                    if (Main.logica_M.equals("Файлы отсутствуют")) {
                        System.out.println("Файлы отсутствуют");
                    }
                    else if (Main.logica_M.equals("Недостаточно файлов")) {
                        System.out.println("Недостаточно файлов");
                    }
                    else if (Main.logica_amr.equals("allMonthlyReports доступен") && Main.logica_yr.equals("yearReport доступен")) {
                        reconciliationOfReports(paths, mapMonthlyReport, mapAnnualReport);

                    } else {
                        System.out.println("** Для сверки необходимо считать все отчёты");
                    }
                    printMenu();
                    break;
                case 4:
                    if (Main.logica_M.equals("Файлы отсутствуют")) {
                        System.out.println("Файлы отсутствуют");
                    }
                    else if (Main.logica_amr.equals("allMonthlyReports доступен")) {
                        allMonthlyReports(paths);
                    }
                    else if (Main.logica_M.equals("Недостаточно файлов для сверки")) {
                        System.out.println("Недостаточно файлов для сверки");
                    } else {
                        System.out.println("** Необходимо считать все месячные отчёты");
                    }
                    printMenu();
                    break;
                case 5:
                    if (Main.logica_Y.equals("Файлы отсутствуют")) {
                        System.out.println("Файлы отсутствуют");
                    }
                    else if (Main.logica_yr.equals("yearReport доступен")) {
                        yearReport(paths);
                    } else {
                        System.out.println("** Необходимо считать годовой отчёт");
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

    public static List<Path> listFiles(Path path) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
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


    private static void readMonthlyReports(List<Path> paths) throws Exception {

        paths = paths.stream().filter(p -> !p.toString().contains("y") && Files.isRegularFile(p))
                .collect(Collectors.toList());
        if (paths.size() == 0) {
            System.out.println("Файлы отсутствуют");
            logica_M = "Файлы отсутствуют";
        } else {

            for(int i=0; i < paths.size();  i++) {
                Path fileName1 = paths.get(i).getFileName();
                System.out.println(fileName1);
            }
        }
    }

    private static void readYearlyReports(List<Path> paths) throws Exception {

        paths = paths.stream().filter(p -> p.toString().contains("y") && Files.isRegularFile(p))
                .collect(Collectors.toList());
        if (paths.size() == 0) {
            System.out.println("Файлы отсутствуют");
            logica_Y = "Файлы отсутствуют";
        } else {

        Path fileName = paths.get(0).getFileName();
        System.out.println(fileName);
        }
    }


    public static void oneMonthlyReports(List<Path> paths, int i) throws Exception {
        Parser parser = new Parser(",");

        Path fileName1 = paths.get(i).getFileName();
        System.out.println(fileName1);
        System.out.println("------------");

        List <MonthlyReport> items = parser.load(new FileInputStream(new File(String.valueOf(paths.get(i)))), MonthlyReport.class);
        OptionalInt maxProfit1 = items.stream().filter(p->!p.getIsExpense()).mapToInt(MonthlyReport:: getTotal).max();
        items.stream().filter(p-> !p.getIsExpense()).filter(p->p.getTotal() == maxProfit1.orElseThrow()).forEach(p->System.out.println("Самый прибыльный товар | " + p.getName() + " | " + maxProfit1.orElseThrow()));


        OptionalInt maxExpenses1 = items.stream().filter(p-> p.getIsExpense()).mapToInt(MonthlyReport:: getTotal).max();
        items.stream().filter(p-> p.getIsExpense()).filter(p->p.getTotal() == maxExpenses1.orElseThrow()).forEach(p->System.out.println("Самый большая трата | " + p.getName() + " | " + maxExpenses1.orElseThrow()));
        System.out.println("--------------------------------------------------");
    }

    private static void reconciliationOfReports(List<Path> paths, HashMap<Integer, ArrayList<Integer>> mapMonthlyReport, HashMap<Integer, ArrayList<Integer>> mapAnnualReport) throws Exception {
        monthlyReport(paths, mapMonthlyReport);
        annualReport(paths, mapAnnualReport);
        printReconciliationOfReports(mapMonthlyReport, mapAnnualReport);
    }

    private static void printReconciliationOfReports(HashMap<Integer, ArrayList<Integer>> mapMonthlyReport, HashMap<Integer, ArrayList<Integer>> mapAnnualReport) {

        System.out.println("Месячный отчет | Общие доходы  |  январь: " + mapMonthlyReport.get(0).get(0) + " |  февраль: " + mapMonthlyReport.get(1).get(0) + " |  март: " + mapMonthlyReport.get(2).get(0));
        System.out.println("Месячный отчет | Общие расходы |  январь: " + mapMonthlyReport.get(0).get(1) + " |  февраль: " + mapMonthlyReport.get(1).get(1) + " |  март: " + mapMonthlyReport.get(2).get(1));
        System.out.println("-------------------------------------------------------------------");
        System.out.println("Годовой отчет | Общие доходы  |  январь: " + mapAnnualReport.get(0).get(0) + " |  февраль: " + mapAnnualReport.get(0).get(2) + " |  март: " + mapAnnualReport.get(0).get(4));
        System.out.println("Годовой отчет | Общие расходы |  январь: " + mapAnnualReport.get(0).get(1) + " |  февраль: " + mapAnnualReport.get(0).get(3) + " |  март: " + mapAnnualReport.get(0).get(5));
        System.out.println("-------------------------------------------------------------------");
        System.out.println("** При успешной сверке отчетов вывести '0'");
        System.out.println("Сверка отчетов | Доход  |  Январь: " + (mapAnnualReport.get(0).get(0) - mapMonthlyReport.get(0).get(0)) + "  |  февраль: " + (mapAnnualReport.get(0).get(2) - mapMonthlyReport.get(1).get(0)) + "  |  март: " + (mapAnnualReport.get(0).get(4) - mapMonthlyReport.get(2).get(0)));
        System.out.println("Сверка отчетов | Расход |  Январь: " + (mapAnnualReport.get(0).get(1) - mapMonthlyReport.get(0).get(1)) + "  |  февраль: " + (mapAnnualReport.get(0).get(3) - mapMonthlyReport.get(1).get(1)) + "  |  март: " + (mapAnnualReport.get(0).get(5) - mapMonthlyReport.get(2).get(1)));
    }

    private static void monthlyReport(List<Path> paths, HashMap<Integer, ArrayList<Integer>> mapMonthlyReport) throws Exception {
        Parser parser = new Parser(",");

        paths = paths.stream().filter(p -> p.toString().contains("m") && Files.isRegularFile(p))
                .collect(Collectors.toList());

        for (int i = 0; i < paths.size(); i++) {

            ArrayList<Integer> mapMR = new ArrayList<>();
            List<MonthlyReport> itemsx = parser.load(new FileInputStream(new File(String.valueOf(paths.get(i)))), MonthlyReport.class);
            mapMR.add(itemsx.stream().filter(p -> !p.getIsExpense()).mapToInt(MonthlyReport::getTotal).sum());
            mapMR.add(itemsx.stream().filter(p -> p.getIsExpense()).mapToInt(MonthlyReport::getTotal).sum());
            mapMonthlyReport.put(i, mapMR);
        }
    }


    private static void annualReport(List<Path> paths, HashMap<Integer, ArrayList<Integer>> mapAnnualReport) throws Exception {
        Parser parser = new Parser(",");
        paths = paths.stream().filter(p -> p.toString().contains("y") && Files.isRegularFile(p))
                .collect(Collectors.toList());

        for(int i=0; i < paths.size();  i++) {

            ArrayList<Integer> mapAR = new ArrayList<>();
            List<YearlyReport> items = parser.load(new FileInputStream(new File(String.valueOf(paths.get(i)))), YearlyReport.class);
            mapAR.add(items.stream().filter(p -> !p.getIsExpense()).filter(p -> p.getMonth() == 01).mapToInt(YearlyReport::getAmount).sum()); // Доход
            mapAR.add(items.stream().filter(p -> p.getIsExpense()).filter(p -> p.getMonth() == 01).mapToInt(YearlyReport::getAmount).sum()); // Расход
            mapAR.add(items.stream().filter(p -> !p.getIsExpense()).filter(p -> p.getMonth() == 02).mapToInt(YearlyReport::getAmount).sum()); // Доход
            mapAR.add(items.stream().filter(p -> p.getIsExpense()).filter(p -> p.getMonth() == 02).mapToInt(YearlyReport::getAmount).sum()); // Расход
            mapAR.add(items.stream().filter(p -> !p.getIsExpense()).filter(p -> p.getMonth() == 03).mapToInt(YearlyReport::getAmount).sum()); // Доход
            mapAR.add(items.stream().filter(p -> p.getIsExpense()).filter(p -> p.getMonth() == 03).mapToInt(YearlyReport::getAmount).sum()); // Расход
            mapAnnualReport.put(i, mapAR);
        }
    }

    private static void howManyFiles(List<Path> paths) throws Exception {
        paths = paths.stream().filter(p -> p.toString().contains("m") && Files.isRegularFile(p))
                .collect(Collectors.toList());
        if (paths.size() < 3) {
            logica_M = "Недостаточно файлов";
            logica_amr = "";
        }
    }

    private static void allMonthlyReports(List<Path> paths) throws Exception {

        paths = paths.stream().filter(p -> p.toString().contains("m") && Files.isRegularFile(p))
                .collect(Collectors.toList());

        for(int i=0; i < paths.size();  i++) {
                oneMonthlyReports(paths, i);
        }
    }


    private static void yearReport(List<Path> paths) throws Exception {
        Parser parser = new Parser(",");

        paths = paths.stream().filter(p -> p.toString().contains("y") && Files.isRegularFile(p))
                .collect(Collectors.toList());

        Path fileName = paths.get(0).getFileName();
        System.out.println(fileName);
        System.out.println("------------");

        List <YearlyReport> items = parser.load(new FileInputStream(new File(String.valueOf(paths.get(0)))), YearlyReport.class);
        int sumProfit1 = items.stream().filter(p-> !p.getIsExpense()).filter(p->p.getMonth() == 01).mapToInt(YearlyReport:: getAmount).sum();
        int sumExpenses1 = items.stream().filter(p-> p.getIsExpense()).filter(p->p.getMonth() == 01).mapToInt(YearlyReport:: getAmount).sum();
        System.out.println("Прибыль за Январь | " + (sumProfit1 - sumExpenses1));
        System.out.println("--------------------------------------------------");


        int sumProfit2 = items.stream().filter(p-> !p.getIsExpense()).filter(p->p.getMonth() == 02).mapToInt(YearlyReport:: getAmount).sum();
        int sumExpenses2 = items.stream().filter(p-> p.getIsExpense()).filter(p->p.getMonth() == 02).mapToInt(YearlyReport:: getAmount).sum();
        System.out.println("Прибыль за Февраль | " + (sumProfit2 - sumExpenses2));
        System.out.println("--------------------------------------------------");

        int sumProfit3 = items.stream().filter(p-> !p.getIsExpense()).filter(p->p.getMonth() == 03).mapToInt(YearlyReport:: getAmount).sum();
        int sumExpenses3 = items.stream().filter(p-> p.getIsExpense()).filter(p->p.getMonth() == 03).mapToInt(YearlyReport:: getAmount).sum();
        System.out.println("Прибыль за Март | " + (sumProfit3 - sumExpenses3));
        System.out.println("--------------------------------------------------");

        int averageProfit = (sumProfit1 + sumProfit2 + sumProfit3) / 3;
        int averageExpenses = (sumExpenses1 + sumExpenses2 + sumExpenses3) / 3;
        System.out.println("Средний расход за все месяцы в году | " + averageExpenses);
        System.out.println("Средний доход за все месяцы в году | " + averageProfit);
        System.out.println("--------------------------------------------------");
    }
}