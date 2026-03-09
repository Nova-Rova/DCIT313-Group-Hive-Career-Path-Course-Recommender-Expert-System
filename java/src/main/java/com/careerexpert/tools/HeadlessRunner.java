package com.careerexpert.tools;

import com.careerexpert.model.UserProfile;
import com.careerexpert.engine.JavaInferenceEngine;
import com.careerexpert.model.CareerResult;
import com.careerexpert.util.ReportExporter;

import java.io.File;
import java.util.List;

/**
 * Simple headless runner that uses the Java inference engine to produce
 * an HTML report without launching the GUI. Useful when Maven is not available.
 */
public class HeadlessRunner {
    public static void main(String[] args) throws Exception {
        // Build a sample profile (you can modify this or pass args later)
        UserProfile profile = new UserProfile();
        profile.addInterest("Technology");
        profile.addSubject("Mathematics");
        profile.addSkill("Problem Solving");
        profile.setPreferredEnvironment("Office");
        profile.setEducationLevel("Undergraduate");

        JavaInferenceEngine engine = new JavaInferenceEngine();
        List<CareerResult> results = engine.evaluate(profile, 3);

        String outDir = (args != null && args.length > 0) ? args[0] : System.getProperty("user.dir");
        File out = new File(outDir);
        if (!out.exists()) out.mkdirs();

        String htmlFile = ReportExporter.exportToHtml(profile, results, out.getAbsolutePath());
        System.out.println("REPORT_PATH=" + htmlFile);
    }
}
