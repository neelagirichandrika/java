

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class StockRecord {
    String name;
    ArrayList<Double> prices = new ArrayList<>();

    StockRecord(String name, double m1, double m2, double m3) {
        this.name = name;
        prices.add(m1);
        prices.add(m2);
        prices.add(m3);
    }
}

public class eg {

    public static double average(ArrayList<Double> list) {
        double sum = 0;
        for (double d : list) sum += d;
        return list.isEmpty() ? 0 : sum / list.size();
    }

    public static double forecast(double avg) {
        return avg * 1.02; 
    }

    public static String profitOrLoss(double lastPrice, double forecast) {
        if (forecast > lastPrice) return "Profit";
        else if (forecast < lastPrice) return "Loss";
        else return "No Change";
    }

    public static void writeCsv(ArrayList<StockRecord> stocks) {
        File csvFile = new File("stocks.csv");
        try (FileWriter writer = new FileWriter(csvFile, false)) {
            writer.write("Stock,Prices...,Average,Forecast,Profit/Loss\n");
            for (StockRecord s : stocks) {
                double avg = average(s.prices);
                double fc = forecast(avg);
                double last = s.prices.get(s.prices.size() - 1);
                String status = profitOrLoss(last, fc);

                writer.write(s.name);
                for (double p : s.prices) {
                    writer.write("," + p);
                }
                writer.write("," + String.format("%.2f", avg) +
                             "," + String.format("%.2f", fc) +
                             "," + status + "\n");
            }
            System.out.println("CSV updated: stocks.csv");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    public static void showAll(ArrayList<StockRecord> stocks) {
        if (stocks.isEmpty()) {
            System.out.println("No stocks available.");
            return;
        }
        System.out.println("\n--- STOCK DETAILS ---");
        for (StockRecord s : stocks) {
            double avg = average(s.prices);
            double fc = forecast(avg);
            double last = s.prices.get(s.prices.size() - 1);
            String status = profitOrLoss(last, fc);

            System.out.println("Stock: " + s.name);
            System.out.println("Prices: " + s.prices);
            System.out.println("Average: " + String.format("%.2f", avg) +
                               ", Forecast: " + String.format("%.2f", fc) +
                               ", Result: " + status);
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<StockRecord> stocks = new ArrayList<>();
        int choice;

        do {
            System.out.println("\n===== STOCK MENU =====");
            System.out.println("1. Add new stock");
            System.out.println("2. Add a month price to existing stock");
            System.out.println("3. Show all details");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1: 
                    System.out.print("Number of stocks");
                    int n = sc.nextInt();
                    sc.nextLine();
                    for (int i = 1; i <= n; i++) {
                        System.out.println("\nStock " + i);
                        System.out.print("Enter stock name: ");
                        String name = sc.nextLine().trim();

                        System.out.print("Enter Month 1 price: ");
                        double m1 = sc.nextDouble();
                        System.out.print("Enter Month 2 price: ");
                        double m2 = sc.nextDouble();
                        System.out.print("Enter Month 3 price: ");
                        double m3 = sc.nextDouble();
                        sc.nextLine();

                        stocks.add(new StockRecord(name, m1, m2, m3));
                    }
                    writeCsv(stocks);
                    break;

                case 2:  
                    if (stocks.isEmpty()) {
                        System.out.println("No stocks yet. Add a stock first.");
                        break;
                    }
                    System.out.print("Enter existing stock name: ");
                    String sname = sc.nextLine().trim();
                    StockRecord found = null;
                    for (StockRecord s : stocks) {
                        if (s.name.equalsIgnoreCase(sname)) {
                            found = s;
                            break;
                        }
                    }
                    if (found == null) {
                        System.out.println("Stock not found.");
                    } else {
                        System.out.print("Enter new month price: ");
                        double newPrice = sc.nextDouble();
                        sc.nextLine();
                        found.prices.add(newPrice);
                        System.out.println("Price added to " + found.name);
                        writeCsv(stocks);
                    }
                    break;

                case 3: 
                    showAll(stocks);
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);
    }
}