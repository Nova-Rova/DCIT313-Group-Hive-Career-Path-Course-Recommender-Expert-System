package com.careerexpert.engine;

import com.careerexpert.model.CareerResult;
import com.careerexpert.model.UserProfile;

import java.util.*;

/**
 * Java-based fallback inference engine.
 *
 * This implements the SAME forward chaining logic as the Prolog knowledge base.
 * It is used automatically when SWI-Prolog is not installed.
 *
 * Rule Design:
 *   Each rule maps a set of conditions (interests + subjects + skills + environment)
 *   to a career. The engine scores each career by counting matched conditions,
 *   then ranks results descending by score.
 *
 * Conflict Resolution:
 *   1. Primary: Raw score (number of matched conditions)
 *   2. Secondary: Match percentage (score / total rule conditions)
 *   3. Tertiary: Career name alphabetically for consistency
 */
public class JavaInferenceEngine {

    // ==================== Knowledge Base ====================

    // Each entry: CareerID → array of required conditions
    private static final Map<String, String[]> RULES = new LinkedHashMap<>();

    // Career metadata
    private static final Map<String, String[]> CAREER_DATA = new LinkedHashMap<>();
    // Format: name, description, skills, degree, certifications, outlook

    static {
        // ===== RULES (mirrors Prolog career_rule facts) =====
        RULES.put("software_developer",    new String[]{"interest:technology","subject:mathematics","skill:problem_solving"});
        RULES.put("data_scientist",        new String[]{"interest:technology","subject:mathematics","skill:analytical_thinking"});
        RULES.put("cybersecurity_analyst", new String[]{"interest:technology","subject:mathematics","skill:problem_solving","environment:office"});
        RULES.put("ai_engineer",           new String[]{"interest:technology","subject:mathematics","skill:analytical_thinking","skill:problem_solving"});
        RULES.put("network_engineer",      new String[]{"interest:technology","subject:physics","skill:problem_solving","environment:office"});
        RULES.put("medical_doctor",        new String[]{"interest:health","subject:biology","skill:analytical_thinking","skill:helping_people","education:undergraduate"});
        RULES.put("nurse",                 new String[]{"interest:health","skill:helping_people","skill:communication","environment:hospital"});
        RULES.put("pharmacist",            new String[]{"interest:health","subject:chemistry","skill:analytical_thinking","skill:attention_to_detail"});
        RULES.put("biomedical_engineer",   new String[]{"interest:engineering","interest:health","subject:biology","skill:problem_solving"});
        RULES.put("civil_engineer",        new String[]{"interest:engineering","subject:mathematics","subject:physics","skill:problem_solving","environment:outdoor"});
        RULES.put("mechanical_engineer",   new String[]{"interest:engineering","subject:physics","subject:mathematics","skill:problem_solving"});
        RULES.put("electrical_engineer",   new String[]{"interest:engineering","subject:physics","subject:mathematics","skill:analytical_thinking"});
        RULES.put("business_analyst",      new String[]{"interest:business","skill:analytical_thinking","skill:communication","environment:office"});
        RULES.put("marketing_manager",     new String[]{"interest:business","skill:creativity","skill:communication","skill:leadership"});
        RULES.put("financial_analyst",     new String[]{"interest:business","subject:mathematics","skill:analytical_thinking","skill:attention_to_detail"});
        RULES.put("accountant",            new String[]{"interest:business","subject:mathematics","skill:attention_to_detail","environment:office"});
        RULES.put("graphic_designer",      new String[]{"interest:arts","skill:creativity","skill:visual_arts","environment:remote"});
        RULES.put("ux_ui_designer",        new String[]{"interest:arts","interest:technology","skill:creativity","skill:problem_solving"});
        RULES.put("teacher_educator",      new String[]{"skill:communication","skill:helping_people","skill:leadership","environment:school"});
        RULES.put("psychologist",          new String[]{"interest:health","subject:biology","skill:empathy","skill:communication"});
        RULES.put("environmental_scientist",new String[]{"interest:engineering","subject:biology","subject:chemistry","environment:outdoor"});
        RULES.put("architect",             new String[]{"interest:arts","interest:engineering","subject:mathematics","skill:creativity"});
        RULES.put("lawyer",                new String[]{"interest:business","skill:analytical_thinking","skill:communication","skill:leadership","education:undergraduate"});
        RULES.put("entrepreneur",          new String[]{"interest:business","skill:creativity","skill:leadership","skill:problem_solving"});

        // ===== CAREER DATA =====
        // Format: [name, description, skills, degree, certifications, outlook]
        CAREER_DATA.put("software_developer",    new String[]{
            "Software Developer",
            "Design and build software applications, systems, and tools.",
            "Problem Solving, Programming, Mathematics, Analytical Thinking",
            "B.Sc. Computer Science / Software Engineering",
            "AWS Certified Developer, Oracle Java Certification, Microsoft Azure",
            "Excellent - 25% growth expected by 2030"});

        CAREER_DATA.put("data_scientist",        new String[]{
            "Data Scientist",
            "Analyze complex datasets to extract insights and build predictive models.",
            "Analytical Thinking, Mathematics, Statistics, Problem Solving",
            "B.Sc./M.Sc. Data Science / Statistics / Computer Science",
            "Google Data Analytics, IBM Data Science, Tableau Desktop",
            "Excellent - 36% growth expected by 2031"});

        CAREER_DATA.put("cybersecurity_analyst", new String[]{
            "Cybersecurity Analyst",
            "Protect computer systems and networks from digital attacks.",
            "Problem Solving, Analytical Thinking, Attention to Detail",
            "B.Sc. Cybersecurity / Computer Science",
            "CompTIA Security+, CEH, CISSP, OSCP",
            "Very Good - 35% growth expected by 2031"});

        CAREER_DATA.put("ai_engineer",           new String[]{
            "AI/ML Engineer",
            "Build intelligent systems using machine learning and AI.",
            "Mathematics, Programming, Analytical Thinking, Problem Solving",
            "B.Sc./M.Sc. Artificial Intelligence / Computer Science",
            "TensorFlow Developer Certificate, AWS ML Specialty",
            "Excellent - fastest growing tech role"});

        CAREER_DATA.put("network_engineer",      new String[]{
            "Network Engineer",
            "Design and manage computer networks and infrastructure.",
            "Problem Solving, Technical Skills, Analytical Thinking",
            "B.Sc. Computer Networks / Information Technology",
            "Cisco CCNA, CompTIA Network+, CCNP",
            "Good - stable demand across industries"});

        CAREER_DATA.put("medical_doctor",        new String[]{
            "Medical Doctor",
            "Diagnose and treat illness, diseases, and injuries.",
            "Analytical Thinking, Helping People, Attention to Detail",
            "M.B.B.S. / M.D. Medicine",
            "Board Certification, BLS/ACLS, Medical License",
            "Good - 3% steady growth, always in demand"});

        CAREER_DATA.put("nurse",                 new String[]{
            "Registered Nurse",
            "Provide and coordinate patient care in health facilities.",
            "Helping People, Communication, Attention to Detail",
            "B.Sc. Nursing / Associate Degree in Nursing",
            "NCLEX-RN, BLS/CPR, Specialty Nursing Certifications",
            "Excellent - 9% growth, critical shortage globally"});

        CAREER_DATA.put("pharmacist",            new String[]{
            "Pharmacist",
            "Dispense medications and counsel patients on drug usage.",
            "Analytical Thinking, Attention to Detail, Chemistry",
            "B.Pharm / Pharm.D.",
            "Pharmacist License, BCPS, Immunization Certification",
            "Good - 2% growth, stable profession"});

        CAREER_DATA.put("biomedical_engineer",   new String[]{
            "Biomedical Engineer",
            "Design medical devices bridging engineering and healthcare.",
            "Problem Solving, Analytical Thinking, Biology",
            "B.Sc. Biomedical Engineering",
            "CBET, PMP",
            "Very Good - 10% growth in medical tech"});

        CAREER_DATA.put("civil_engineer",        new String[]{
            "Civil Engineer",
            "Design and supervise construction of infrastructure.",
            "Problem Solving, Mathematics, Technical Skills",
            "B.Sc. Civil Engineering",
            "PE License, PMP, LEED Green Associate",
            "Good - 8% growth from infrastructure investment"});

        CAREER_DATA.put("mechanical_engineer",   new String[]{
            "Mechanical Engineer",
            "Design and manufacture mechanical systems and devices.",
            "Problem Solving, Mathematics, Technical Skills",
            "B.Sc. Mechanical Engineering",
            "PE License, SolidWorks Certification, Six Sigma",
            "Good - 7% growth in manufacturing and energy"});

        CAREER_DATA.put("electrical_engineer",   new String[]{
            "Electrical Engineer",
            "Design electrical systems, circuits, and electronic devices.",
            "Mathematics, Problem Solving, Technical Skills",
            "B.Sc. Electrical Engineering / Electronics",
            "PE License, Certified Energy Auditor, IEEE Certifications",
            "Good - 7% growth in energy and electronics"});

        CAREER_DATA.put("business_analyst",      new String[]{
            "Business Analyst",
            "Analyze business processes and recommend data-driven improvements.",
            "Analytical Thinking, Communication, Problem Solving",
            "B.Sc. Business Administration / Information Systems",
            "CBAP, PMI-PBA, Agile/Scrum Master",
            "Good - 14% growth in digital transformation"});

        CAREER_DATA.put("marketing_manager",     new String[]{
            "Marketing Manager",
            "Plan and execute campaigns to promote products and grow brands.",
            "Creativity, Communication, Business Acumen, Leadership",
            "B.Sc. Marketing / Business Administration",
            "Google Analytics, HubSpot Marketing, Facebook Blueprint",
            "Good - 10% growth with digital marketing boom"});

        CAREER_DATA.put("financial_analyst",     new String[]{
            "Financial Analyst",
            "Assess investments and provide financial guidance to businesses.",
            "Analytical Thinking, Mathematics, Attention to Detail",
            "B.Sc. Finance / Accounting / Economics",
            "CFA, CPA, Bloomberg Market Concepts",
            "Good - 9% growth in finance sector"});

        CAREER_DATA.put("accountant",            new String[]{
            "Accountant / Auditor",
            "Prepare and examine financial records ensuring accuracy.",
            "Mathematics, Attention to Detail, Analytical Thinking",
            "B.Sc. Accounting / Finance",
            "CPA, ACCA, CMA, QuickBooks Certification",
            "Good - 7% stable growth in all sectors"});

        CAREER_DATA.put("graphic_designer",      new String[]{
            "Graphic Designer",
            "Create visual content for print, digital media, and branding.",
            "Creativity, Visual Arts, Communication",
            "B.Sc./B.A. Graphic Design / Visual Communication",
            "Adobe Certified Expert (ACE), Google UX Design",
            "Fair - 3% growth, strong freelance potential"});

        CAREER_DATA.put("ux_ui_designer",        new String[]{
            "UX/UI Designer",
            "Design intuitive and visually appealing digital user experiences.",
            "Creativity, Problem Solving, Communication, Visual Arts",
            "B.Sc. Computer Science / Interaction Design",
            "Google UX Design Certificate, Figma Certification",
            "Very Good - 13% growth in digital products"});

        CAREER_DATA.put("teacher_educator",      new String[]{
            "Teacher / Educator",
            "Educate and mentor students in academic subjects and life skills.",
            "Communication, Helping People, Leadership, Empathy",
            "B.Ed. / B.Sc. + PGDE / Education Degree",
            "Teaching License/Certificate, TESOL/TEFL",
            "Good - steady demand, critical profession globally"});

        CAREER_DATA.put("psychologist",          new String[]{
            "Psychologist / Counselor",
            "Study human behavior and provide mental health support.",
            "Empathy, Communication, Analytical Thinking",
            "B.Sc./M.Sc. Psychology / Clinical Psychology",
            "LPC, NBCC Certification",
            "Excellent - 22% growth, rising mental health awareness"});

        CAREER_DATA.put("environmental_scientist",new String[]{
            "Environmental Scientist",
            "Study environmental problems and develop sustainability solutions.",
            "Analytical Thinking, Biology, Problem Solving",
            "B.Sc. Environmental Science / Environmental Engineering",
            "CHMM, PE (Environmental), ISO 14001 Lead Auditor",
            "Good - 8% growth in green economy"});

        CAREER_DATA.put("architect",             new String[]{
            "Architect",
            "Design buildings balancing aesthetics, function, and safety.",
            "Creativity, Mathematics, Visual Arts, Problem Solving",
            "B.Arch / M.Arch Architecture",
            "Licensed Architect (RA), LEED AP, AutoCAD Certification",
            "Fair - 3% growth, specialized creative profession"});

        CAREER_DATA.put("lawyer",                new String[]{
            "Lawyer / Legal Counsel",
            "Advise clients on legal matters and represent them in court.",
            "Analytical Thinking, Communication, Leadership",
            "LLB / JD Law Degree + Bar Exam",
            "State Bar License, Specialized Legal Certifications",
            "Good - 10% growth, diverse practice areas"});

        CAREER_DATA.put("entrepreneur",          new String[]{
            "Entrepreneur / Startup Founder",
            "Build and grow innovative businesses solving real problems.",
            "Creativity, Leadership, Business Acumen, Problem Solving",
            "B.Sc. Business / Any Field + MBA (optional)",
            "PMP, Lean Startup, Business Development Certifications",
            "Variable - high risk, high reward career"});
    }

