package com.careerexpert.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all user inputs from the questionnaire.
 * These are used to build the Prolog query fact list.
 */
public class UserProfile {

    private List<String> interests = new ArrayList<>();
    private List<String> strongSubjects = new ArrayList<>();
    private List<String> skills = new ArrayList<>();
    private String preferredEnvironment;
    private String educationLevel;

    // ==================== Setters ====================

    public void addInterest(String interest) {
        interests.add(interest.toLowerCase().replace(" ", "_"));
    }

    public void addSubject(String subject) {
        strongSubjects.add(subject.toLowerCase().replace(" ", "_"));
    }

    public void addSkill(String skill) {
        skills.add(skill.toLowerCase().replace(" ", "_"));
    }

    public void setPreferredEnvironment(String env) {
        this.preferredEnvironment = env.toLowerCase().replace(" ", "_");
    }

    public void setEducationLevel(String edu) {
        this.educationLevel = edu.toLowerCase().replace(" ", "_");
    }

    // ==================== Getters ====================

    public List<String> getInterests() { return interests; }
    public List<String> getStrongSubjects() { return strongSubjects; }
    public List<String> getSkills() { return skills; }
    public String getPreferredEnvironment() { return preferredEnvironment; }
    public String getEducationLevel() { return educationLevel; }

    /**
     * Converts the UserProfile to a Prolog fact list string.
     * Example: [interest(technology),subject(mathematics),skill(problem_solving),environment(office)]
     */
    public String toPrologFactList() {
        List<String> facts = new ArrayList<>();

        for (String interest : interests) {
            facts.add("interest(" + interest + ")");
        }
        for (String subject : strongSubjects) {
            facts.add("subject(" + subject + ")");
        }
        for (String skill : skills) {
            facts.add("skill(" + skill + ")");
        }
        if (preferredEnvironment != null && !preferredEnvironment.isEmpty()) {
            facts.add("environment(" + preferredEnvironment + ")");
        }
        if (educationLevel != null && !educationLevel.isEmpty()) {
            facts.add("education(" + educationLevel + ")");
        }

        return "[" + String.join(",", facts) + "]";
    }

    /**
     * Returns a human-readable summary of the user's profile
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your Profile Summary:\n");
        sb.append("  Interests        : ").append(String.join(", ", interests)).append("\n");
        sb.append("  Strong Subjects  : ").append(String.join(", ", strongSubjects)).append("\n");
        sb.append("  Skills           : ").append(String.join(", ", skills)).append("\n");
        sb.append("  Work Environment : ").append(preferredEnvironment != null ? preferredEnvironment : "Not specified").append("\n");
        sb.append("  Education Level  : ").append(educationLevel != null ? educationLevel : "Not specified").append("\n");
        return sb.toString();
    }

    public boolean hasEnoughData() {
        int total = interests.size() + strongSubjects.size() + skills.size();
        return total >= 2;
    }

    @Override
    public String toString() {
        return String.format("UserProfile{interests=%s, subjects=%s, skills=%s, env=%s, edu=%s}",
                interests, strongSubjects, skills, preferredEnvironment, educationLevel);
    }
}
