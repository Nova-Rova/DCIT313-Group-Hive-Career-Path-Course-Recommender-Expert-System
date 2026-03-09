import os
import subprocess
import sys

def run_recommender(inputs: str) -> str:
    """Helper to run the Python interface with a sequence of newline-separated answers."""
    script = os.path.join(os.path.dirname(__file__), "..", "interface", "main.py")
    proc = subprocess.Popen(
        [sys.executable, script],
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
    )
    out, _ = proc.communicate(inputs)
    return out


def test_basic_technology_path():
    # choose Technology interest, Math+Physics subjects, Problem Solving
    # Analytical Thinking skills, Office environment, High school education
    answers = "1\n1 4\n1 2\n1\n1\nn\nn\n"
    output = run_recommender(answers)
    assert "AI/ML Engineer" in output or "Software Developer" in output
    assert "match" in output.lower()


def test_no_selection_returns_warning():
    # send empty responses for the multi-select questions
    answers = "\n\n\n1\n1\nn\nn\n"
    output = run_recommender(answers)
    assert "Please select at least 2 inputs" in output