    // ==================== Explanation Map ====================

    private static final Map<String, String> EXPLANATIONS = new LinkedHashMap<>();

    static {
        EXPLANATIONS.put("software_developer",     "Recommended because: Interest=Technology + Strong Subject=Mathematics + Skill=Problem Solving. These are core requirements for software development.");
        EXPLANATIONS.put("data_scientist",         "Recommended because: Interest=Technology + Strong Subject=Mathematics + Skill=Analytical Thinking. Data science demands strong quantitative reasoning.");
        EXPLANATIONS.put("cybersecurity_analyst",  "Recommended because: Interest=Technology + Subject=Mathematics + Skill=Problem Solving + Environment=Office. Security roles need logical tech thinkers.");
        EXPLANATIONS.put("ai_engineer",            "Recommended because: Interest=Technology + Subject=Mathematics + Skills=Analytical Thinking & Problem Solving. AI/ML engineering needs deep math and logic.");
        EXPLANATIONS.put("network_engineer",       "Recommended because: Interest=Technology + Subject=Physics + Skill=Problem Solving + Environment=Office. Networking needs physical and logical systems knowledge.");
        EXPLANATIONS.put("medical_doctor",         "Recommended because: Interest=Health + Subject=Biology + Skills=Analytical Thinking & Helping People. Medicine combines science with compassionate care.");
        EXPLANATIONS.put("nurse",                  "Recommended because: Interest=Health + Skills=Helping People & Communication + Environment=Hospital. Nursing requires strong patient interaction skills.");
        EXPLANATIONS.put("pharmacist",             "Recommended because: Interest=Health + Subject=Chemistry + Skills=Analytical Thinking & Attention to Detail. Pharmacists must understand drug interactions precisely.");
        EXPLANATIONS.put("biomedical_engineer",    "Recommended because: Interests=Engineering & Health + Subject=Biology + Skill=Problem Solving. Biomedical engineering bridges healthcare and technical fields.");
        EXPLANATIONS.put("civil_engineer",         "Recommended because: Interest=Engineering + Subjects=Mathematics & Physics + Skill=Problem Solving + Environment=Outdoor. Civil engineers work on infrastructure projects.");
        EXPLANATIONS.put("mechanical_engineer",    "Recommended because: Interest=Engineering + Subjects=Physics & Mathematics + Skill=Problem Solving. Mechanical engineering applies physics to design systems.");
        EXPLANATIONS.put("electrical_engineer",    "Recommended because: Interest=Engineering + Subjects=Physics & Mathematics + Skill=Analytical Thinking. Electrical engineering requires deep physics understanding.");
        EXPLANATIONS.put("business_analyst",       "Recommended because: Interest=Business + Skills=Analytical Thinking & Communication + Environment=Office. BAs bridge business needs with technical solutions.");
        EXPLANATIONS.put("marketing_manager",      "Recommended because: Interest=Business + Skills=Creativity, Communication & Leadership. Marketing requires persuasion, innovation, and strategic thinking.");
        EXPLANATIONS.put("financial_analyst",      "Recommended because: Interest=Business + Subject=Mathematics + Skills=Analytical Thinking & Attention to Detail. Finance demands precision and quantitative analysis.");
        EXPLANATIONS.put("accountant",             "Recommended because: Interest=Business + Subject=Mathematics + Skill=Attention to Detail + Environment=Office. Accounting needs meticulous numerical accuracy.");
        EXPLANATIONS.put("graphic_designer",       "Recommended because: Interest=Arts + Skills=Creativity & Visual Arts + Environment=Remote. Graphic design rewards artistic talent and visual communication.");
        EXPLANATIONS.put("ux_ui_designer",         "Recommended because: Interests=Arts & Technology + Skills=Creativity & Problem Solving. UX/UI sits at the intersection of aesthetics and user research.");
        EXPLANATIONS.put("teacher_educator",       "Recommended because: Skills=Communication, Helping People & Leadership + Environment=School. Teaching requires passion for sharing knowledge.");
        EXPLANATIONS.put("psychologist",           "Recommended because: Interest=Health + Subject=Biology + Skills=Empathy & Communication. Psychology combines behavioral science with compassionate counseling.");
        EXPLANATIONS.put("environmental_scientist","Recommended because: Interest=Engineering + Subjects=Biology & Chemistry + Environment=Outdoor. Environmental science involves fieldwork and lab problem solving.");
        EXPLANATIONS.put("architect",              "Recommended because: Interests=Arts & Engineering + Subject=Mathematics + Skill=Creativity. Architecture merges artistic vision with structural precision.");
        EXPLANATIONS.put("lawyer",                 "Recommended because: Interest=Business + Skills=Analytical Thinking, Communication & Leadership + Education=Undergraduate. Law needs sharp reasoning and persuasion.");
        EXPLANATIONS.put("entrepreneur",           "Recommended because: Interest=Business + Skills=Creativity, Leadership & Problem Solving. Entrepreneurship rewards innovative thinkers who build ventures.");
    }

