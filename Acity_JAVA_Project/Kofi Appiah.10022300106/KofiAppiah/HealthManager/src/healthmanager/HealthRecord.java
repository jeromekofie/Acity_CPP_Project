package healthmanager;

import java.util.Objects;

/**
 * Represents a patient's health record with comprehensive medical information.
 * This class encapsulates all relevant health metrics and provides calculated
 * health indicators like BMI.
 * 
 * @author [Your Name]
 * @version 1.0
 * @since 2023
 */
public class HealthRecord {
    private static int recordCounter = 1000; // Starting ID for auto-generation
    
    public final int id;
    public String name;
    public int age;
    public double weight; // in kilograms
    public double height; // in meters
    public double bmi;
    public String bloodType;
    public String allergies;
    public String medicalHistory;
    public String currentMedications;

    /**
     * Constructs a new HealthRecord with required parameters.
     * 
    public double height; // in meters
    public double bmi;
    public String bloodType;
    public String allergies;
    public String medicalHistory;
    public String currentMedications;
     * @param name Full name of the patient
     * @param age Age in years
     * @param weight Weight in kilograms
     * @param height Height in meters
     * @param bloodType Blood type (e.g., "A+", "O-")
     * @param allergies Known allergies (comma-separated if multiple)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public HealthRecord(String name, int age, double weight, double height, 
                       String bloodType, String allergies) {
        this.id = ++recordCounter;
        setName(name);
        setAge(age);
        setWeight(weight);
        setHeight(height);
        setBloodType(bloodType);
        setAllergies(allergies);
        this.bmi = BMIUtils.calculateBMI(weight, height);
        this.medicalHistory = "";
        this.currentMedications = "";
    }

    // Getters with JavaDoc
    /**
     * @return The unique record ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return Patient's full name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Patient's age in years
     */
    public int getAge() {
        return age;
    }

    /**
     * @return Patient's weight in kilograms
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @return Patient's height in meters
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return Calculated Body Mass Index
     */
    public double getBmi() {
        return bmi;
    }

    /**
     * @return Patient's blood type
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * @return Known allergies (comma-separated if multiple)
     */
    public String getAllergies() {
        return allergies;
    }

    /**
     * @return Patient's medical history
     */
    public String getMedicalHistory() {
        return medicalHistory;
    }

    /**
     * @return Current medications (comma-separated if multiple)
     */
    public String getCurrentMedications() {
        return currentMedications;
    }

    // Setters with validation
    /**
     * Sets the patient's name after validation.
     * @param name Full name (2-100 characters)
     * @throws IllegalArgumentException if name is invalid
     */
    public void setName(String name) {
        if (name == null || name.trim().length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Name must be 2-100 characters");
        }
        this.name = name.trim();
    }

    /**
     * Sets the patient's age after validation.
     * @param age Age in years (0-120)
     * @throws IllegalArgumentException if age is invalid
     */
    public void setAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 0-120");
        }
        this.age = age;
    }

    /**
     * Sets the patient's weight and recalculates BMI.
     * @param weight Weight in kilograms (>0)
     * @throws IllegalArgumentException if weight is invalid
     */
    public void setWeight(double weight) {
        if (weight <= 0 || weight > 500) {
            throw new IllegalArgumentException("Weight must be between 0-500 kg");
        }
        this.weight = weight;
        this.bmi = BMIUtils.calculateBMI(weight, height);
    }

    /**
     * Sets the patient's height and recalculates BMI.
     * @param height Height in meters (>0)
     * @throws IllegalArgumentException if height is invalid
     */
    public void setHeight(double height) {
        if (height <= 0 || height > 3) {
            throw new IllegalArgumentException("Height must be between 0-3 meters");
        }
        this.height = height;
        this.bmi = BMIUtils.calculateBMI(weight, height);
    }

    /**
     * Sets the patient's blood type.
     * @param bloodType Standard blood type (e.g., "A+", "O-")
     * @throws IllegalArgumentException if blood type is invalid
     */
    public void setBloodType(String bloodType) {
        if (bloodType == null || !bloodType.matches("^(A|B|AB|O)[+-]$")) {
            throw new IllegalArgumentException("Invalid blood type format");
        }
        this.bloodType = bloodType.toUpperCase();
    }

    /**
     * Sets the patient's allergies.
     * @param allergies Known allergies (null or empty for none)
     */
    public void setAllergies(String allergies) {
        this.allergies = allergies != null ? allergies.trim() : "None";
    }

    /**
     * Sets the patient's medical history.
     * @param medicalHistory Medical history details
     */
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory != null ? medicalHistory.trim() : "";
    }

    /**
     * Sets the patient's current medications.
     * @param currentMedications Current medications (comma-separated if multiple)
     */
    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications != null ? currentMedications.trim() : "";
    }

    // Business logic methods
    /**
     * Returns a BMI category based on calculated BMI.
     * @return BMI category string
     */
    public String getBmiCategory() {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal weight";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    /**
     * Adds a new medication to the current medications list.
     * @param medication New medication to add
     */
    public void addMedication(String medication) {
        if (medication == null || medication.trim().isEmpty()) return;
        
        if (currentMedications.isEmpty()) {
            currentMedications = medication.trim();
        } else {
            currentMedications += ", " + medication.trim();
        }
    }

    // Standard methods override
    @Override
    public String toString() {
        return String.format("HealthRecord [ID: %d, Name: %s, Age: %d, BMI: %.1f (%s)]",
                id, name, age, bmi, getBmiCategory());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthRecord that = (HealthRecord) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}