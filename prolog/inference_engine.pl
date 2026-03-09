% ============================================================
% Inference Engine - Career Expert System
% Implements Forward Chaining with Scoring & Ranking
% ============================================================

:- module(inference_engine, [
    run_inference/2,
    top_n_careers/3,
    format_results/2
]).

:- use_module(knowledge_base).

% ============================================================
% MAIN INFERENCE ENTRY POINT
% run_inference(+UserFacts, -RankedResults)
% UserFacts: list of facts like [interest(technology), skill(problem_solving), ...]
% RankedResults: list of career(ID, Score, Explanation) sorted descending
% ============================================================

run_inference(UserFacts, RankedResults) :-
    findall(
        career_score(Score, CareerID),
        evaluate_career(CareerID, UserFacts, Score),
        Unsorted
    ),
    sort(0, @>=, Unsorted, Sorted),
    enrich_results(Sorted, UserFacts, RankedResults).

% Enrich results with full career details and explanations
enrich_results([], _, []).
enrich_results([career_score(Score, CareerID)|Rest], UserFacts, [Result|EnrichedRest]) :-
    career(CareerID, Name, Desc, Skills, Degree, Certs, Outlook),
    rule_explanation(CareerID, Explanation),
    career_rule(CareerID, RuleConditions),
    length(RuleConditions, TotalConditions),
    MatchPct is round((Score / TotalConditions) * 100),
    Result = result{
        id: CareerID,
        name: Name,
        score: Score,
        match_percent: MatchPct,
        description: Desc,
        required_skills: Skills,
        degree: Degree,
        certifications: Certs,
        outlook: Outlook,
        explanation: Explanation
    },
    enrich_results(Rest, UserFacts, EnrichedRest).

% Get top N career matches
top_n_careers(UserFacts, N, TopResults) :-
    run_inference(UserFacts, AllResults),
    length(AllResults, Total),
    Limit is min(N, Total),
    length(TopResults, Limit),
    append(TopResults, _, AllResults).

% ============================================================
% RESULT FORMATTING (for console output / Java bridge)
% ============================================================

format_results([], _) :- 
    write('No matching careers found. Try broadening your inputs.'), nl.

format_results(Results, MaxDisplay) :-
    Results \= [],
    write('================================================='), nl,
    write('   TOP CAREER RECOMMENDATIONS'), nl,
    write('================================================='), nl,
    display_results(Results, 1, MaxDisplay).

display_results(_, Rank, Max) :- Rank > Max, !.
display_results([], _, _) :- !.
display_results([Result|Rest], Rank, Max) :-
    format("~n[#~w] ~w~n", [Rank, Result.name]),
    format("    Match Score   : ~w / ~w conditions (~w%)~n",
        [Result.score, _, Result.match_percent]),
    format("    Description   : ~w~n", [Result.description]),
    format("    Degree        : ~w~n", [Result.degree]),
    format("    Certifications: ~w~n", [Result.certifications]),
    format("    Career Outlook: ~w~n", [Result.outlook]),
    format("    WHY THIS CAREER?~n    ~w~n", [Result.explanation]),
    write('    -------------------------------------------------'), nl,
    NextRank is Rank + 1,
    display_results(Rest, NextRank, Max).

% ============================================================
% CONFLICT RESOLUTION STRATEGY
% When multiple careers match, we use:
%   1. Raw score (number of matched conditions)
%   2. Match percentage (score / total rule conditions)
%   3. If tied, alphabetical order for consistency
% ============================================================

resolve_conflicts(Results, Resolved) :-
    msort(Results, Resolved).

% ============================================================
% SIMPLE QUERY INTERFACE (for testing in SWI-Prolog console)
% Usage: query([interest(technology), skill(problem_solving), subject(mathematics)])
% ============================================================

query(UserFacts) :-
    top_n_careers(UserFacts, 3, TopResults),
    format_results(TopResults, 3).

% Test predicate
test_system :-
    write('TEST 1: Technology + Math + Problem Solving'), nl,
    query([interest(technology), subject(mathematics), skill(problem_solving)]),
    nl,
    write('TEST 2: Health + Biology + Helping People'), nl,
    query([interest(health), subject(biology), skill(helping_people)]),
    nl,
    write('TEST 3: Business + Communication + Leadership'), nl,
    query([interest(business), skill(communication), skill(leadership)]).
