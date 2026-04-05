"""
DCIT 313 - Career Path & Course Recommender Expert System
Interface: main.py

This script acts as the bridge between the user and the Prolog
knowledge base using the pyswip library.

Architecture:
    User Input → Python (main.py) → pyswip → SWI-Prolog (career_rules.pl)
                                            ← Inference Results
                ← Ranked Career Recommendations + Explanations
"""
"""
main.py – User Interface & Inference Controller

This script serves as the main entry point of the Career Path & Course
Recommender Expert System. It provides a command-line interface that
collects user input, communicates with the Prolog knowledge base, and
displays ranked career recommendations.

Core Responsibilities:
- Loads and connects to the Prolog knowledge base using pyswip
- Collects user profile data (interests, subjects, skills, environment, education)
- Translates user input into Prolog facts
- Executes forward-chaining inference queries in Prolog
- Retrieves and processes matching career rules with scores
- Ranks and formats the top career recommendations
- Displays detailed explanations for each recommendation
- Optionally exports results to a report file

System Flow:
    User Input → Python Interface (main.py) → pyswip → Prolog Engine
                                                     → Rule Matching & Scoring
                ← Processed Results ← Ranked Career Recommendations

Key Features:
- Rule-based reasoning (symbolic AI, not machine learning)
- Match scoring and percentage calculation
- Structured and user-friendly CLI output
- Report generation for documentation

This file acts as the bridge between the user and the expert system,
handling both interaction and coordination of the inference process.
"""
import os
import sys
from pyswip import Prolog


KB_PATH = os.path.join(
    os.path.dirname(os.path.abspath(__file__)),
    "..", "knowledge_base", "career_rules.pl"
)

TOP_N = 3

MIN_SCORE = 2


class C:
    RESET  = "\033[0m"
    BOLD   = "\033[1m"
    BLUE   = "\033[94m"
    CYAN   = "\033[96m"
    GREEN  = "\033[92m"
    YELLOW = "\033[93m"
    RED    = "\033[91m"
    WHITE  = "\033[97m"
    DIM    = "\033[2m"

def bold(s):   return f"{C.BOLD}{s}{C.RESET}"
def blue(s):   return f"{C.BLUE}{s}{C.RESET}"
def cyan(s):   return f"{C.CYAN}{s}{C.RESET}"
def green(s):  return f"{C.GREEN}{s}{C.RESET}"
def yellow(s): return f"{C.YELLOW}{s}{C.RESET}"
def red(s):    return f"{C.RED}{s}{C.RESET}"
def dim(s):    return f"{C.DIM}{s}{C.RESET}"

def load_prolog() -> Prolog:
    """Load the Prolog knowledge base via pyswip."""
    prolog = Prolog()
    kb_path = os.path.normpath(KB_PATH)
    if not os.path.exists(kb_path):
        print(red(f"[ERROR] Knowledge base not found at: {kb_path}"))
        sys.exit(1)
    prolog_path = kb_path.replace("\\", "/")
    prolog.consult(prolog_path)
    return prolog


def build_fact_list(profile: dict) -> list:
    """
    Convert the user's profile dictionary into a list of Prolog terms.
    e.g. [interest(technology), subject(mathematics), skill(problem_solving)]
    """
    facts = []
    for interest in profile.get("interests", []):
        facts.append(f"interest({interest})")
    for subject in profile.get("subjects", []):
        facts.append(f"subject({subject})")
    for skill in profile.get("skills", []):
        facts.append(f"skill({skill})")
    if profile.get("environment"):
        facts.append(f"environment({profile['environment']})")
    if profile.get("education"):
        facts.append(f"education({profile['education']})")
    return facts


def run_inference(prolog: Prolog, profile: dict) -> list:
    """
    Forward chaining inference via pyswip.

    Queries Prolog for all careers that match >= MIN_SCORE conditions,
    retrieves full details, and returns a ranked list of results.
    """
    facts = build_fact_list(profile)
    if not facts:
        return []

    fact_list_str = "[" + ", ".join(facts) + "]"

    results = []

    query = f"get_recommendation({fact_list_str}, CareerID, Score)"
    matches = list(prolog.query(query))

    for match in matches:
        career_id = str(match["CareerID"])
        score     = int(match["Score"])

        detail_query = (
            f"get_career_info({career_id}, Name, Desc, Degree, Certs, Outlook, Why)"
        )
        details = list(prolog.query(detail_query))
        if not details:
            continue
        d = details[0]

        size_query = f"get_rule_size({career_id}, Size)"
        sizes = list(prolog.query(size_query))
        rule_size  = int(sizes[0]["Size"]) if sizes else 1
        match_pct  = round((score / rule_size) * 100)

        raw_certs = d["Certs"]
        if isinstance(raw_certs, list):
            certs = [str(c) for c in raw_certs]
        else:
            certs = [str(raw_certs)]

        results.append({
            "id":          career_id,
            "name":        str(d["Name"]),
            "description": str(d["Desc"]),
            "degree":      str(d["Degree"]),
            "certs":       certs,
            "outlook":     str(d["Outlook"]),
            "why":         str(d["Why"]),
            "score":       score,
            "rule_size":   rule_size,
            "match_pct":   match_pct,
        })

    results.sort(key=lambda r: (-r["score"], -r["match_pct"], r["name"]))

    for i, r in enumerate(results[:TOP_N]):
        r["rank"] = i + 1

    return results[:TOP_N]


