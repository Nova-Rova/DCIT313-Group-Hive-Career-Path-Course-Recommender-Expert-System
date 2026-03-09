# Career Path & Course Recommender System
## Complete System Documentation

---

## 1. Problem Statement

Students and young professionals worldwide face a critical challenge: identifying the career path best aligned with their unique combination of interests, academic strengths, and personal skills. Traditional career guidance is expensive, time-limited, and not always accessible. This project delivers a **Rule-Based Expert System** that encodes domain knowledge from career research and occupational data into a structured IF-THEN rule base, enabling automated, explainable career recommendations for any user.

---

## 2. Objectives

- Build a rule-based expert system with at least 20 career rules
- Implement forward chaining inference to match user profiles to careers
- Rank multiple career matches using a scoring system
- Provide clear explanations for every recommendation ("Why this career?")
- Deliver a clean Java Swing GUI questionnaire interface
- Enable export of results as HTML/text reports
- Separate concerns cleanly: UI, logic, rules, and data model

---

## 3. System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         USER INTERFACE (Java Swing)                  │
│  WelcomePanel → QuestionnairePanel → ResultsPanel                    │
│  [MainWindow.java]                                                    │
└─────────────────────────────┬───────────────────────────────────────┘
                               │  UserProfile object
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      INFERENCE ENGINE LAYER                           │
│                                                                       │
│   ┌───────────────────────┐      ┌──────────────────────────────┐   │
│   │  PrologBridge.java    │ ───► │  SWI-Prolog Process          │   │
│   │  (Primary path)       │      │  prolog_server.pl            │   │
│   └───────────────────────┘      └──────────────────────────────┘   │
│              │                              │                         │
│     (fallback if Prolog                     │                         │
│      not installed)                         ▼                         │
│   ┌───────────────────────┐      ┌──────────────────────────────┐   │
│   │  JavaInferenceEngine  │      │  knowledge_base.pl           │   │
│   │  (Mirrors Prolog rules│      │  inference_engine.pl         │   │
│   │   in pure Java)       │      │  (24 career rules, facts,   │   │
│   └───────────────────────┘      │   scoring predicates)        │   │
│                                  └──────────────────────────────┘   │
└─────────────────────────────┬───────────────────────────────────────┘
                               │  List<CareerResult>
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       EXPLANATION MODULE                              │
│   Maps each CareerID → human-readable "Why this career?" string      │
│   [rule_explanation/2 in Prolog | EXPLANATIONS map in Java]          │
└─────────────────────────────┬───────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         OUTPUT MODULE                                 │
│   ResultsPanel.java  →  ranked cards with match %, explanation       │
│   ReportExporter.java → HTML report / text file export               │
└─────────────────────────────────────────────────────────────────────┘
```

### Component Responsibilities

| Component | Language | Responsibility |
|---|---|---|
| `MainWindow.java` | Java | Screen navigation, top-level controller |
| `WelcomePanel.java` | Java | Splash screen |
| `QuestionnairePanel.java` | Java | Collects user inputs (interests, subjects, skills) |
| `ResultsPanel.java` | Java | Displays ranked career cards with explanations |
| `UserProfile.java` | Java | Data model for user inputs; converts to Prolog fact list |
| `CareerResult.java` | Java | Data model for a single recommendation result |
| `PrologBridge.java` | Java | Launches SWI-Prolog subprocess; sends/receives via stdio |
| `JavaInferenceEngine.java` | Java | Pure-Java mirror of Prolog rules (fallback engine) |
| `ReportExporter.java` | Java | Exports HTML and text reports |
| `knowledge_base.pl` | Prolog | Career facts, IF-THEN rules, scoring predicates |
| `inference_engine.pl` | Prolog | Forward chaining evaluation, ranking, result formatting |
| `prolog_server.pl` | Prolog | Stdin/stdout bridge server for Java ↔ Prolog communication |

---

## 4. Rule Base Design

### Rule Structure (Prolog Syntax)

```prolog
career_rule(CareerID, [
    interest(Value),
    subject(Value),
    skill(Value),
    environment(Value),
    education(Value)
]).
```

Each rule maps a career to a list of required conditions. The inference engine performs **forward chaining**: it iterates over all rules, counts how many conditions in each rule are satisfied by the user's fact set, and scores each career accordingly.

### Rule Quality Standards Applied

| Bad Rule (Avoided) | Better Rule (Used) |
|---|---|
| `IF likes_computers → Software Engineer` | `IF interest=Technology AND subject=Mathematics AND skill=Problem_Solving THEN Software Developer` |
| `IF creative → Designer` | `IF interest=Arts AND skill=Creativity AND skill=Visual_Arts AND environment=Remote THEN Graphic Designer` |

Rules are:
- **Clear** — each condition is a specific, measurable attribute
- **Non-conflicting** — different careers require different combinations
- **Not repetitive** — no two rules are identical
- **Logically structured** — conditions reflect real-world career requirements

### Sample Rules

```
RULE 01: Software Developer
  IF interest=Technology AND subject=Mathematics AND skill=Problem_Solving
  THEN career=Software Developer

