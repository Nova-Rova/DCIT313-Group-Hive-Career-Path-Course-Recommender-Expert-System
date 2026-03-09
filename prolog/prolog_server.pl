% ============================================================
% Prolog Bridge Server
% Communicates with Java via stdin/stdout using JSON-like protocol
% Run with: swipl -g main -t halt prolog_server.pl
% ============================================================

:- use_module(knowledge_base).
:- use_module(library(lists)).

% ============================================================
% MAIN LOOP - reads queries from Java, writes results back
% Protocol: Java sends a Prolog term as a string, ending with newline
% Prolog sends back a structured response
% ============================================================

main :-
    set_prolog_flag(encoding, utf8),
    read_eval_loop.

read_eval_loop :-
    read_term(Query, [variable_names(_)]),
    ( Query == end_of_file -> true
    ;
      handle_query(Query),
      read_eval_loop
    ).

% ============================================================
% QUERY HANDLERS
% ============================================================

% Query: recommend(UserFacts)
handle_query(recommend(UserFactsAtom)) :-
    !,
    term_to_atom(UserFacts, UserFactsAtom),
    findall(
        CareerID-Score,
        (   career(CareerID, _, _, _, _, _, _),
            score_career(CareerID, UserFacts, Score),
            Score >= 2
        ),
        Pairs
    ),
    sort(2, @>=, Pairs, Sorted),
    write_results(Sorted, UserFacts).

% Query: get_career(CareerID)
handle_query(get_career(CareerID)) :-
    !,
    ( career(CareerID, Name, Desc, Skills, Degree, Certs, Outlook) ->
        format("CAREER|~w|~w|~w|~w|~w|~w~n",
            [Name, Desc, Skills, Degree, Certs, Outlook])
    ;
        format("ERROR|Career not found: ~w~n", [CareerID])
    ).

% Query: all_careers
handle_query(all_careers) :-
    !,
    forall(
        career(ID, Name, _, _, Degree, _, Outlook),
        format("CAREER_LIST|~w|~w|~w|~w~n", [ID, Name, Degree, Outlook])
    ).

% Fallback
handle_query(Unknown) :-
    format("ERROR|Unknown query: ~w~n", [Unknown]).

% ============================================================
% RESULT WRITER
% Writes pipe-delimited results for Java to parse
% Format: RESULT|rank|career_id|name|score|match_pct|degree|outlook|explanation
% ============================================================

write_results(ScoredPairs, UserFacts) :-
    length(ScoredPairs, Total),
    format("RESULT_COUNT|~w~n", [Total]),
    write_ranked_results(ScoredPairs, 1, UserFacts).

write_ranked_results([], _, _).
write_ranked_results([CareerID-Score|Rest], Rank, UserFacts) :-
    career(CareerID, Name, Desc, Skills, Degree, Certs, Outlook),
    rule_explanation(CareerID, Explanation),
    career_rule(CareerID, RuleConditions),
    length(RuleConditions, TotalConds),
    MatchPct is round((Score / TotalConds) * 100),
    % Encode pipes in text fields to avoid parsing issues
    atomic_list_concat(Skills, ',', SkillsStr),
    atomic_list_concat(Certs, ',', CertsStr),
    format("RESULT|~w|~w|~w|~w|~w|~w|~w|~w|~w|~w|~w~n",
        [Rank, CareerID, Name, Score, MatchPct, Desc, SkillsStr, Degree, CertsStr, Outlook, Explanation]),
    NextRank is Rank + 1,
    write_ranked_results(Rest, NextRank, UserFacts).

% ============================================================
% SCORING (copied from knowledge_base for standalone use)
% ============================================================

score_career(CareerID, UserFacts, Score) :-
    career_rule(CareerID, RuleConditions),
    count_matches(RuleConditions, UserFacts, Score).

count_matches([], _, 0).
count_matches([Condition|Rest], UserFacts, Score) :-
    count_matches(Rest, UserFacts, RestScore),
    ( member(Condition, UserFacts) ->
        Score is RestScore + 1
    ;
        Score is RestScore
    ).
