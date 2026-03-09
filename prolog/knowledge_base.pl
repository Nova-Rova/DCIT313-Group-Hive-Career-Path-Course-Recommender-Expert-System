% ============================================================
% Career Path & Course Recommender System - Knowledge Base
% Prolog Rule-Based Expert System
% ============================================================
% Knowledge Acquisition Sources:
%   - O*NET Online (occupational information network)
%   - Bureau of Labor Statistics Occupational Outlook Handbook
%   - University curriculum research (MIT, Stanford, etc.)
%   - IEEE, ACM, AMA professional body guidelines
% ============================================================

:- module(knowledge_base, [
    career/7,
    career_rule/2,
    rule_explanation/2,
    all_careers/1,
    evaluate_career/3
]).

% ============================================================
% CAREER FACTS
% career(ID, Name, Description, RequiredSkills, Degree, Certifications, Outlook)
% ============================================================

career(software_developer,
    'Software Developer',
    'Design and build software applications, systems, and tools.',
    [problem_solving, analytical_thinking, programming, mathematics],
    'B.Sc. Computer Science / Software Engineering',
    ['AWS Certified Developer', 'Oracle Java Certification', 'Microsoft Azure'],
    'Excellent - 25% growth expected by 2030').

career(data_scientist,
    'Data Scientist',
    'Analyze complex datasets to extract insights and build predictive models.',
    [analytical_thinking, mathematics, problem_solving, statistics],
    'B.Sc./M.Sc. Data Science / Statistics / Computer Science',
    ['Google Data Analytics Certificate', 'IBM Data Science', 'Tableau Desktop'],
    'Excellent - 36% growth expected by 2031').

career(cybersecurity_analyst,
    'Cybersecurity Analyst',
    'Protect computer systems and networks from digital attacks and breaches.',
    [problem_solving, analytical_thinking, attention_to_detail, programming],
    'B.Sc. Cybersecurity / Computer Science / Information Technology',
    ['CompTIA Security+', 'CEH', 'CISSP', 'OSCP'],
    'Very Good - 35% growth expected by 2031').

career(ai_engineer,
    'AI/ML Engineer',
    'Build intelligent systems using machine learning and artificial intelligence.',
    [mathematics, programming, analytical_thinking, problem_solving],
    'B.Sc./M.Sc. Artificial Intelligence / Computer Science',
    ['TensorFlow Developer Certificate', 'AWS ML Specialty', 'DeepLearning.AI'],
    'Excellent - fastest growing tech role').

career(network_engineer,
    'Network Engineer',
    'Design, implement, and manage computer networks and infrastructure.',
    [problem_solving, technical_skills, analytical_thinking, attention_to_detail],
    'B.Sc. Computer Networks / Information Technology',
    ['Cisco CCNA', 'CompTIA Network+', 'CCNP'],
    'Good - stable demand across industries').

career(medical_doctor,
    'Medical Doctor',
    'Diagnose and treat illness, diseases, and injuries in patients.',
    [analytical_thinking, helping_people, attention_to_detail, communication],
    'M.B.B.S. / M.D. Medicine',
    ['Board Certification (specialty)', 'BLS/ACLS', 'Medical License'],
    'Good - 3% steady growth, always in demand').

career(nurse,
    'Registered Nurse',
    'Provide and coordinate patient care in hospitals and health facilities.',
    [helping_people, communication, attention_to_detail, empathy],
    'B.Sc. Nursing / Associate Degree in Nursing',
    ['NCLEX-RN', 'BLS/CPR', 'Specialty Nursing Certifications'],
    'Excellent - 9% growth, critical shortage globally').

career(pharmacist,
    'Pharmacist',
    'Dispense medications and counsel patients on drug usage and interactions.',
    [analytical_thinking, attention_to_detail, helping_people, chemistry],
    'B.Pharm / Pharm.D.',
    ['Pharmacist License', 'BCPS', 'Immunization Certification'],
    'Good - 2% growth, stable profession').

career(biomedical_engineer,
    'Biomedical Engineer',
    'Design medical devices and equipment bridging engineering and healthcare.',
    [problem_solving, analytical_thinking, biology, mathematics],
    'B.Sc. Biomedical Engineering',
    ['Certified Biomedical Equipment Technician (CBET)', 'PMP'],
    'Very Good - 10% growth, expanding medical tech sector').