RULE 04: AI/ML Engineer
  IF interest=Technology AND subject=Mathematics
     AND skill=Analytical_Thinking AND skill=Problem_Solving
  THEN career=AI/ML Engineer

RULE 06: Medical Doctor
  IF interest=Health AND subject=Biology
     AND skill=Analytical_Thinking AND skill=Helping_People
     AND education=Undergraduate
  THEN career=Medical Doctor

RULE 13: Business Analyst
  IF interest=Business AND skill=Analytical_Thinking
     AND skill=Communication AND environment=Office
  THEN career=Business Analyst

RULE 17: Graphic Designer
  IF interest=Arts AND skill=Creativity AND skill=Visual_Arts
     AND environment=Remote
  THEN career=Graphic Designer

RULE 24: Entrepreneur
  IF interest=Business AND skill=Creativity
     AND skill=Leadership AND skill=Problem_Solving
  THEN career=Entrepreneur
```

---

## 5. Inference Engine - Forward Chaining Explained

### Algorithm

```
1. COLLECT user inputs → build UserFacts = {interest:X, subject:Y, skill:Z, ...}

2. FOR each career rule R in KnowledgeBase:
     score(R) = COUNT of conditions in R that are present in UserFacts
     total(R) = total number of conditions in R
     matchPct(R) = score(R) / total(R) × 100

3. FILTER: keep only careers where score(R) ≥ 2 (minimum threshold)

4. SORT: primary = score DESC, secondary = matchPct DESC, tertiary = name ASC

5. RETURN: top 3 results with explanation for each
```

### Conflict Resolution Strategy

When multiple careers match the same score:
1. **Primary key**: Raw score (absolute number of matched conditions)
2. **Secondary key**: Match percentage (normalizes for rule length differences)
3. **Tertiary key**: Career name alphabetically (ensures deterministic output)

**Example**: A user matches 3 conditions for both Software Developer and Data Scientist. Software Developer's rule has 3 total conditions (100% match) vs Data Scientist's rule with 3 total (also 100%). In this case, both rank equally and secondary/tertiary keys decide order.

---

## 6. Java ↔ Prolog Communication Protocol

```
Java → Prolog:   recommend('[interest(technology),skill(problem_solving)]').
                 (Prolog term written to stdin, terminated by .\n)

Prolog → Java:   RESULT_COUNT|3
                 RESULT|1|software_developer|Software Developer|3|100|...description...|...skills...|...degree...|...certs...|...outlook...|...explanation...
                 RESULT|2|data_scientist|Data Scientist|2|67|...
                 RESULT|3|cybersecurity_analyst|Cybersecurity Analyst|2|50|...
```

Each RESULT line is pipe-delimited with 12 fields. Java's `PrologBridge.parseResultLine()` splits on `|` and constructs a `CareerResult` object.

---

## 7. Knowledge Acquisition

Sources used to design the 24 career rules:

| Source | Usage |
|---|---|
| O*NET Online (onetonline.org) | Occupational requirements, skills per career |
| BLS Occupational Outlook Handbook | Career outlook statistics, growth rates |
| University curriculum analysis (MIT, Stanford, Cambridge) | Subject-to-career mappings |
| IEEE / ACM / AMA professional body guidelines | Certification requirements |
| Career research aggregators (Indeed, LinkedIn Insights) | Industry skill demand trends |

---

## 8. Test Cases

| TC | Inputs | Expected Top Result | Rationale |
|---|---|---|---|
| TC-01 | Technology + Mathematics + Problem Solving | Software Developer | Perfect 3/3 match |
| TC-02 | Health + Biology + Helping People + Analytical Thinking + Undergraduate | Medical Doctor | 5/5 match |
| TC-03 | Business + Communication + Leadership + Creativity | Marketing Manager | 4/4 match |
| TC-04 | Arts + Creativity + Visual Arts + Remote | Graphic Designer | 4/4 match |
| TC-06 | Technology + Mathematics + Analytical Thinking + Problem Solving | AI/ML Engineer | 4/4 match |
| TC-10 | Communication + Helping People + Leadership + School | Teacher/Educator | 4/4 match |
| TC-11 | (empty profile) | No results | Below threshold |
| TC-13 | Mixed tech inputs | Results sorted desc by score | Conflict resolution |
| TC-17 | Business + Creativity + Leadership + Problem Solving | Entrepreneur | 4/4 match |

---

## 9. System Limitations

- **Not a replacement for professional career counseling**: A human counselor brings contextual judgment this system cannot provide.
- **Rule-based (not AI/ML)**: The system applies fixed logical rules; it does not learn from user data or improve over time.
- **Limited career scope**: Only 24 career paths are defined. Many valid careers are not covered.
- **No personality assessment**: The system does not incorporate personality types (e.g., MBTI, Holland Codes).
- **Binary conditions**: Each fact is either present or absent; there is no weighted preference scale.
- **English only**: All inputs and outputs are in English.
- **Geographic context not considered**: Job market conditions vary significantly by country and region.

---

## 10. Setup & Running Instructions

### Prerequisites
- Java 17+
- Maven 3.8+
- SWI-Prolog 9.x (optional — system falls back to Java engine if not installed)

### Run the Java GUI

```bash
# Navigate to java directory
cd CareerExpertSystem/java