OPTIONS = {
    "interests": {
        "label": "Areas of Interest",
        "choices": {
            "1": ("technology",   "Technology & Computing"),
            "2": ("business",     "Business & Finance"),
            "3": ("health",       "Health & Medicine"),
            "4": ("arts",         "Arts & Design"),
            "5": ("engineering",  "Engineering & Construction"),
        }
    },
    "subjects": {
        "label": "Strong Academic Subjects",
        "choices": {
            "1": ("mathematics",  "Mathematics"),
            "2": ("biology",      "Biology"),
            "3": ("chemistry",    "Chemistry"),
            "4": ("physics",      "Physics"),
            "5": ("economics",    "Economics"),
            "6": ("literature",   "Literature / Language"),
            "7": ("history",      "History / Social Studies"),
        }
    },
    "skills": {
        "label": "Key Skills",
        "choices": {
            "1":  ("problem_solving",     "Problem Solving"),
            "2":  ("analytical_thinking", "Analytical Thinking"),
            "3":  ("creativity",          "Creativity"),
            "4":  ("communication",       "Communication"),
            "5":  ("leadership",          "Leadership"),
            "6":  ("helping_people",      "Helping / Caring for People"),
            "7":  ("attention_to_detail", "Attention to Detail"),
            "8":  ("visual_arts",         "Visual Arts"),
            "9":  ("empathy",             "Empathy"),
            "10": ("technical_skills",    "Technical / Hands-on Skills"),
        }
    },
    "environment": {
        "label": "Preferred Work Environment",
        "choices": {
            "1": ("office",      "Office"),
            "2": ("hospital",    "Hospital / Clinic"),
            "3": ("outdoor",     "Outdoor / Field Work"),
            "4": ("remote",      "Remote / Work from Home"),
            "5": ("school",      "School / Educational Institution"),
            "6": ("laboratory",  "Laboratory"),
        }
    },
    "education": {
        "label": "Current Education Level",
        "choices": {
            "1": ("high_school",    "High School / SHS"),
            "2": ("undergraduate",  "Undergraduate (University)"),
            "3": ("postgraduate",   "Postgraduate (Masters/PhD)"),
        }
    },
}


def print_banner():
    print()
    print(blue("╔══════════════════════════════════════════════════════════╗"))
    print(blue("║") + bold("        🎓  Career Path & Course Recommender           ") + blue("║"))
    print(blue("║") + dim("        Rule-Based Expert System  •  DCIT 313          ") + blue("║"))
    print(blue("╚══════════════════════════════════════════════════════════╝"))
    print()


def print_section(title: str):
    print()
    print(cyan(f"  ┌─ {title} "))
    print(cyan("  │"))


def ask_multi(key: str) -> list:
    """Ask a multi-select question and return list of selected values."""
    cfg = OPTIONS[key]
    print_section(f"Q: {cfg['label']}  (select all that apply, e.g. 1 3 5)")
    for k, (val, label) in cfg["choices"].items():
        print(f"  │   {cyan(k):>4}  {label}")
    print(cyan("  │"))
    try:
        raw = input(cyan("  └▶ ") + "Your choices: ").strip()
    except KeyboardInterrupt:
        # User pressed Ctrl+C – treat as empty selection
        print()
        return []

    selected = []
    for token in raw.split():
        if token in cfg["choices"]:
            selected.append(cfg["choices"][token][0])
    return selected


def ask_single(key: str) -> str:
    """Ask a single-select question and return the selected value."""
    cfg = OPTIONS[key]
    print_section(f"Q: {cfg['label']}  (enter one number)")
    for k, (val, label) in cfg["choices"].items():
        print(f"  │   {cyan(k):>4}  {label}")
    print(cyan("  │"))
    try:
        raw = input(cyan("  └▶ ") + "Your choice: ").strip()
    except KeyboardInterrupt:
        print()
        return ""
    if raw in cfg["choices"]:
        return cfg["choices"][raw][0]
    return ""


def collect_profile() -> dict:
    """Run the interactive questionnaire and return the user's profile."""
    print(bold("\n  Please answer the following questions about yourself.\n"))
    profile = {}
    profile["interests"]   = ask_multi("interests")
    profile["subjects"]    = ask_multi("subjects")
    profile["skills"]      = ask_multi("skills")
    profile["environment"] = ask_single("environment")
    profile["education"]   = ask_single("education")
    return profile


def print_profile_summary(profile: dict):
    print()
    print(bold("  ── Your Profile ──────────────────────────────────────"))
    print(f"  Interests   : {', '.join(profile['interests']) or 'none'}")
    print(f"  Subjects    : {', '.join(profile['subjects'])  or 'none'}")
    print(f"  Skills      : {', '.join(profile['skills'])    or 'none'}")
    print(f"  Environment : {profile.get('environment') or 'not specified'}")
    print(f"  Education   : {profile.get('education')   or 'not specified'}")
    print(bold("  ──────────────────────────────────────────────────────"))