career(civil_engineer,
    'Civil Engineer',
    'Design and supervise construction of infrastructure like roads and bridges.',
    [problem_solving, mathematics, analytical_thinking, technical_skills],
    'B.Sc. Civil Engineering',
    ['PE License', 'PMP', 'LEED Green Associate'],
    'Good - 8% growth driven by infrastructure investment').

career(mechanical_engineer,
    'Mechanical Engineer',
    'Design, analyze, and manufacture mechanical systems and devices.',
    [problem_solving, mathematics, technical_skills, analytical_thinking],
    'B.Sc. Mechanical Engineering',
    ['PE License', 'SolidWorks Certification', 'Six Sigma'],
    'Good - 7% growth across manufacturing and energy sectors').

career(electrical_engineer,
    'Electrical Engineer',
    'Design electrical systems, circuits, and electronic devices.',
    [mathematics, problem_solving, technical_skills, analytical_thinking],
    'B.Sc. Electrical Engineering / Electronics',
    ['PE License', 'Certified Energy Auditor', 'IEEE Certifications'],
    'Good - 7% growth in energy and electronics industries').

career(business_analyst,
    'Business Analyst',
    'Analyze business processes and recommend improvements using data.',
    [analytical_thinking, communication, problem_solving, business_acumen],
    'B.Sc. Business Administration / Information Systems',
    ['CBAP', 'PMI-PBA', 'Agile/Scrum Master'],
    'Good - 14% growth in digital transformation era').

career(marketing_manager,
    'Marketing Manager',
    'Plan and execute campaigns to promote products and grow brand awareness.',
    [creativity, communication, business_acumen, leadership],
    'B.Sc. Marketing / Business Administration',
    ['Google Analytics', 'HubSpot Marketing', 'Facebook Blueprint'],
    'Good - 10% growth with digital marketing boom').

career(financial_analyst,
    'Financial Analyst',
    'Assess investment opportunities and provide financial guidance to businesses.',
    [analytical_thinking, mathematics, business_acumen, attention_to_detail],
    'B.Sc. Finance / Accounting / Economics',
    ['CFA', 'CPA', 'Bloomberg Market Concepts'],
    'Good - 9% growth in finance sector').

career(accountant,
    'Accountant / Auditor',
    'Prepare and examine financial records ensuring accuracy and compliance.',
    [mathematics, attention_to_detail, analytical_thinking, business_acumen],
    'B.Sc. Accounting / Finance',
    ['CPA', 'ACCA', 'CMA', 'QuickBooks Certification'],
    'Good - 7% stable growth in all sectors').

career(graphic_designer,
    'Graphic Designer',
    'Create visual content for print, digital media, and branding.',
    [creativity, visual_arts, communication, attention_to_detail],
    'B.Sc./B.A. Graphic Design / Visual Communication',
    ['Adobe Certified Expert (ACE)', 'Google UX Design'],
    'Fair - 3% growth, competitive field with freelance potential').

career(ux_ui_designer,
    'UX/UI Designer',
    'Design intuitive and visually appealing digital user experiences.',
    [creativity, problem_solving, communication, visual_arts],
    'B.Sc. Computer Science / Interaction Design / HCI',
    ['Google UX Design Certificate', 'Adobe XD', 'Figma Certification'],
    'Very Good - 13% growth in digital product development').

career(teacher_educator,
    'Teacher / Educator',
    'Educate and mentor students in academic subjects and life skills.',
    [communication, helping_people, leadership, empathy],
    'B.Ed. / B.Sc. + PGDE / Education Degree',
    ['Teaching License/Certificate', 'TESOL/TEFL (for language teachers)'],
    'Good - steady demand, critical profession globally').

career(psychologist,
    'Psychologist / Counselor',
    'Study human behavior and provide mental health support and therapy.',
    [empathy, communication, analytical_thinking, helping_people],
    'B.Sc./M.Sc. Psychology / Clinical Psychology',
    ['Licensed Professional Counselor (LPC)', 'NBCC Certification'],
    'Excellent - 22% growth, rising mental health awareness').

