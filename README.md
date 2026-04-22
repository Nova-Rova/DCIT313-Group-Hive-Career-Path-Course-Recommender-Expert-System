# DCIT313-Group-Hive-CareerPath-course-Recomender-Expert-System

## Career Path & Course Recommender Expert System

**DCIT 313 — Group Project**

---

## Group Members
| Name                          | GitHub Username  | Student ID | Role                                  |
|-------------------------------|------------------|------------|---------------------------------------|
| James Preprah Ankrah          | Ankila-coder     | 22037210   | Knowledge Base Design (Prolog Rules)  |
| George Antwi                  | gkantwi001       | 22014943   | Python Interface & pyswip Integration |
| Joel Asante                   | Asante-jpg       | 22238914   | Documentation & Knowledge Engineering |
| Muhammad Amanullah            | Heisamanullah    | 22017780   | Testing & Validation                  |
| Amartey Felix Nii Laryea      | Nova-Rova        | 22107810   | Knowledge Base Design                 |
| Benjamin Ato Davis            | alakazem         | 22046566   | Python Interface                      |
| Glago Gideon Elorm            | glaogideonelorm  | 22128981   | Programmer                            |
---

## System Description

A **rule-based Expert System / Knowledge-Based System** that acts as an Intelligent Agent to recommend suitable career paths and academic courses based on user inputs.

**Intelligent Agent Mapping:**

- **Perceptions (Inputs):** User's interests, strong subjects, skills, preferred work environment, and education level
- **Actions (Outputs):** Top 3 ranked career recommendations with match percentage, suggested degree, certifications, career outlook, and explanation of why each career was recommended

---

## Technical Stack

| Component       | Technology          |
| --------------- | ------------------- |
| Logic Engine    | SWI-Prolog          |
| User Interface  | Python 3 + `pyswip` |
| Version Control | GitHub              |

---

## Project Structure

```
├── knowledge_base/
│   └── career_rules.pl     ← 24 IF-THEN rules, career facts, explanations
├── interface/
│   └── main.py             ← Python UI bridging user ↔ Prolog KB via pyswip
├── docs/
│   ├── report.md           ← Knowledge Engineering Report
│   └── last_report.txt     ← Auto-generated session report
└── README.md
```

---

## Setup & Run

### Prerequisites

```bash
# Install SWI-Prolog (Mac)
brew install swi-prolog

# Install pyswip
pip install pyswip
```

### Run

```bash
cd interface
python main.py
```

---

## Sample Interaction

```
  ┌─ Q: Areas of Interest  (select all that apply)
  │      1  Technology & Computing
  │      2  Business & Finance
  │      3  Health & Medicine
  │      4  Arts & Design
  │      5  Engineering & Construction
  └▶ Your choices: 1

  ┌─ Q: Strong Academic Subjects
  └▶ Your choices: 1 4     (Mathematics + Physics)

  ┌─ Q: Key Skills
  └▶ Your choices: 1 2     (Problem Solving + Analytical Thinking)

  ✔  Top 3 Career Recommendations

  ╔══════════════════════════════════════╗
  ║  #1  AI/ML Engineer   100% match    ║
  ║  Match  ████████████████████  4/4   ║
  ║  💡 Why? Interest=Technology +      ║
  ║     Subject=Mathematics + Skills=   ║
  ║     Analytical Thinking & Problem   ║
  ║     Solving                         ║
  ╚══════════════════════════════════════╝
```

---

## Knowledge Base Summary

- **24 career rules** covering Technology, Business, Health, Arts, and Engineering
- Each rule maps 3–5 user attributes (interest + subject + skill + environment) to a career
- Forward chaining inference engine implemented in Prolog
- Conflict resolution: score → match percentage → alphabetical

---

## Testing the Project ✅

Although the system is primarily interactive, you can verify it works automatically by running the supplied pytest suite.

1. Install development requirements (pytest). In your virtual environment:

   ```bash
   pip install pytest
   ```

2. Run the tests from the repository root:
   ```bash
   pytest
   ```

The basic tests launch the interface script with example answers and check that a known career name appears in the output, plus a scenario with no selections to confirm the warning message.

> You can also manually exercise the program by following the **Sample Interaction** steps above — if the prompts appear and a recommendation is returned, the project is functioning correctly.

---

_DCIT 313 — Intelligent Systems | University of Ghana_
