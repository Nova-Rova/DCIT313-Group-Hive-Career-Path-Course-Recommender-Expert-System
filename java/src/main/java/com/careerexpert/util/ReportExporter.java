package com.careerexpert.util;

import com.careerexpert.model.CareerResult;
import com.careerexpert.model.UserProfile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exports career recommendations as a formatted text/HTML report.
 *
 * NOTE: For proper PDF export, this can be extended to use iText or Apache PDFBox.
 * This version generates a clean HTML file that can be printed as PDF from any browser,
 * or a plain text file - no external dependencies required.
 */
public class ReportExporter {

    /**
     * Exports results to an HTML file styled for printing/PDF conversion.
     */
    public static String exportToHtml(UserProfile profile, List<CareerResult> results, String outputPath) throws IOException {
        String filename = outputPath + File.separator + "CareerReport_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".html";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(generateHtml(profile, results));
        }

        return filename;
    }

    /**
     * Exports results to a plain text file.
     */
    public static String exportToText(UserProfile profile, List<CareerResult> results, String outputPath) throws IOException {
        String filename = outputPath + File.separator + "CareerReport_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(generateTextReport(profile, results));
        }

        return filename;
    }

    private static String generateHtml(UserProfile profile, List<CareerResult> results) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>");
        html.append("<title>Career Recommendation Report</title>");
        html.append("<style>");
        html.append("body{font-family:Arial,sans-serif;margin:40px;color:#222;}");
        html.append("h1{color:#1a237e;border-bottom:3px solid #1a237e;padding-bottom:10px;}");
        html.append("h2{color:#283593;margin-top:30px;}");
        html.append(".card{border:1px solid #90caf9;border-radius:8px;padding:20px;margin:20px 0;background:#f8f9ff;}");
        html.append(".rank{font-size:2em;font-weight:bold;color:#1565c0;float:left;margin-right:15px;}");
        html.append(".match{background:#e3f2fd;padding:4px 12px;border-radius:20px;font-weight:bold;color:#1565c0;}");
        html.append(".label{font-weight:bold;color:#555;width:160px;display:inline-block;}");
        html.append(".explanation{background:#fffde7;border-left:4px solid #f9a825;padding:12px;margin-top:12px;border-radius:0 6px 6px 0;}");
        html.append(".profile-box{background:#e8f5e9;border:1px solid #a5d6a7;border-radius:8px;padding:16px;margin-bottom:24px;}");
        html.append(".footer{margin-top:40px;font-size:12px;color:#888;border-top:1px solid #ddd;padding-top:10px;}");
        html.append("@media print{.card{page-break-inside:avoid;}}");
        html.append("</style></head><body>");

        // Header
        html.append("<h1>🎓 Career Path & Course Recommendation Report</h1>");
        html.append("<p><strong>Generated:</strong> ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))).append("</p>");

        // User profile
        html.append("<div class='profile-box'>");
        html.append("<h2>Your Profile</h2>");
        html.append("<p><span class='label'>Interests:</span>").append(String.join(", ", profile.getInterests())).append("</p>");
        html.append("<p><span class='label'>Strong Subjects:</span>").append(String.join(", ", profile.getStrongSubjects())).append("</p>");
        html.append("<p><span class='label'>Skills:</span>").append(String.join(", ", profile.getSkills())).append("</p>");
        html.append("<p><span class='label'>Work Environment:</span>").append(profile.getPreferredEnvironment() != null ? profile.getPreferredEnvironment() : "Not specified").append("</p>");
        html.append("<p><span class='label'>Education Level:</span>").append(profile.getEducationLevel() != null ? profile.getEducationLevel() : "Not specified").append("</p>");
        html.append("</div>");

        // Results
        html.append("<h2>Top Career Recommendations</h2>");
        if (results.isEmpty()) {
            html.append("<p>No careers matched your profile. Please try broadening your inputs.</p>");
        } else {
            for (CareerResult r : results) {
                html.append("<div class='card'>");
                html.append("<div class='rank'>#").append(r.getRank()).append("</div>");
                html.append("<h2 style='display:inline;'>").append(r.getCareerName()).append("</h2>");
                html.append(" &nbsp;<span class='match'>").append(r.getMatchPercent()).append("% Match</span>");
                html.append("<div style='clear:both;margin-top:10px;'></div>");
                html.append("<p><span class='label'>Description:</span>").append(r.getDescription()).append("</p>");
                html.append("<p><span class='label'>Required Skills:</span>").append(String.join(", ", r.getRequiredSkills())).append("</p>");
                html.append("<p><span class='label'>Suggested Degree:</span>").append(r.getSuggestedDegree()).append("</p>");
                html.append("<p><span class='label'>Certifications:</span>").append(String.join(", ", r.getCertifications())).append("</p>");
                html.append("<p><span class='label'>Career Outlook:</span>").append(r.getCareerOutlook()).append("</p>");
                html.append("<div class='explanation'><strong>💡 Why this career?</strong><br>").append(r.getExplanation()).append("</div>");
                html.append("</div>");
            }
        }

        // Limitations
        html.append("<h2>System Limitations</h2>");
        html.append("<ul>");
        html.append("<li>This is a rule-based expert system, not a replacement for professional career counseling.</li>");
        html.append("<li>Recommendations are limited to the predefined rule set (24 career rules).</li>");
        html.append("<li>Results are based on general patterns and may not reflect individual circumstances.</li>");
        html.append("<li>This system uses forward chaining logic, not AI/ML prediction.</li>");
        html.append("</ul>");

        html.append("<div class='footer'>Career Path & Course Recommender System &nbsp;|&nbsp; Rule-Based Expert System &nbsp;|&nbsp; Prolog + Java</div>");
        html.append("</body></html>");
        return html.toString();
    }

    private static String generateTextReport(UserProfile profile, List<CareerResult> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("======================================================\n");
        sb.append("   CAREER PATH & COURSE RECOMMENDATION REPORT\n");
        sb.append("======================================================\n");
        sb.append("Generated: ").append(LocalDate.now()).append("\n\n");

        sb.append("YOUR PROFILE\n");
        sb.append("------------\n");
        sb.append(profile.getSummary()).append("\n");

        sb.append("TOP CAREER RECOMMENDATIONS\n");
        sb.append("--------------------------\n");
        for (CareerResult r : results) {
            sb.append(r.toDisplayString()).append("\n");
        }

        sb.append("\nSYSTEM LIMITATIONS\n");
        sb.append("------------------\n");
        sb.append("- Not a replacement for professional career counseling.\n");
        sb.append("- Rule-based system (not AI/ML).\n");
        sb.append("- Limited to 24 predefined career rules.\n");

        return sb.toString();
    }
}