    // ==================== Inference Engine ====================

    /**
     * Forward chaining evaluation.
     * Converts user profile to a set of facts, scores each career rule,
     * and returns top N results sorted by score descending.
     */
    public List<CareerResult> evaluate(UserProfile profile, int topN) {
        // Build fact set from user profile
        Set<String> userFacts = buildFactSet(profile);

        // Score each career
        List<CareerResult> scored = new ArrayList<>();
        for (Map.Entry<String, String[]> rule : RULES.entrySet()) {
            String careerId = rule.getKey();
            String[] conditions = rule.getValue();

            int matches = countMatches(conditions, userFacts);
            int total = conditions.length;

            // Minimum threshold: at least 2 conditions must match
            if (matches >= 2) {
                int matchPct = (int) Math.round((matches * 100.0) / total);
                String[] data = CAREER_DATA.get(careerId);
                String explanation = EXPLANATIONS.getOrDefault(careerId, "Matched based on your profile.");

                List<String> skills = Arrays.asList(data[2].split(",\\s*"));
                List<String> certs  = Arrays.asList(data[4].split(",\\s*"));

                CareerResult result = new CareerResult(
                        0, careerId, data[0], matches, matchPct,
                        data[1], skills, data[3], certs, data[5], explanation
                );
                scored.add(result);
            }
        }

        // Sort: primary = score descending, secondary = match% descending, tertiary = name asc
        scored.sort(Comparator
                .comparingInt(CareerResult::getScore).reversed()
                .thenComparingInt(CareerResult::getMatchPercent).reversed()
                .thenComparing(CareerResult::getCareerName));

        // Assign ranks and return top N
        List<CareerResult> topResults = scored.subList(0, Math.min(topN, scored.size()));
        for (int i = 0; i < topResults.size(); i++) {
            topResults.get(i).setRank(i + 1);
        }
        return topResults;
    }

    private Set<String> buildFactSet(UserProfile profile) {
        Set<String> facts = new HashSet<>();
        for (String interest : profile.getInterests()) {
            facts.add("interest:" + interest);
        }
        for (String subject : profile.getStrongSubjects()) {
            facts.add("subject:" + subject);
        }
        for (String skill : profile.getSkills()) {
            facts.add("skill:" + skill);
        }
        if (profile.getPreferredEnvironment() != null) {
            facts.add("environment:" + profile.getPreferredEnvironment());
        }
        if (profile.getEducationLevel() != null) {
            facts.add("education:" + profile.getEducationLevel());
        }
        return facts;
    }

    private int countMatches(String[] conditions, Set<String> userFacts) {
        int count = 0;
        for (String condition : conditions) {
            if (userFacts.contains(condition)) count++;
        }
        return count;
    }
}
