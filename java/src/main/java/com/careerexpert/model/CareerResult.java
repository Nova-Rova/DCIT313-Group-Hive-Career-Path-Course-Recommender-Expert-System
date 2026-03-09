package com.careerexpert.model;

import java.util.List;

/**
 * Represents a career recommendation result returned by the Prolog engine.
 */
public class CareerResult {

    private int rank;
    private String careerId;
    private String careerName;
    private int score;
    private int matchPercent;
    private String description;
    private List<String> requiredSkills;
    private String suggestedDegree;
    private List<String> certifications;
    private String careerOutlook;
    private String explanation;

    public CareerResult() {}

    public CareerResult(int rank, String careerId, String careerName, int score,
                        int matchPercent, String description, List<String> requiredSkills,
                        String suggestedDegree, List<String> certifications,
                        String careerOutlook, String explanation) {
        this.rank = rank;
        this.careerId = careerId;
        this.careerName = careerName;
        this.score = score;
        this.matchPercent = matchPercent;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.suggestedDegree = suggestedDegree;
        this.certifications = certifications;
        this.careerOutlook = careerOutlook;
        this.explanation = explanation;
    }

    // ===================== Getters & Setters =====================

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public String getCareerId() { return careerId; }
    public void setCareerId(String careerId) { this.careerId = careerId; }

    public String getCareerName() { return careerName; }
    public void setCareerName(String careerName) { this.careerName = careerName; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getMatchPercent() { return matchPercent; }
    public void setMatchPercent(int matchPercent) { this.matchPercent = matchPercent; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getSuggestedDegree() { return suggestedDegree; }
    public void setSuggestedDegree(String suggestedDegree) { this.suggestedDegree = suggestedDegree; }

    public List<String> getCertifications() { return certifications; }
    public void setCertifications(List<String> certifications) { this.certifications = certifications; }

    public String getCareerOutlook() { return careerOutlook; }
    public void setCareerOutlook(String careerOutlook) { this.careerOutlook = careerOutlook; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    // ===================== Display Helper =====================

    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════╗\n");
        sb.append(String.format("║  #%d  %-44s ║\n", rank, careerName));
        sb.append("╠══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Match Score   : %d conditions matched (%d%%)         \n", score, matchPercent));
        sb.append(String.format("║  Description   : %s\n", wordWrap(description, 50)));
        sb.append(String.format("║  Degree        : %s\n", suggestedDegree));
        sb.append(String.format("║  Certifications: %s\n", String.join(", ", certifications)));
        sb.append(String.format("║  Outlook       : %s\n", careerOutlook));
        sb.append("╠══════════════════════════════════════════════════╣\n");
        sb.append("║  WHY THIS CAREER?\n");
        sb.append(String.format("║  %s\n", wordWrap(explanation, 50)));
        sb.append("╚══════════════════════════════════════════════════╝\n");
        return sb.toString();
    }

    private String wordWrap(String text, int lineLength) {
        if (text == null || text.length() <= lineLength) return text;
        StringBuilder result = new StringBuilder();
        String[] words = text.split(" ");
        int currentLength = 0;
        for (String word : words) {
            if (currentLength + word.length() > lineLength) {
                result.append("\n║  ");
                currentLength = 0;
            }
            result.append(word).append(" ");
            currentLength += word.length() + 1;
        }
        return result.toString().trim();
    }

    @Override
    public String toString() {
        return String.format("CareerResult{rank=%d, career='%s', score=%d, match=%d%%}",
                rank, careerName, score, matchPercent);
    }
}
