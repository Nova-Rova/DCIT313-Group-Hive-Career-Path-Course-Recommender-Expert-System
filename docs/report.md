# Knowledge Engineering Report
## Career Path & Course Recommender Expert System
### DCIT 313 — Group Project

---

## 1. Problem Statement

Students and young professionals face a critical challenge identifying which career best aligns with their unique combination of interests, academic strengths, and personal skills. Traditional career guidance is expensive, time-limited, and not always accessible. This expert system encodes domain knowledge from career research into a structured Prolog rule base, enabling automated, explainable career recommendations.

---

## 2. Objectives

- Build a rule-based Knowledge-Based System with 24+ career rules in SWI-Prolog
- Implement forward chaining inference to map user perceptions → career actions
- Score and rank multiple career matches using a transparent scoring algorithm
- Provide clear natural-language explanations for every recommendation
- Deliver an interactive Python terminal UI using the `pyswip` library
- Demonstrate the Intelligent Agent loop: **Perceive → Reason → Act**

---

## 3. Intelligent Agent Design

This system functions as an **Intelligent Agent** as required:

```
PERCEPTION  →  User answers questionnaire (interests, subjects, skills, environment)
REASONING   →  Prolog KB applies 24 IF-THEN rules via forward chaining
ACTION      →  System recommends top 3 careers + explains reasoning
```

| Agent Component | Implementation |
|---|---|
| Sensors / Perception | Python questionnaire (`main.py`) collects user inputs |
| Knowledge Base | `career_rules.pl` — 24 career rules + 24 career facts + explanations |
| Inference Engine | Prolog predicates: `get_recommendation/3`, `score_career/3`, `count_matches/3` |
| Actuators / Action | Ranked results displayed in terminal with match % and "Why this career?" |
| Bridge | `pyswip` library connects Python UI to SWI-Prolog KB |

---

## 4. System Architecture

```
┌─────────────────────────────────────────────────────────┐
│              USER (Terminal Interface)                    │
│  interests, subjects, skills, environment, education     │
└────────────────────────┬────────────────────────────────┘
                          │  Python dict
                          ▼
┌─────────────────────────────────────────────────────────┐
│         INFERENCE INTERFACE  (interface/main.py)         │
│                                                          │
│  1. build_fact_list()  →  converts profile to Prolog     │
│                           fact list string               │
│  2. run_inference()    →  queries Prolog via pyswip      │
│  3. Sorts results by score, match%, name                 │
│  4. print_results()    →  displays ranked recommendations│
└────────────────────────┬────────────────────────────────┘
                          │  pyswip  (Python ↔ SWI-Prolog bridge)
                          ▼
┌─────────────────────────────────────────────────────────┐
│        KNOWLEDGE BASE  (knowledge_base/career_rules.pl)  │
│                                                          │
│  career/6          24 career facts (name, desc, etc.)    │
│  career_rule/2     24 IF-THEN rules (conditions list)    │
│  why/2             24 explanation strings                 │
│  get_recommendation/3  scores and filters careers        │
│  get_career_info/7     retrieves full career details      │
│  score_career/3    counts matched conditions             │
│  count_matches/3   recursive list matching               │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│              EXPLANATION MODULE                          │
│  why/2 facts map each CareerID → human-readable reason  │
│  "Recommended because: Interest=X + Subject=Y + ..."    │
└─────────────────────────────────────────────────────────┘
```

---

## 5. Knowledge Acquisition Sources

Knowledge for the 24 career rules was acquired from the following sources:

| Source | What Was Used |
|---|---|
| **O*NET Online** (onetonline.org) | Required skills and knowledge areas per occupation |
| **BLS Occupational Outlook Handbook** (bls.gov/ooh) | Career growth rates, job outlook statistics |
| **MIT OpenCourseWare / Stanford curriculum** | Subject-to-career academic pathway mapping |
| **IEEE / ACM guidelines** | Engineering and computing career requirements |
| **AMA (American Medical Association)** | Healthcare career educational requirements |
| **LinkedIn Workforce Insights 2024** | In-demand skills per industry sector |
| **Professional certification bodies** | Certification requirements (CompTIA, Cisco, CFA, etc.) |

### Knowledge Engineering Process

1. **Domain identification** — Selected 24 careers spanning Technology, Business, Health, Arts, and Engineering
2. **Attribute selection** — Identified 5 input dimensions: interest, subject, skill, environment, education
3. **Rule elicitation** — Mapped each career to 3–5 distinguishing conditions from O*NET and BLS data
4. **Rule validation** — Cross-checked rules against real university admission requirements
5. **Conflict analysis** — Ensured no two rules are identical; added environment/education conditions to differentiate similar careers (e.g. Software Developer vs Cybersecurity Analyst)

---

## 6. Rule Design Quality

### Rule Quality Standards Applied

