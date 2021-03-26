import java.io.BufferedReader;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class PrintReceipt {

    private static float getTax(TaxRate taxRate, List<Item> items) {
        float totalTax = 0;
        List<String> foodList = getItemList("food.csv");
        List<String> clothingList = getItemList("clothing.csv");
        System.out.println(taxRate.isExemptClothing());
        System.out.println(taxRate.isExemptFood());
        System.out.println(taxRate.getState());

        for (Item item : items) {
            // if not exempting the item
            if (!((taxRate.isExemptFood() && foodList.contains(item.getName().toLowerCase()))
                    || (taxRate.isExemptClothing() && clothingList.contains(item.getName().toLowerCase())))) {
                totalTax += item.getPrice() * item.getQuantity() * taxRate.getTaxRate();

            }
        }
        // round up to nearest 0.05
        return (float) (Math.ceil(totalTax * 20.0) / 20.0);

    }

    private static List<TaxRate> getTaxRates() {
        String taxRateCsvPath = "tax_rates.csv";
        List<TaxRate> taxRates = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(taxRateCsvPath));
            // skip headers
            csvReader.readLine();
            String row = "";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String state = data[0];
                float rate = Float.parseFloat(data[1]) / 100;
                boolean exemptFood = Boolean.parseBoolean(data[2]);
                boolean exemptClothing = Boolean.parseBoolean(data[3]);
                taxRates.add(new TaxRate(state, rate, exemptFood, exemptClothing));
            }
            csvReader.close();
        } catch (IOException e) {

        }
        return taxRates;

    }

    private static List<String> getItemList(String path) {
        List<String> exemptions = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(path));
            // skip headers
            csvReader.readLine();
            String row = "";
            while ((row = csvReader.readLine()) != null) {
                exemptions.add(row.toLowerCase());
            }
            csvReader.close();
        } catch (IOException e) {

        }
        return exemptions;

    }

    private static String readInput() {
        String path = "input.txt";
        String input = "";
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(path));
            input = csvReader.readLine();
            csvReader.close();
        } catch (IOException e) {

        }
        return input;

    }

    private static void createReceipt(List<Item> itemList, float tax) {

        try {
            FileWriter writer = new FileWriter("receipt.txt");
            String line = String.format("item                %s         %s\n", "price", "qty");
            System.out.printf(line);
            writer.write(line);
            float subtotal = 0;
            for (Item item : itemList) {
                line = String.format("%-15s %9s %11d\n", item.getName(), String.format("$%.2f", item.getPrice()),
                        item.getQuantity());
                System.out.printf(line);
                writer.write(line);
                subtotal += item.getPrice();
            }
            line = String.format("subtotal:%28s\n", String.format("$%.2f", subtotal));
            System.out.printf(line);
            writer.write(line);
            line = String.format("tax:%33s\n", String.format("$%.2f", tax));
            System.out.printf(line);
            writer.write(line);
            line = String.format("total:%31s\n", String.format("$%.2f", subtotal + tax));
            System.out.printf(line);
            writer.write(line);
            writer.close();
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) {
        List<TaxRate> taxrates = getTaxRates();

        String input = readInput();

        Scanner scanner = new Scanner(input.replace(",", ""));

        scanner.next();

        List<Item> itemList = new ArrayList<>();

        String state = scanner.next().toUpperCase();

        TaxRate stateTaxRate = taxrates.stream().filter(taxRate -> state.equals(taxRate.getState())).findAny()
                .orElse(null);

        if (stateTaxRate != null) {
            while (scanner.hasNext()) {
                int quantity = scanner.nextInt();
                String name = scanner.next();
                String next = scanner.next();
                while (!"at".equals(next)) {
                    name += " " + next;
                    next = scanner.next();
                }
                float price = scanner.nextFloat();
                Item item = new Item(name, quantity, price);
                itemList.add(item);
            }
            float tax = getTax(stateTaxRate, itemList);
            createReceipt(itemList, tax);

            scanner.close();
        }

    }
}

// Tax Rate Object
class TaxRate {
    private String state;
    private float taxRate;
    private boolean exemptFood;
    private boolean exemptClothing;

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(float taxRate) {
        this.taxRate = taxRate;
    }

    public boolean isExemptFood() {
        return this.exemptFood;
    }

    public void setExemptFood(boolean exemptFood) {
        this.exemptFood = exemptFood;
    }

    public boolean isExemptClothing() {
        return this.exemptClothing;
    }

    public void setExemptClothing(boolean exemptClothing) {
        this.exemptClothing = exemptClothing;
    }

    TaxRate(String state, float taxRate, boolean exemptFood, boolean exemptClothing) {
        this.state = state.toUpperCase();
        this.taxRate = taxRate;
        this.exemptClothing = exemptClothing;
        this.exemptFood = exemptFood;
    }
}

// Item Object
class Item {
    private String name;
    private int quantity;
    private float price;

    Item(String name, int quantity, float price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