career(environmental_scientist,
    'Environmental Scientist',
    'Study environmental problems and develop solutions for sustainability.',
    [analytical_thinking, biology, problem_solving, attention_to_detail],
    'B.Sc. Environmental Science / Environmental Engineering',
    ['CHMM', 'PE (Environmental)', 'ISO 14001 Lead Auditor'],
    'Good - 8% growth in green economy transition').

career(architect,
    'Architect',
    'Design buildings and spaces balancing aesthetics, function, and safety.',
    [creativity, mathematics, visual_arts, problem_solving],
    'B.Arch / M.Arch Architecture',
    ['Licensed Architect (RA)', 'LEED AP', 'AutoCAD Certification'],
    'Fair - 3% growth, specialized creative profession').

career(lawyer,
    'Lawyer / Legal Counsel',
    'Advise clients on legal matters and represent them in legal proceedings.',
    [analytical_thinking, communication, attention_to_detail, leadership],
    'LLB / JD Law Degree + Bar Exam',
    ['State Bar License', 'Specialized Legal Certifications'],
    'Good - 10% growth, diverse practice areas').

career(entrepreneur,
    'Entrepreneur / Startup Founder',
    'Build and grow innovative businesses solving real-world problems.',
    [creativity, leadership, business_acumen, problem_solving, communication],
    'B.Sc. Business / Any Field + MBA (optional)',
    ['PMP', 'Lean Startup Methodology', 'Business Development Certifications'],
    'Variable - high risk, high reward career path').

% ============================================================
% CAREER RULES (IF-THEN Logic)
% career_rule(CareerID, [list_of_required_attributes])
% Each attribute is: interest(X), subject(X), skill(X), environment(X), education(X)
% ============================================================

% RULE 1: Software Developer
career_rule(software_developer, [
    interest(technology),
    subject(mathematics),
    skill(problem_solving)
]).

% RULE 2: Data Scientist
career_rule(data_scientist, [
    interest(technology),
    subject(mathematics),
    skill(analytical_thinking)
]).

% RULE 3: Cybersecurity Analyst
career_rule(cybersecurity_analyst, [
    interest(technology),
    subject(mathematics),
    skill(problem_solving),
    environment(office)
]).

% RULE 4: AI/ML Engineer
career_rule(ai_engineer, [
    interest(technology),
    subject(mathematics),
    skill(analytical_thinking),
    skill(problem_solving)
]).

% RULE 5: Network Engineer
career_rule(network_engineer, [
    interest(technology),
    subject(physics),
    skill(problem_solving),
    environment(office)
]).

% RULE 6: Medical Doctor
career_rule(medical_doctor, [
    interest(health),
    subject(biology),
    skill(analytical_thinking),
    skill(helping_people),
    education(undergraduate)
]).

% RULE 7: Registered Nurse
career_rule(nurse, [
    interest(health),
    skill(helping_people),
    skill(communication),
    environment(hospital)
]).

% RULE 8: Pharmacist
career_rule(pharmacist, [
    interest(health),
    subject(chemistry),
    skill(analytical_thinking),
    skill(attention_to_detail)
]).

% RULE 9: Biomedical Engineer
career_rule(biomedical_engineer, [
    interest(engineering),
    interest(health),
    subject(biology),
    skill(problem_solving)
]).

% RULE 10: Civil Engineer
career_rule(civil_engineer, [
    interest(engineering),
    subject(mathematics),
    subject(physics),
    skill(problem_solving),
    environment(outdoor)
]).

% RULE 11: Mechanical Engineer
career_rule(mechanical_engineer, [
    interest(engineering),
    subject(physics),
    subject(mathematics),
    skill(problem_solving)
]).

% RULE 12: Electrical Engineer
career_rule(electrical_engineer, [
    interest(engineering),
    subject(physics),
    subject(mathematics),
    skill(analytical_thinking)
]).

% RULE 13: Business Analyst
career_rule(business_analyst, [
    interest(business),
    skill(analytical_thinking),
    skill(communication),
    environment(office)
]).

% RULE 14: Marketing Manager
career_rule(marketing_manager, [
    interest(business),
    skill(creativity),
    skill(communication),
    skill(leadership)
]).