| Anti-pattern (Avoided) | Best Practice (Used) |
|---|---|
| `IF likes_computers → Software Engineer` | `IF interest=technology AND subject=mathematics AND skill=problem_solving THEN Software Developer` |
| `IF creative → Designer` | `IF interest=arts AND skill=creativity AND skill=visual_arts AND environment=remote THEN Graphic Designer` |
| Duplicate rules | Each career has a unique combination of conditions |

### Sample Rules

```prolog
% RULE 1 — Software Developer
career_rule(software_developer, [
    interest(technology),
    subject(mathematics),
    skill(problem_solving)
]).

% RULE 4 — AI/ML Engineer (more specific than Data Scientist)
career_rule(ai_engineer, [
    interest(technology),
    subject(mathematics),
    skill(analytical_thinking),
    skill(problem_solving)
]).

% RULE 6 — Medical Doctor
career_rule(medical_doctor, [
    interest(health),
    subject(biology),
    skill(analytical_thinking),
    skill(helping_people)
]).

% RULE 24 — Entrepreneur
career_rule(entrepreneur, [
    interest(business),
    skill(creativity),
    skill(leadership),
    skill(problem_solving)
]).
```

---

## 7. Inference Engine — Forward Chaining Explained

### Algorithm

```
1. COLLECT inputs  →  build UserFacts = [interest(X), subject(Y), skill(Z), ...]

2. FOR each career_rule(CareerID, Conditions) in knowledge base:
     score = COUNT conditions that are present in UserFacts
     total = LENGTH of Conditions list
     match% = (score / total) × 100

3. FILTER  →  keep only careers where score >= 2

4. SORT    →  primary: score DESC
              secondary: match% DESC
              tertiary: name ASC (tie-breaking)

5. RETURN  →  top 3 results with full details and explanation
```

### Prolog Predicates

```prolog
% Entry point called from Python:
get_recommendation(UserFacts, CareerID, Score) :-
    career(CareerID, _, _, _, _, _),
    score_career(CareerID, UserFacts, Score),
    Score >= 2.

% Scoring (recursive list traversal):
count_matches([], _, 0).
count_matches([H|T], Facts, Score) :-
    count_matches(T, Facts, Rest),
    (member(H, Facts) -> Score is Rest + 1 ; Score is Rest).
```

### Conflict Resolution Strategy

| Priority | Key | Rationale |
|---|---|---|
| 1st | Raw score (DESC) | More matched conditions = stronger recommendation |
| 2nd | Match percentage (DESC) | Normalises for rules with different numbers of conditions |
| 3rd | Career name (ASC) | Deterministic tie-break for reproducibility |

---

## 8. Test Cases

| Test | Inputs | Expected Top Result | Pass? |
|---|---|---|---|
| TC-01 | Technology + Mathematics + Problem Solving | Software Developer | ✓ |
| TC-02 | Health + Biology + Helping People + Analytical Thinking | Medical Doctor | ✓ |
| TC-03 | Business + Communication + Leadership + Creativity | Marketing Manager | ✓ |
| TC-04 | Arts + Creativity + Visual Arts + Remote | Graphic Designer | ✓ |
| TC-05 | Engineering + Physics + Mathematics + Problem Solving | Mechanical/Civil Engineer | ✓ |
| TC-06 | Technology + Mathematics + Analytical + Problem Solving | AI/ML Engineer | ✓ |
| TC-07 | Health + Biology + Empathy + Communication | Psychologist | ✓ |
| TC-08 | Communication + Helping People + Leadership + School | Teacher | ✓ |
| TC-09 | Business + Creativity + Leadership + Problem Solving | Entrepreneur | ✓ |
| TC-10 | (empty profile) | No results | ✓ |

---

## 9. System Limitations

- **Not a replacement for professional career counseling** — a human counselor brings contextual judgment this system cannot provide
- **Rule-based only (not AI/ML)** — the system applies fixed logical rules; it does not learn from user data or adapt over time
- **Limited career scope** — only 24 career paths are defined; many valid careers are not covered
- **No personality assessment** — does not incorporate MBTI, Holland Codes, or similar frameworks
- **Binary conditions** — each fact is either present or absent; there is no weighted preference scale
- **Geographic context not considered** — job market conditions vary significantly by country and region
- **Self-reported inputs** — the accuracy of recommendations depends on honest and accurate self-assessment by the user

---

## 10. Repository Structure

```
DCIT313-Group[Name]-CareerRecommender/
├── knowledge_base/
│   └── career_rules.pl       ← 24 IF-THEN rules + career facts + explanations
├── interface/
│   └── main.py               ← Python UI using pyswip
├── docs/
│   ├── report.md             ← This file (Knowledge Engineering Report)
│   └── last_report.txt       ← Auto-generated after each session
└── README.md
```

---

## 11. Setup Instructions

```bash
# 1. Install SWI-Prolog (Mac)
brew install swi-prolog

# 2. Install Python dependencies
pip install pyswip

# 3. Run the system
cd interface
python main.py
```
