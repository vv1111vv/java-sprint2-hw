

import java.util.Map;

class MonthlyReport {

    private final String name;
    private final Boolean isExpense;
    private final Integer quantity;
    private final Integer sumOfOne;


    public MonthlyReport(Map<String, String> values) {
        this.name = values.get("item_name");
        this.isExpense = Boolean.valueOf(values.get("is_expense"));
        this.quantity = Integer.valueOf(values.get("quantity"));
        this.sumOfOne = Integer.valueOf(values.get("sum_of_one"));
    }

    public String getName() {
        return name;
    }

    public Boolean getIsExpense() {
        return isExpense;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getSumOfOne() {
        return sumOfOne;
    }

    public Integer getTotal() {
        return quantity * sumOfOne;
    }

    public String toString() {
        return name + " | " + (quantity * sumOfOne);
    }
}
