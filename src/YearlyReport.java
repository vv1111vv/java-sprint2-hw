

import java.util.Map;

class YearlyReport {

    private final Integer month;
    private final Integer amount;
    private final Boolean isExpense;


    public YearlyReport(Map<String, String> values) {
        this.isExpense = Boolean.valueOf(values.get("is_expense"));
        this.month = Integer.valueOf(values.get("month"));
        this.amount = Integer.valueOf(values.get("amount"));
    }

    public Boolean getIsExpense() {
        return isExpense;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getAmount() {
        return amount;
    }

}
