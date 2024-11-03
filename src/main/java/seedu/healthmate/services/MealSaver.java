package seedu.healthmate.services;


import java.util.Optional;

import seedu.healthmate.core.Meal;
import seedu.healthmate.core.MealList;
import seedu.healthmate.exceptions.EmptyCalorieException;
import seedu.healthmate.utils.DuplicateEntryChecker;

/**
 * Service class responsible for saving and managing meal data.
 * Handles extraction of meal information from user input and persistence of meals.
 */
public class MealSaver {
    private HistoryTracker historyTracker;

    /**
     * Constructs a new MealSaver with the specified history tracker.
     *
     * @param historyTracker The history tracker to use for saving meal data
     */
    public MealSaver(HistoryTracker historyTracker) {
        this.historyTracker = historyTracker;
    }

    /**
     * Extracts a meal object from the user's input string.
     * 
     * @param userInput The raw input string from the user
     * @return Optional containing the extracted Meal if successful, empty Optional otherwise
     */
    public Optional<Meal> extractMealFromUserInput(String userInput) {
        try {
            String command = "save meal";
            Meal meal = Meal.extractMealFromString(userInput, command);
            if (meal.descriptionIsEmpty()) {
                UI.printReply("Meal options require a name", "Retry: ");
                return Optional.empty();
            }
            return Optional.of(meal);
        } catch (EmptyCalorieException e) {
            UI.printReply("Every meal needs a calorie integer. (e.g. 120)", "");
        } catch (StringIndexOutOfBoundsException s) {
            UI.printReply("Do not forget to use /c to mark the following integer as calories", "Retry: ");
        } catch (Exception n) {
            UI.printReply("A calorie entry needs to be an integer", "Error: ");
        }
        return null;
    }

    /**
     * Saves a meal to the meal list, handling duplicate entries.
     *
     * @param meal The meal to save
     * @param mealList The list to save the meal to
     */
    public void saveMeal(Meal meal, MealList mealList) {
        if (DuplicateEntryChecker.isDuplicate(meal.getName(), mealList.getMealList())) {
            System.out.println("Duplicate meal found: " + meal.getName());
            if (shouldOverwrite(meal)) {
                overwriteMeal(meal, mealList);
            }
        } else {
            mealList.addMeal(meal);
        }
        historyTracker.saveMealOptions(mealList);
    }

    /**
     * Determines if a duplicate meal should be overwritten.
     *
     * @param meal The meal to check
     * @return true if the meal should be overwritten, false otherwise
     */
    private boolean shouldOverwrite(Meal meal) {
        // Logic to determine if the meal should be overwritten
        return true;
    }

    /**
     * Overwrites an existing meal in the meal list with a new meal.
     *
     * @param newMeal The new meal to replace the existing one
     * @param mealList The list containing the meal to overwrite
     */
    private void overwriteMeal(Meal newMeal, MealList mealList) {
        mealList.updateMeal(newMeal);
    }
}
