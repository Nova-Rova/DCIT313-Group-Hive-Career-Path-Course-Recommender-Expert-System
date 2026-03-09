# 🎓 Career Path & Course Recommender System
### Rule-Based Expert System — Prolog + Java

---

## Overview

A rule-based expert system that recommends personalized career paths and academic courses based on user interests, strong subjects, skills, and work environment preferences.

**Languages**: Prolog (Knowledge Base + Inference Engine) + Java (GUI + Bridge)  
**Architecture**: Forward Chaining | 24 Career Rules | Scoring & Ranking | Explanation Facility

---

## Quick Start

```bash
# Make script executable (Linux/Mac)
chmod +x run.sh
./run.sh

# Windows: build manually
cd java
mvn clean package
java -jar target/CareerExpertSystem.jar
```

---

## Project Structure

```
CareerExpertSystem/
├── prolog/
│   ├── knowledge_base.pl      ← 24 IF-THEN rules + career facts
│   ├── inference_engine.pl    ← Forward chaining + scoring
│   └── prolog_server.pl       ← Java↔Prolog communication bridge
├── java/
│   └── src/main/java/com/careerexpert/
│       ├── model/             ← UserProfile, CareerResult
│       ├── engine/            ← PrologBridge, JavaInferenceEngine, Tests
│       ├── ui/                ← MainWindow (Swing GUI)
│       └── util/              ← ReportExporter (HTML + text)
├── docs/
│   └── DOCUMENTATION.md       ← Full documentation
└── run.sh                     ← Build + launch script
```

---

## How It Works

1. User answers questionnaire (interests, subjects, skills, environment)
2. Java builds a Prolog fact list: `[interest(technology), skill(problem_solving), ...]`
3. Prolog inference engine scores each of the 24 career rules using forward chaining
4. Results sorted: primary by score, secondary by match %, tertiary alphabetically
5. Top 3 careers displayed with match percentage + "Why this career?" explanation
6. User can export a full HTML or text report

---

## Testing Prolog Directly

```bash
cd prolog
swipl knowledge_base.pl inference_engine.pl

?- query([interest(technology), subject(mathematics), skill(problem_solving)]).
?- query([interest(health), subject(biology), skill(helping_people)]).
?- test_system.
```

---

## Requirements

- Java 17+
- Maven 3.8+
- SWI-Prolog 9.x *(optional — system has Java fallback)*

---

## Limitations

- Rule-based only (not AI/ML)
- 24 predefined career paths
- Not a replacement for professional career counseling
- No geographic or market context

---

*Expert System | Forward Chaining Inference | Prolog + Java*