% RULE 15: Financial Analyst
career_rule(financial_analyst, [
    interest(business),
    subject(mathematics),
    skill(analytical_thinking),
    skill(attention_to_detail)
]).

% RULE 16: Accountant
career_rule(accountant, [
    interest(business),
    subject(mathematics),
    skill(attention_to_detail),
    environment(office)
]).

% RULE 17: Graphic Designer
career_rule(graphic_designer, [
    interest(arts),
    skill(creativity),
    skill(visual_arts),
    environment(remote)
]).

% RULE 18: UX/UI Designer
career_rule(ux_ui_designer, [
    interest(arts),
    interest(technology),
    skill(creativity),
    skill(problem_solving)
]).

% RULE 19: Teacher / Educator
career_rule(teacher_educator, [
    skill(communication),
    skill(helping_people),
    skill(leadership),
    environment(school)
]).

% RULE 20: Psychologist
career_rule(psychologist, [
    interest(health),
    subject(biology),
    skill(empathy),
    skill(communication)
]).

% RULE 21: Environmental Scientist
career_rule(environmental_scientist, [
    interest(engineering),
    subject(biology),
    subject(chemistry),
    environment(outdoor)
]).

% RULE 22: Architect
career_rule(architect, [
    interest(arts),
    interest(engineering),
    subject(mathematics),
    skill(creativity)
]).

% RULE 23: Lawyer
career_rule(lawyer, [
    interest(business),
    skill(analytical_thinking),
    skill(communication),
    skill(leadership),
    education(undergraduate)
]).

% RULE 24: Entrepreneur
career_rule(entrepreneur, [
    interest(business),
    skill(creativity),
    skill(leadership),
    skill(problem_solving)
]).

% ============================================================
% RULE EXPLANATIONS
% Maps each rule to a human-readable explanation string
% ============================================================

rule_explanation(software_developer,
    'Recommended because: Interest=Technology + Strong Subject=Mathematics + Skill=Problem Solving. These are core requirements for software development roles.').

rule_explanation(data_scientist,
    'Recommended because: Interest=Technology + Strong Subject=Mathematics + Skill=Analytical Thinking. Data science demands strong quantitative and logical reasoning.').

rule_explanation(cybersecurity_analyst,
    'Recommended because: Interest=Technology + Strong Subject=Mathematics + Skill=Problem Solving + Preferred Environment=Office. Security roles need logical thinkers who enjoy tech challenges.').

rule_explanation(ai_engineer,
    'Recommended because: Interest=Technology + Strong Subject=Mathematics + Skills=Analytical Thinking & Problem Solving. AI/ML engineering demands both deep math and logical programming skills.').

rule_explanation(network_engineer,
    'Recommended because: Interest=Technology + Strong Subject=Physics + Skill=Problem Solving + Environment=Office. Networking requires understanding of physical and logical systems.').

rule_explanation(medical_doctor,
    'Recommended because: Interest=Health + Strong Subject=Biology + Skills=Analytical Thinking & Helping People + Education=Undergraduate (for postgrad medicine). Medicine combines science with compassionate care.').

rule_explanation(nurse,
    'Recommended because: Interest=Health + Skills=Helping People & Communication + Preferred Environment=Hospital. Nursing requires direct patient interaction and strong interpersonal skills.').

rule_explanation(pharmacist,
    'Recommended because: Interest=Health + Strong Subject=Chemistry + Skills=Analytical Thinking & Attention to Detail. Pharmacists must precisely understand drug compositions and interactions.').

rule_explanation(biomedical_engineer,
    'Recommended because: Interests=Engineering & Health + Strong Subject=Biology + Skill=Problem Solving. Biomedical engineering bridges healthcare and technical engineering.').

rule_explanation(civil_engineer,
    'Recommended because: Interest=Engineering + Strong Subjects=Mathematics & Physics + Skill=Problem Solving + Environment=Outdoor. Civil engineers work on construction sites and infrastructure projects.').

rule_explanation(mechanical_engineer,
    'Recommended because: Interest=Engineering + Strong Subjects=Physics & Mathematics + Skill=Problem Solving. Mechanical engineering applies physics to design and build systems.').