# Build
mvn clean package

# Run
java -jar target/CareerExpertSystem.jar
```

### Run Prolog Knowledge Base Standalone (SWI-Prolog)

```bash
cd CareerExpertSystem/prolog

# Load and test in interactive console
swipl knowledge_base.pl inference_engine.pl

# Then query:
?- query([interest(technology), subject(mathematics), skill(problem_solving)]).
?- query([interest(health), subject(biology), skill(helping_people)]).
?- test_system.
```

### Run Java Tests

```bash
cd CareerExpertSystem/java
mvn test
```

---

## 11. Project File Structure

```
CareerExpertSystem/
├── prolog/
│   ├── knowledge_base.pl        ← Career facts + IF-THEN rules (24 rules)
│   ├── inference_engine.pl      ← Forward chaining evaluator + ranking
│   └── prolog_server.pl         ← Java↔Prolog bridge server (stdin/stdout)
│
├── java/
│   ├── pom.xml
│   └── src/main/java/com/careerexpert/
│       ├── model/
│       │   ├── UserProfile.java  ← User input data model
│       │   └── CareerResult.java ← Result data model
│       ├── engine/
│       │   ├── PrologBridge.java       ← Prolog subprocess manager
│       │   ├── JavaInferenceEngine.java← Pure-Java fallback engine
│       │   └── InferenceEngineTest.java← JUnit 5 test suite (20 tests)
│       ├── ui/
│       │   └── MainWindow.java   ← Swing GUI (WelcomePanel + QuestionnairePanel + ResultsPanel)
│       └── util/
│           └── ReportExporter.java ← HTML + text report exporter
│
└── docs/
    └── DOCUMENTATION.md          ← This file
```

---

## 12. Career Outlook Summary (24 Careers)

| Career | Growth | Difficulty |
|---|---|---|
| Data Scientist | ⭐⭐⭐⭐⭐ 36% | High |
| Cybersecurity Analyst | ⭐⭐⭐⭐⭐ 35% | High |
| Psychologist | ⭐⭐⭐⭐⭐ 22% | High |
| Business Analyst | ⭐⭐⭐⭐ 14% | Medium |
| UX/UI Designer | ⭐⭐⭐⭐ 13% | Medium |
| Nurse | ⭐⭐⭐⭐ 9% | Medium-High |
| Financial Analyst | ⭐⭐⭐⭐ 9% | High |
| Civil Engineer | ⭐⭐⭐ 8% | High |
| Environmental Scientist | ⭐⭐⭐ 8% | Medium |
| Mechanical Engineer | ⭐⭐⭐ 7% | High |
| Electrical Engineer | ⭐⭐⭐ 7% | High |
| Accountant | ⭐⭐⭐ 7% | Medium |
| Marketing Manager | ⭐⭐⭐ 10% | Medium |
| Lawyer | ⭐⭐⭐ 10% | Very High |
| Biomedical Engineer | ⭐⭐⭐ 10% | High |
| Software Developer | ⭐⭐⭐⭐ 25% | High |
| Medical Doctor | ⭐⭐⭐ 3% | Very High |
| Pharmacist | ⭐⭐ 2% | High |
| Teacher/Educator | ⭐⭐⭐ stable | Medium |
| Graphic Designer | ⭐⭐ 3% | Medium |
| Architect | ⭐⭐ 3% | High |
| Network Engineer | ⭐⭐⭐ stable | Medium-High |
| AI/ML Engineer | ⭐⭐⭐⭐⭐ fastest | Very High |
| Entrepreneur | Variable | Very High |
