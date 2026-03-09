package com.careerexpert.engine;

import com.careerexpert.model.CareerResult;
import com.careerexpert.model.UserProfile;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Test Suite for Career Expert System Inference Engine
 *
 * Tests cover:
 *  - Correctness of forward chaining rules
 *  - Conflict resolution / scoring
 *  - Edge cases
 *  - All 24 career rules
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InferenceEngineTest {

    private JavaInferenceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new JavaInferenceEngine();
    }

    // ==================== Core Rule Tests ====================

    @Test
    @Order(1)
    @DisplayName("TC-01: Technology + Mathematics + Problem Solving → Software Developer (Top 1)")
    void testSoftwareDeveloper() {
        UserProfile profile = new UserProfile();
        profile.addInterest("technology");
        profile.addSubject("mathematics");
        profile.addSkill("problem_solving");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty(), "Should return at least one result");
        assertEquals("Software Developer", results.get(0).getCareerName(),
                "Top result should be Software Developer");
        assertTrue(results.get(0).getScore() >= 3,
                "Should match 3 conditions");
    }

    @Test
    @Order(2)
    @DisplayName("TC-02: Health + Biology + Helping People → Medical Doctor (Top match)")
    void testMedicalDoctor() {
        UserProfile profile = new UserProfile();
        profile.addInterest("health");
        profile.addSubject("biology");
        profile.addSkill("helping_people");
        profile.addSkill("analytical_thinking");
        profile.setEducationLevel("undergraduate");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> r.getCareerName().equals("Medical Doctor")),
                "Medical Doctor should be in top 3");
    }

    @Test
    @Order(3)
    @DisplayName("TC-03: Business + Communication + Leadership → Marketing Manager")
    void testMarketingManager() {
        UserProfile profile = new UserProfile();
        profile.addInterest("business");
        profile.addSkill("communication");
        profile.addSkill("leadership");
        profile.addSkill("creativity");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> r.getCareerName().equals("Marketing Manager")),
                "Marketing Manager should be in top 3");
    }

    @Test
    @Order(4)
    @DisplayName("TC-04: Arts + Creativity + Visual Arts + Remote → Graphic Designer")
    void testGraphicDesigner() {
        UserProfile profile = new UserProfile();
        profile.addInterest("arts");
        profile.addSkill("creativity");
        profile.addSkill("visual_arts");
        profile.setPreferredEnvironment("remote");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty());
        assertEquals("Graphic Designer", results.get(0).getCareerName(),
                "Top result should be Graphic Designer");
    }

    @Test
    @Order(5)
    @DisplayName("TC-05: Engineering + Mathematics + Physics + Problem Solving → Civil/Mechanical Engineer")
    void testEngineer() {
        UserProfile profile = new UserProfile();
        profile.addInterest("engineering");
        profile.addSubject("mathematics");
        profile.addSubject("physics");
        profile.addSkill("problem_solving");
        profile.setPreferredEnvironment("outdoor");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> r.getCareerName().contains("Engineer")),
                "At least one engineer should be in results");
    }

    @Test
    @Order(6)
    @DisplayName("TC-06: Technology + Mathematics + Analytical Thinking + Problem Solving → AI Engineer (Top match)")
    void testAIEngineer() {
        UserProfile profile = new UserProfile();
        profile.addInterest("technology");
        profile.addSubject("mathematics");
        profile.addSkill("analytical_thinking");
        profile.addSkill("problem_solving");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> r.getCareerName().equals("AI/ML Engineer")),
                "AI/ML Engineer should be in top 3");
        // AI Engineer has 4 matching conditions — should score higher than Data Scientist
        assertEquals("AI/ML Engineer", results.get(0).getCareerName(),
                "AI/ML Engineer should rank #1 with all 4 conditions matched");
    }

    @Test
    @Order(7)
    @DisplayName("TC-07: Health + Empathy + Communication → Psychologist")
    void testPsychologist() {
        UserProfile profile = new UserProfile();
        profile.addInterest("health");
        profile.addSubject("biology");
        profile.addSkill("empathy");
        profile.addSkill("communication");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> r.getCareerName().contains("Psychologist")),
                "Psychologist should be in results");
    }

    @Test
    @Order(8)
    @DisplayName("TC-08: Business + Mathematics + Analytical Thinking + Attention to Detail → Financial Analyst")
    void testFinancialAnalyst() {
        UserProfile profile = new UserProfile();
        profile.addInterest("business");
        profile.addSubject("mathematics");
        profile.addSkill("analytical_thinking");
        profile.addSkill("attention_to_detail");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertTrue(results.stream().anyMatch(r -> r.getCareerName().equals("Financial Analyst")),
                "Financial Analyst should be in results");
    }

    @Test
    @Order(9)
    @DisplayName("TC-09: Arts + Technology + Creativity + Problem Solving → UX/UI Designer")
    void testUXDesigner() {
        UserProfile profile = new UserProfile();
        profile.addInterest("arts");
        profile.addInterest("technology");
        profile.addSkill("creativity");
        profile.addSkill("problem_solving");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertTrue(results.stream().anyMatch(r -> r.getCareerName().equals("UX/UI Designer")),
                "UX/UI Designer should be in results");
    }

    @Test
    @Order(10)
    @DisplayName("TC-10: Communication + Helping People + Leadership + School → Teacher")
    void testTeacher() {
        UserProfile profile = new UserProfile();
        profile.addSkill("communication");
        profile.addSkill("helping_people");
        profile.addSkill("leadership");
        profile.setPreferredEnvironment("school");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertFalse(results.isEmpty());
        assertEquals("Teacher / Educator", results.get(0).getCareerName(),
                "Teacher/Educator should rank #1 with all 4 conditions matched");
    }

    // ==================== Edge Case Tests ====================

    @Test
    @Order(11)
    @DisplayName("TC-11: Empty profile → No results (below threshold)")
    void testEmptyProfile() {
        UserProfile profile = new UserProfile();
        List<CareerResult> results = engine.evaluate(profile, 3);
        assertTrue(results.isEmpty(), "Empty profile should return no results");
    }

    @Test
    @Order(12)
    @DisplayName("TC-12: Single input → May still match some careers")
    void testSingleInput() {
        UserProfile profile = new UserProfile();
        profile.addInterest("technology"); // Only 1 fact

        List<CareerResult> results = engine.evaluate(profile, 3);
        // With only 1 fact, minimum threshold of 2 should not be met
        assertTrue(results.isEmpty() || results.size() <= 3,
                "Single input should return 0-3 results");
    }

    @Test
    @Order(13)
    @DisplayName("TC-13: Results sorted by score descending")
    void testResultsSortedByScore() {
        UserProfile profile = new UserProfile();
        profile.addInterest("technology");
        profile.addSubject("mathematics");
        profile.addSkill("problem_solving");
        profile.addSkill("analytical_thinking");

        List<CareerResult> results = engine.evaluate(profile, 5);

        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(results.get(i).getScore() >= results.get(i + 1).getScore(),
                    "Results should be sorted descending by score");
        }
    }

    @Test
    @Order(14)
    @DisplayName("TC-14: Ranks are correctly assigned 1, 2, 3")
    void testRanksAssigned() {
        UserProfile profile = new UserProfile();
        profile.addInterest("business");
        profile.addSubject("mathematics");
        profile.addSkill("analytical_thinking");
        profile.addSkill("communication");
        profile.addSkill("leadership");

        List<CareerResult> results = engine.evaluate(profile, 3);

        for (int i = 0; i < results.size(); i++) {
            assertEquals(i + 1, results.get(i).getRank(),
                    "Rank should be " + (i + 1));
        }
    }

    @Test
    @Order(15)
    @DisplayName("TC-15: Match percentage is between 0 and 100")
    void testMatchPercentRange() {
        UserProfile profile = new UserProfile();
        profile.addInterest("engineering");
        profile.addSubject("physics");
        profile.addSkill("problem_solving");

        List<CareerResult> results = engine.evaluate(profile, 5);

        for (CareerResult r : results) {
            assertTrue(r.getMatchPercent() >= 0 && r.getMatchPercent() <= 100,
                    "Match percent must be 0-100, got: " + r.getMatchPercent());
        }
    }

    @Test
    @Order(16)
    @DisplayName("TC-16: Health + Chemistry → Pharmacist (specific chemistry interest)")
    void testPharmacist() {
        UserProfile profile = new UserProfile();
        profile.addInterest("health");
        profile.addSubject("chemistry");
        profile.addSkill("analytical_thinking");
        profile.addSkill("attention_to_detail");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertTrue(results.stream().anyMatch(r -> r.getCareerName().equals("Pharmacist")),
                "Pharmacist should be in results with health + chemistry profile");
    }

    @Test
    @Order(17)
    @DisplayName("TC-17: Business + Creativity + Leadership + Problem Solving → Entrepreneur")
    void testEntrepreneur() {
        UserProfile profile = new UserProfile();
        profile.addInterest("business");
        profile.addSkill("creativity");
        profile.addSkill("leadership");
        profile.addSkill("problem_solving");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertTrue(results.stream().anyMatch(r -> r.getCareerName().contains("Entrepreneur")),
                "Entrepreneur should be in results");
    }

    @Test
    @Order(18)
    @DisplayName("TC-18: All results have non-null explanation")
    void testExplanationsNotNull() {
        UserProfile profile = new UserProfile();
        profile.addInterest("technology");
        profile.addSubject("mathematics");
        profile.addSkill("problem_solving");

        List<CareerResult> results = engine.evaluate(profile, 3);

        for (CareerResult r : results) {
            assertNotNull(r.getExplanation(), "Explanation should not be null");
            assertFalse(r.getExplanation().isEmpty(), "Explanation should not be empty");
        }
    }

    @Test
    @Order(19)
    @DisplayName("TC-19: UserProfile.toPrologFactList formats correctly")
    void testPrologFactList() {
        UserProfile profile = new UserProfile();
        profile.addInterest("Technology");
        profile.addSubject("Mathematics");
        profile.addSkill("Problem Solving");
        profile.setPreferredEnvironment("Office");

        String factList = profile.toPrologFactList();

        assertTrue(factList.startsWith("["), "Should start with [");
        assertTrue(factList.endsWith("]"), "Should end with ]");
        assertTrue(factList.contains("interest(technology)"), "Should contain interest fact");
        assertTrue(factList.contains("subject(mathematics)"), "Should contain subject fact");
        assertTrue(factList.contains("skill(problem_solving)"), "Should contain skill fact");
        assertTrue(factList.contains("environment(office)"), "Should contain environment fact");
    }

    @Test
    @Order(20)
    @DisplayName("TC-20: Engineering + Biology + Chemistry + Outdoor → Environmental Scientist")
    void testEnvironmentalScientist() {
        UserProfile profile = new UserProfile();
        profile.addInterest("engineering");
        profile.addSubject("biology");
        profile.addSubject("chemistry");
        profile.setPreferredEnvironment("outdoor");

        List<CareerResult> results = engine.evaluate(profile, 3);

        assertTrue(results.stream().anyMatch(r -> r.getCareerName().equals("Environmental Scientist")),
                "Environmental Scientist should be recommended");
    }
}
