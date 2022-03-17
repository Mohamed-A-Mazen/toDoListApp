package todopackage;

import java.time.LocalDate;

public class ToDoItem {
    private String shortDescription;
    private String detailedDescription;
    private LocalDate date;

    public ToDoItem(String shortDescription, String detailedDescription, LocalDate date) {
        this.shortDescription = shortDescription;
        this.detailedDescription = detailedDescription;
        this.date = date;
    }
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    @Override
    public String toString() {
        return this.getShortDescription();
    }

    public LocalDate getDate() {
        return date;
    }



    public void setDate(LocalDate date) {
        this.date = date;
    }


}