rule_explanation(electrical_engineer,
    'Recommended because: Interest=Engineering + Strong Subjects=Physics & Mathematics + Skill=Analytical Thinking. Electrical engineering requires deep understanding of physics and circuits.').

rule_explanation(business_analyst,
    'Recommended because: Interest=Business + Skills=Analytical Thinking & Communication + Environment=Office. BAs bridge business needs with technical solutions in corporate settings.').

rule_explanation(marketing_manager,
    'Recommended because: Interest=Business + Skills=Creativity, Communication & Leadership. Marketing requires persuasion, innovation, and strategic thinking.').

rule_explanation(financial_analyst,
    'Recommended because: Interest=Business + Strong Subject=Mathematics + Skills=Analytical Thinking & Attention to Detail. Finance demands precision and quantitative analysis skills.').

rule_explanation(accountant,
    'Recommended because: Interest=Business + Strong Subject=Mathematics + Skill=Attention to Detail + Environment=Office. Accounting requires meticulous numerical accuracy in structured settings.').

rule_explanation(graphic_designer,
    'Recommended because: Interest=Arts + Skills=Creativity & Visual Arts + Environment=Remote. Graphic design rewards artistic talent and visual communication skills.').

rule_explanation(ux_ui_designer,
    'Recommended because: Interests=Arts & Technology + Skills=Creativity & Problem Solving. UX/UI design sits at the intersection of aesthetics and technical user research.').

rule_explanation(teacher_educator,
    'Recommended because: Skills=Communication, Helping People & Leadership + Environment=School. Teaching requires passion for sharing knowledge and guiding others.').

rule_explanation(psychologist,
    'Recommended because: Interest=Health + Strong Subject=Biology + Skills=Empathy & Communication. Psychology combines scientific study of behavior with compassionate counseling.').

rule_explanation(environmental_scientist,
    'Recommended because: Interest=Engineering + Strong Subjects=Biology & Chemistry + Environment=Outdoor. Environmental science involves fieldwork and lab-based problem solving.').

rule_explanation(architect,
    'Recommended because: Interests=Arts & Engineering + Strong Subject=Mathematics + Skill=Creativity. Architecture merges artistic vision with structural engineering precision.').

rule_explanation(lawyer,
    'Recommended because: Interest=Business + Skills=Analytical Thinking, Communication & Leadership + Education=Undergraduate. Law requires sharp reasoning, research, and persuasive argumentation.').

rule_explanation(entrepreneur,
    'Recommended because: Interest=Business + Skills=Creativity, Leadership & Problem Solving. Entrepreneurship rewards innovative thinkers who take initiative to build ventures.').

% ============================================================
% INFERENCE ENGINE - Forward Chaining Implementation
% ============================================================

% Count how many conditions in a rule are satisfied by user facts
score_career(CareerID, UserFacts, Score) :-
    career_rule(CareerID, RuleConditions),
    count_matches(RuleConditions, UserFacts, Score).

count_matches([], _, 0).
count_matches([Condition|Rest], UserFacts, Score) :-
    count_matches(Rest, UserFacts, RestScore),
    (member(Condition, UserFacts) ->
        Score is RestScore + 1
    ;
        Score is RestScore
    ).

% Check minimum threshold: user must match at least 2 conditions
matches_threshold(CareerID, UserFacts, Score) :-
    score_career(CareerID, UserFacts, Score),
    Score >= 2.

% Get all careers with their scores, filtered by threshold
evaluate_career(CareerID, UserFacts, Score) :-
    career(CareerID, _, _, _, _, _, _),
    matches_threshold(CareerID, UserFacts, Score).

% Get all career IDs
all_careers(CareerIDs) :-
    findall(ID, career(ID, _, _, _, _, _, _), CareerIDs).

% ============================================================
% UTILITY PREDICATES
% ============================================================

% Get career full details by ID
get_career_details(ID, Name, Desc, Skills, Degree, Certs, Outlook) :-
    career(ID, Name, Desc, Skills, Degree, Certs, Outlook).

% Get explanation for a career recommendation
get_explanation(CareerID, Explanation) :-
    rule_explanation(CareerID, Explanation).