def print_results(results: list):
    print()
    if not results:
        print(red("  ✗  No careers matched your profile."))
        print(dim("     Try selecting more interests, subjects, or skills."))
        return

    print(bold(green(f"  ✔  Top {len(results)} Career Recommendation(s) for You\n")))

    for r in results:
        bar_filled = int(r["match_pct"] / 5)  
        bar = "█" * bar_filled + "░" * (20 - bar_filled)

        print(blue("  ╔══════════════════════════════════════════════════════════╗"))
        print(blue("  ║") + f"  {bold('#' + str(r['rank']))}  {bold(yellow(r['name']))} " +
              green(f"  {r['match_pct']}% match") + blue(""))
        print(blue("  ╠══════════════════════════════════════════════════════════╣"))
        print(blue("  ║") + f"  {cyan('Match')}       {bar}  {r['score']}/{r['rule_size']}")
        print(blue("  ║") + f"  {cyan('Description')} {r['description']}")
        print(blue("  ║") + f"  {cyan('Degree')}      {r['degree']}")
        print(blue("  ║") + f"  {cyan('Certs')}       {', '.join(r['certs'])}")
        print(blue("  ║") + f"  {cyan('Outlook')}     {r['outlook']}")
        print(blue("  ╠══════════════════════════════════════════════════════════╣"))
        print(blue("  ║") + f"  {bold('💡 Why this career?')}")
        why = r["why"]
        words = why.split()
        line = "  ║     "
        for word in words:
            if len(line) + len(word) > 62:
                print(blue("  ║") + dim(line))
                line = "  ║     " + word + " "
            else:
                line += word + " "
        if line.strip():
            print(blue("  ║") + dim(line))
        print(blue("  ╚══════════════════════════════════════════════════════════╝"))
        print()


def export_report(profile: dict, results: list):
    """Save results to a plain-text report file."""
    path = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        "..", "docs", "last_report.txt"
    )
    with open(path, "w") as f:
        f.write("CAREER PATH & COURSE RECOMMENDER - RESULTS REPORT\n")
        f.write("=" * 60 + "\n\n")
        f.write("USER PROFILE\n")
        f.write(f"  Interests   : {', '.join(profile['interests'])}\n")
        f.write(f"  Subjects    : {', '.join(profile['subjects'])}\n")
        f.write(f"  Skills      : {', '.join(profile['skills'])}\n")
        f.write(f"  Environment : {profile.get('environment', '')}\n")
        f.write(f"  Education   : {profile.get('education', '')}\n\n")
        f.write("TOP CAREER RECOMMENDATIONS\n")
        f.write("-" * 60 + "\n\n")
        if not results:
            f.write("No careers matched your profile.\n")
        else:
            for r in results:
                f.write(f"#{r['rank']}  {r['name']}  ({r['match_pct']}% match)\n")
                f.write(f"    Score       : {r['score']}/{r['rule_size']} conditions\n")
                f.write(f"    Description : {r['description']}\n")
                f.write(f"    Degree      : {r['degree']}\n")
                f.write(f"    Certs       : {', '.join(r['certs'])}\n")
                f.write(f"    Outlook     : {r['outlook']}\n")
                f.write(f"    Why         : {r['why']}\n\n")
        f.write("\nSYSTEM LIMITATIONS\n")
        f.write("- Rule-based expert system, not a replacement for professional counseling.\n")
        f.write("- Limited to 24 predefined career rules.\n")
        f.write("- Does not incorporate AI/ML; uses symbolic forward chaining logic.\n")
    print(dim(f"\n  Report saved to: {os.path.normpath(path)}"))



def main():
    print_banner()
    print(dim("  Loading Prolog knowledge base..."))

    try:
        prolog = load_prolog()
    except Exception as e:
        print(red(f"  [ERROR] Failed to load Prolog KB: {e}"))
        sys.exit(1)

    print(green("  ✔  Knowledge base loaded (24 career rules).\n"))

    while True:
        try:
            profile = collect_profile()
        except KeyboardInterrupt:
            print(red("\n  ✗  Input cancelled by user. Exiting."))
            break
        print_profile_summary(profile)

        if not profile["interests"] and not profile["subjects"] and not profile["skills"]:
            print(red("\n  ✗  Please select at least 2 inputs to get recommendations.\n"))
            continue

        print(dim("\n  Running inference engine..."))

        try:
            results = run_inference(prolog, profile)
        except Exception as e:
            print(red(f"\n  [ERROR] Inference failed: {e}"))
            print(dim("  Make sure SWI-Prolog is installed: brew install swi-prolog"))
            sys.exit(1)

        print_results(results)

        save = input(cyan("  Save report to file? (y/n): ")).strip().lower()
        if save == "y":
            export_report(profile, results)

        again = input(cyan("\n  Start over? (y/n): ")).strip().lower()
        if again != "y":
            print(bold(green("\n  Thank you for using the Career Recommender. Good luck! 🎓\n")))
            break


if __name__ == "__main__":
    main()
