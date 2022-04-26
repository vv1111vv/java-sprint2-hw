


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import java.util.Comparator;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    public static String logica_M;
    public static String logica_Y;


    public static void main(String[] args) throws Exception {

        Main.logica_M = "Проверка наличия файлов";
        Main.logica_Y = "Проверка наличия файлов";

        Path path = Paths.get("resources");
        List<Path> paths = listFiles(path);
        HashMap<Integer, ArrayList<Integer>> mapMonthlyReport = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> mapAnnualReport = new HashMap<>();
        HashMap<Integer, ArrayList<MonthlyReport>> mapMonthlyReportMaxSumOfOne = new HashMap<>();
        HashMap<Integer, ArrayList<MonthlyReport>> mapMonthlyReportMaxQuantity = new HashMap<>();

        printMenu();

        int userInput;
        do
        {
            userInput = scanner.nextInt();

            switch (userInput){
                case 1:
                    readMonthlyReports(paths, mapMonthlyReport, mapMonthlyReportMaxSumOfOne, mapMonthlyReportMaxQuantity);
                    printMenu();
                    break;
                case 2:
                    readYearlyReports(paths, mapAnnualReport);
                    printMenu();
                    break;
                case 3:
                    if (Main.logica_M.equals("Файлы считаны") && Main.logica_Y.equals("Файлы считаны")) {
                        printReconciliationOfReports(mapMonthlyReport, mapAnnualReport);
                    } else {
                        System.out.println("** Для сверки необходимо считать все отчёты");
                    }
                    printMenu();
                    break;
                case 4:
                    if (Main.logica_M.equals("Файлы считаны")) {
                        printMonthlyReports(paths, mapMonthlyReportMaxSumOfOne, mapMonthlyReportMaxQuantity);
                    } else {
                        System.out.println("** Необходимо считать все месячные отчёты");
                    }
                    printMenu();
                    break;
                case 5:
                    if (Main.logica_Y.equals("Файлы считаны")) {
                        printYearReport(paths, mapAnnualReport);
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

    private static List<Path> listFiles(Path path) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
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


    private static void readMonthlyReports(List<Path> paths, HashMap<Integer, ArrayList<Integer>> mapMonthlyReport,
                                          HashMap<Integer, ArrayList<MonthlyReport>> mapMonthlyReportMaxSumOfOne,
                                          HashMap<Integer, ArrayList<MonthlyReport>> mapMonthlyReportMaxQuantity) throws Exception {

        paths = paths.stream().filter(p -> p.toString().contains("m") && Files.isRegularFile(p))
                .collect(Collectors.toList());
        if (paths.size() == 0) {
            System.out.println("Файлы отсутствуют");
            logica_M = "Файлы отсутствуют";
        } else if (paths.size() < 3) {
            System.out.println("Недостаточно файлов");
            logica_M = "Недостаточно файлов";
        } else {
            logica_M = "Файлы считаны";
            Parser parser = new Parser(",");
            for(int i=0; i < paths.size();  i++) {
                Path fileName1 = paths.get(i).getFileName();
                System.out.println(fileName1);

                ArrayList<Integer> mapMR = new ArrayList<>();
                List<MonthlyReport> items = parser.load(new FileInputStream(new File(String.valueOf(paths.get(i)))), MonthlyReport.class);
                mapMR.add(items.stream().filter(p -> !p.getIsExpense()).mapToInt(MonthlyReport :: getTotal).sum());
                mapMR.add(items.stream().filter(p -> p.getIsExpense()).mapToInt(MonthlyReport :: getTotal).sum());
                mapMonthlyReport.put(i, mapMR);

                ArrayList<MonthlyReport> mapMaxSumOfOne = new ArrayList<>();
                mapMaxSumOfOne.add(items.stream().filter(p -> !p.getIsExpense()).sorted(Comparator.comparing(MonthlyReport::getTotal, Comparator.reverseOrder())).findFirst().orElse(null));
                mapMonthlyReportMaxSumOfOne.put(i, mapMaxSumOfOne);

                ArrayList<MonthlyReport> mapMaxQuantity = new ArrayList<>();
                mapMaxQuantity.add(items.stream().filter(p -> p.getIsExpense()).sorted(Comparator.comparing(MonthlyReport::getTotal, Comparator.reverseOrder())).findFirst().orElse(null));
                mapMonthlyReportMaxQuantity.put(i, mapMaxQuantity);
            }
        }
    }


    private static void readYearlyReports(List<Path> paths, HashMap<Integer, ArrayList<Integer>> mapAnnualReport) throws Exception {
        paths = paths.stream().filter(p -> p.toString().contains("y") && Files.isRegularFile(p))
                .collect(Collectors.toList());
        if (paths.size() == 0) {
            System.out.println("Файлы отсутствуют");
            logica_Y = "Файлы отсутствуют";
        } else {
            logica_Y = "Файлы считаны";
            Path fileName = paths.get(0).getFileName();
            System.out.println(fileName);

            Parser parser = new Parser(",");

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
    }


    private static void printMonthlyReports(List<Path> paths, HashMap<Integer, ArrayList<MonthlyReport>> mapMonthlyReportMaxSumOfOne,
                                         HashMap<Integer, ArrayList<MonthlyReport>> mapMonthlyReportMaxQuantity) {

        System.out.println(paths.get(0).getFileName());
        System.out.println("------------");
        System.out.println("Самый прибыльный товар " + mapMonthlyReportMaxSumOfOne.get(0));
        System.out.println("Самый большая трата " + mapMonthlyReportMaxQuantity.get(0));
        System.out.println("-------------------------------------------------------------------");
        System.out.println(paths.get(1).getFileName());
        System.out.println("------------");
        System.out.println("Самый прибыльный товар " + mapMonthlyReportMaxSumOfOne.get(1));
        System.out.println("Самый большая трата " + mapMonthlyReportMaxQuantity.get(1));
        System.out.println("-------------------------------------------------------------------");
        System.out.println(paths.get(2).getFileName());
        System.out.println("------------");
        System.out.println("Самый прибыльный товар " + mapMonthlyReportMaxSumOfOne.get(2));
        System.out.println("Самый большая трата " + mapMonthlyReportMaxQuantity.get(2));
        System.out.println("-------------------------------------------------------------------");
    }


    private static void printReconciliationOfReports(HashMap<Integer, ArrayList<Integer>> mapMonthlyReport,
                                                     HashMap<Integer, ArrayList<Integer>> mapAnnualReport) {

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


    private static void printYearReport(List<Path> paths, HashMap<Integer, ArrayList<Integer>> mapAnnualReport) {

        paths = paths.stream().filter(p -> p.toString().contains("y") && Files.isRegularFile(p))
                .collect(Collectors.toList());

        Path fileName = paths.get(0).getFileName();
        System.out.println(fileName);
        System.out.println("------------");

        System.out.println("Прибыль за Январь | " + (mapAnnualReport.get(0).get(0) - mapAnnualReport.get(0).get(1)));
        System.out.println("--------------------------------------------------");
        System.out.println("Прибыль за Февраль | " + (mapAnnualReport.get(0).get(2) - mapAnnualReport.get(0).get(3)));
        System.out.println("--------------------------------------------------");
        System.out.println("Прибыль за Март | " + (mapAnnualReport.get(0).get(4) - mapAnnualReport.get(0).get(5)));
        System.out.println("--------------------------------------------------");

        int averageProfit = (mapAnnualReport.get(0).get(0) + mapAnnualReport.get(0).get(2) + mapAnnualReport.get(0).get(4)) / 3;
        int averageExpenses = (mapAnnualReport.get(0).get(1) + mapAnnualReport.get(0).get(3) + mapAnnualReport.get(0).get(5)) / 3;
        System.out.println("Средний расход за все месяцы в году | " + averageExpenses);
        System.out.println("Средний доход за все месяцы в году | " + averageProfit);
        System.out.println("--------------------------------------------------");
    }
}