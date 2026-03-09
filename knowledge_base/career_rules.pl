% ============================================================
% DCIT 313 - Career Path & Course Recommender Expert System
% Knowledge Base: career_rules.pl
%
% Knowledge Acquisition Sources:
%   - O*NET Online (onetonline.org)
%   - BLS Occupational Outlook Handbook
%   - University curriculum research (MIT, Stanford, Cambridge)
%   - IEEE / ACM / AMA professional body guidelines
%   - LinkedIn Workforce Insights 2024
% ============================================================


% ============================================================
% SECTION 1: CAREER FACTS
% career(id, name, description, degree, certifications, outlook).
% ============================================================

career(software_developer,
    'Software Developer',
    'Design and build software applications, systems, and tools.',
    'B.Sc. Computer Science / Software Engineering',
    ['AWS Certified Developer', 'Oracle Java Certification', 'Microsoft Azure'],
    'Excellent - 25% growth expected by 2030').

career(data_scientist,
    'Data Scientist',
    'Analyze complex datasets to extract insights and build predictive models.',
    'B.Sc./M.Sc. Data Science / Statistics / Computer Science',
    ['Google Data Analytics Certificate', 'IBM Data Science', 'Tableau Desktop'],
    'Excellent - 36% growth expected by 2031').

career(cybersecurity_analyst,
    'Cybersecurity Analyst',
    'Protect computer systems and networks from digital attacks and breaches.',
    'B.Sc. Cybersecurity / Computer Science / Information Technology',
    ['CompTIA Security+', 'CEH', 'CISSP', 'OSCP'],
    'Very Good - 35% growth expected by 2031').

career(ai_engineer,
    'AI/ML Engineer',
    'Build intelligent systems using machine learning and artificial intelligence.',
    'B.Sc./M.Sc. Artificial Intelligence / Computer Science',
    ['TensorFlow Developer Certificate', 'AWS ML Specialty', 'DeepLearning.AI'],
    'Excellent - fastest growing tech role').

career(network_engineer,
    'Network Engineer',
    'Design, implement, and manage computer networks and infrastructure.',
    'B.Sc. Computer Networks / Information Technology',
    ['Cisco CCNA', 'CompTIA Network+', 'CCNP'],
    'Good - stable demand across all industries').

career(medical_doctor,
    'Medical Doctor',
    'Diagnose and treat illness, diseases, and injuries in patients.',
    'M.B.B.S. / M.D. Medicine',
    ['Board Certification', 'BLS/ACLS', 'Medical License'],
    'Good - 3% steady growth, always in demand').

career(nurse,
    'Registered Nurse',
    'Provide and coordinate patient care in hospitals and health facilities.',
    'B.Sc. Nursing / Associate Degree in Nursing',
    ['NCLEX-RN', 'BLS/CPR', 'Specialty Nursing Certifications'],
    'Excellent - 9% growth, critical shortage globally').

career(pharmacist,
    'Pharmacist',
    'Dispense medications and counsel patients on drug usage and interactions.',
    'B.Pharm / Pharm.D.',
    ['Pharmacist License', 'BCPS', 'Immunization Certification'],
    'Good - 2% growth, stable profession').

career(biomedical_engineer,
    'Biomedical Engineer',
    'Design medical devices and equipment bridging engineering and healthcare.',
    'B.Sc. Biomedical Engineering',
    ['Certified Biomedical Equipment Technician (CBET)', 'PMP'],
    'Very Good - 10% growth in medical tech sector').

career(civil_engineer,
    'Civil Engineer',
    'Design and supervise construction of infrastructure like roads and bridges.',
    'B.Sc. Civil Engineering',
    ['PE License', 'PMP', 'LEED Green Associate'],
    'Good - 8% growth driven by infrastructure investment').

career(mechanical_engineer,
    'Mechanical Engineer',
    'Design, analyze, and manufacture mechanical systems and devices.',
    'B.Sc. Mechanical Engineering',
    ['PE License', 'SolidWorks Certification', 'Six Sigma'],
    'Good - 7% growth across manufacturing and energy sectors').

career(electrical_engineer,
    'Electrical Engineer',
    'Design electrical systems, circuits, and electronic devices.',
    'B.Sc. Electrical Engineering / Electronics',
    ['PE License', 'Certified Energy Auditor', 'IEEE Certifications'],
    'Good - 7% growth in energy and electronics industries').

career(business_analyst,
    'Business Analyst',
    'Analyze business processes and recommend improvements using data.',
    'B.Sc. Business Administration / Information Systems',
    ['CBAP', 'PMI-PBA', 'Agile/Scrum Master'],
    'Good - 14% growth in digital transformation era').

career(marketing_manager,
    'Marketing Manager',
    'Plan and execute campaigns to promote products and grow brand awareness.',
    'B.Sc. Marketing / Business Administration',
    ['Google Analytics', 'HubSpot Marketing', 'Facebook Blueprint'],
    'Good - 10% growth with digital marketing boom').

career(financial_analyst,
    'Financial Analyst',
    'Assess investment opportunities and provide financial guidance to businesses.',
    'B.Sc. Finance / Accounting / Economics',
    ['CFA', 'CPA', 'Bloomberg Market Concepts'],
    'Good - 9% growth in finance sector').

career(accountant,
    'Accountant / Auditor',
    'Prepare and examine financial records ensuring accuracy and compliance.',
    'B.Sc. Accounting / Finance',
    ['CPA', 'ACCA', 'CMA', 'QuickBooks Certification'],
    'Good - 7% stable growth in all sectors').

career(graphic_designer,
    'Graphic Designer',
    'Create visual content for print, digital media, and branding.',
    'B.Sc./B.A. Graphic Design / Visual Communication',
    ['Adobe Certified Expert (ACE)', 'Google UX Design'],
    'Fair - 3% growth, competitive field with freelance potential').

career(ux_ui_designer,
    'UX/UI Designer',
    'Design intuitive and visually appealing digital user experiences.',
    'B.Sc. Computer Science / Interaction Design / HCI',
    ['Google UX Design Certificate', 'Adobe XD', 'Figma Certification'],
    'Very Good - 13% growth in digital product development').

career(teacher,
    'Teacher / Educator',
    'Educate and mentor students in academic subjects and life skills.',
    'B.Ed. / B.Sc. + PGDE / Education Degree',
    ['Teaching License/Certificate', 'TESOL/TEFL'],
    'Good - steady demand, critical profession globally').

career(psychologist,
    'Psychologist / Counselor',
    'Study human behavior and provide mental health support and therapy.',
    'B.Sc./M.Sc. Psychology / Clinical Psychology',
    ['Licensed Professional Counselor (LPC)', 'NBCC Certification'],
    'Excellent - 22% growth, rising mental health awareness').

career(environmental_scientist,
    'Environmental Scientist',
    'Study environmental problems and develop solutions for sustainability.',
    'B.Sc. Environmental Science / Environmental Engineering',
    ['CHMM', 'PE Environmental', 'ISO 14001 Lead Auditor'],
    'Good - 8% growth in green economy transition').

career(architect,
    'Architect',
    'Design buildings and spaces balancing aesthetics, function, and safety.',
    'B.Arch / M.Arch Architecture',
    ['Licensed Architect (RA)', 'LEED AP', 'AutoCAD Certification'],
    'Fair - 3% growth, specialized creative profession').

career(lawyer,
    'Lawyer / Legal Counsel',
    'Advise clients on legal matters and represent them in legal proceedings.',
    'LLB / JD Law Degree + Bar Exam',
    ['State Bar License', 'Specialized Legal Certifications'],
    'Good - 10% growth, diverse practice areas').

career(entrepreneur,
    'Entrepreneur / Startup Founder',
    'Build and grow innovative businesses solving real-world problems.',
    'B.Sc. Business / Any Field + MBA (optional)',
    ['PMP', 'Lean Startup Methodology', 'Business Development Certifications'],
    'Variable - high risk, high reward career path').


% ============================================================
% SECTION 2: IF-THEN RULES (The Core of the Expert System)
%
% Format: career_rule(CareerID, [list_of_conditions]).
% Each condition is one of:
%   interest(X)     - user's area of interest
%   subject(X)      - user's strong academic subject
%   skill(X)        - user's self-identified skill
%   environment(X)  - preferred work environment
%   education(X)    - current education level
%
% Conflict Resolution: Python scores each rule by counting
% how many conditions match the user's profile, then ranks
% careers in descending order. Minimum threshold = 2 matches.
% ============================================================

% RULE 1: Software Developer
% IF interest=technology AND subject=mathematics AND skill=problem_solving
% THEN recommend software_developer
career_rule(software_developer, [
    interest(technology),
    subject(mathematics),
    skill(problem_solving)
]).

% RULE 2: Data Scientist
% IF interest=technology AND subject=mathematics AND skill=analytical_thinking
% THEN recommend data_scientist
career_rule(data_scientist, [
    interest(technology),
    subject(mathematics),
    skill(analytical_thinking)
]).

% RULE 3: Cybersecurity Analyst
% IF interest=technology AND subject=mathematics AND skill=problem_solving AND environment=office
% THEN recommend cybersecurity_analyst
career_rule(cybersecurity_analyst, [
    interest(technology),
    subject(mathematics),
    skill(problem_solving),
    environment(office)
]).

% RULE 4: AI/ML Engineer
% IF interest=technology AND subject=mathematics AND skill=analytical_thinking AND skill=problem_solving
% THEN recommend ai_engineer
career_rule(ai_engineer, [
    interest(technology),
    subject(mathematics),
    skill(analytical_thinking),
    skill(problem_solving)
]).

% RULE 5: Network Engineer
% IF interest=technology AND subject=physics AND skill=problem_solving AND environment=office
% THEN recommend network_engineer
career_rule(network_engineer, [
    interest(technology),
    subject(physics),
    skill(problem_solving),
    environment(office)
]).

% RULE 6: Medical Doctor
% IF interest=health AND subject=biology AND skill=analytical_thinking AND skill=helping_people
% THEN recommend medical_doctor
career_rule(medical_doctor, [
    interest(health),
    subject(biology),
    skill(analytical_thinking),
    skill(helping_people)
]).

% RULE 7: Registered Nurse
% IF interest=health AND skill=helping_people AND skill=communication AND environment=hospital
% THEN recommend nurse
career_rule(nurse, [
    interest(health),
    skill(helping_people),
    skill(communication),
    environment(hospital)
]).

% RULE 8: Pharmacist
% IF interest=health AND subject=chemistry AND skill=analytical_thinking AND skill=attention_to_detail
% THEN recommend pharmacist
career_rule(pharmacist, [
    interest(health),
    subject(chemistry),
    skill(analytical_thinking),
    skill(attention_to_detail)
]).

% RULE 9: Biomedical Engineer
% IF interest=engineering AND interest=health AND subject=biology AND skill=problem_solving
% THEN recommend biomedical_engineer
career_rule(biomedical_engineer, [
    interest(engineering),
    interest(health),
    subject(biology),
    skill(problem_solving)
]).

% RULE 10: Civil Engineer
% IF interest=engineering AND subject=mathematics AND subject=physics AND skill=problem_solving AND environment=outdoor
% THEN recommend civil_engineer
career_rule(civil_engineer, [
    interest(engineering),
    subject(mathematics),
    subject(physics),
    skill(problem_solving),
    environment(outdoor)
]).

% RULE 11: Mechanical Engineer
% IF interest=engineering AND subject=physics AND subject=mathematics AND skill=problem_solving
% THEN recommend mechanical_engineer
career_rule(mechanical_engineer, [
    interest(engineering),
    subject(physics),
    subject(mathematics),
    skill(problem_solving)
]).

% RULE 12: Electrical Engineer
% IF interest=engineering AND subject=physics AND subject=mathematics AND skill=analytical_thinking
% THEN recommend electrical_engineer
career_rule(electrical_engineer, [
    interest(engineering),
    subject(physics),
    subject(mathematics),
    skill(analytical_thinking)
]).

% RULE 13: Business Analyst
% IF interest=business AND skill=analytical_thinking AND skill=communication AND environment=office
% THEN recommend business_analyst
career_rule(business_analyst, [
    interest(business),
    skill(analytical_thinking),
    skill(communication),
    environment(office)
]).

% RULE 14: Marketing Manager
% IF interest=business AND skill=creativity AND skill=communication AND skill=leadership
% THEN recommend marketing_manager
career_rule(marketing_manager, [
    interest(business),
    skill(creativity),
    skill(communication),
    skill(leadership)
]).

% RULE 15: Financial Analyst
% IF interest=business AND subject=mathematics AND skill=analytical_thinking AND skill=attention_to_detail
% THEN recommend financial_analyst
career_rule(financial_analyst, [
    interest(business),
    subject(mathematics),
    skill(analytical_thinking),
    skill(attention_to_detail)
]).

% RULE 16: Accountant
% IF interest=business AND subject=mathematics AND skill=attention_to_detail AND environment=office
% THEN recommend accountant
career_rule(accountant, [
    interest(business),
    subject(mathematics),
    skill(attention_to_detail),
    environment(office)
]).

% RULE 17: Graphic Designer
% IF interest=arts AND skill=creativity AND skill=visual_arts AND environment=remote
% THEN recommend graphic_designer
career_rule(graphic_designer, [
    interest(arts),
    skill(creativity),
    skill(visual_arts),
    environment(remote)
]).

% RULE 18: UX/UI Designer
% IF interest=arts AND interest=technology AND skill=creativity AND skill=problem_solving
% THEN recommend ux_ui_designer
career_rule(ux_ui_designer, [
    interest(arts),
    interest(technology),
    skill(creativity),
    skill(problem_solving)
]).

% RULE 19: Teacher / Educator
% IF skill=communication AND skill=helping_people AND skill=leadership AND environment=school
% THEN recommend teacher
career_rule(teacher, [
    skill(communication),
    skill(helping_people),
    skill(leadership),
    environment(school)
]).

% RULE 20: Psychologist
% IF interest=health AND subject=biology AND skill=empathy AND skill=communication
% THEN recommend psychologist
career_rule(psychologist, [
    interest(health),
    subject(biology),
    skill(empathy),
    skill(communication)
]).

% RULE 21: Environmental Scientist
% IF interest=engineering AND subject=biology AND subject=chemistry AND environment=outdoor
% THEN recommend environmental_scientist
career_rule(environmental_scientist, [
    interest(engineering),
    subject(biology),
    subject(chemistry),
    environment(outdoor)
]).

% RULE 22: Architect
% IF interest=arts AND interest=engineering AND subject=mathematics AND skill=creativity
% THEN recommend architect
career_rule(architect, [
    interest(arts),
    interest(engineering),
    subject(mathematics),
    skill(creativity)
]).

% RULE 23: Lawyer
% IF interest=business AND skill=analytical_thinking AND skill=communication AND skill=leadership
% THEN recommend lawyer
career_rule(lawyer, [
    interest(business),
    skill(analytical_thinking),
    skill(communication),
    skill(leadership)
]).

% RULE 24: Entrepreneur
% IF interest=business AND skill=creativity AND skill=leadership AND skill=problem_solving
% THEN recommend entrepreneur
career_rule(entrepreneur, [
    interest(business),
    skill(creativity),
    skill(leadership),
    skill(problem_solving)
]).


% ============================================================
% SECTION 3: EXPLANATION FACTS
% Maps each career to a human-readable "why" string.
% ============================================================

why(software_developer,
    'Interest=Technology + Subject=Mathematics + Skill=Problem Solving. These are the core requirements for software development roles.').
why(data_scientist,
    'Interest=Technology + Subject=Mathematics + Skill=Analytical Thinking. Data science demands strong quantitative and logical reasoning.').
why(cybersecurity_analyst,
    'Interest=Technology + Subject=Mathematics + Skill=Problem Solving + Environment=Office. Security roles need logical thinkers who enjoy technical challenges.').
why(ai_engineer,
    'Interest=Technology + Subject=Mathematics + Skills=Analytical Thinking & Problem Solving. AI/ML engineering demands both deep math and logical programming skills.').
why(network_engineer,
    'Interest=Technology + Subject=Physics + Skill=Problem Solving + Environment=Office. Networking requires understanding of physical and logical systems.').
why(medical_doctor,
    'Interest=Health + Subject=Biology + Skills=Analytical Thinking & Helping People. Medicine combines science with compassionate patient care.').
why(nurse,
    'Interest=Health + Skills=Helping People & Communication + Environment=Hospital. Nursing requires direct patient interaction and strong interpersonal skills.').
why(pharmacist,
    'Interest=Health + Subject=Chemistry + Skills=Analytical Thinking & Attention to Detail. Pharmacists must precisely understand drug compositions and interactions.').
why(biomedical_engineer,
    'Interests=Engineering & Health + Subject=Biology + Skill=Problem Solving. Biomedical engineering bridges healthcare and technical fields.').
why(civil_engineer,
    'Interest=Engineering + Subjects=Mathematics & Physics + Skill=Problem Solving + Environment=Outdoor. Civil engineers work on construction sites and infrastructure.').
why(mechanical_engineer,
    'Interest=Engineering + Subjects=Physics & Mathematics + Skill=Problem Solving. Mechanical engineering applies physics to design and build systems.').
why(electrical_engineer,
    'Interest=Engineering + Subjects=Physics & Mathematics + Skill=Analytical Thinking. Electrical engineering requires deep understanding of physics and circuits.').
why(business_analyst,
    'Interest=Business + Skills=Analytical Thinking & Communication + Environment=Office. Business Analysts bridge business needs with technical solutions.').
why(marketing_manager,
    'Interest=Business + Skills=Creativity, Communication & Leadership. Marketing requires persuasion, innovation, and strategic thinking.').
why(financial_analyst,
    'Interest=Business + Subject=Mathematics + Skills=Analytical Thinking & Attention to Detail. Finance demands precision and quantitative analysis.').
why(accountant,
    'Interest=Business + Subject=Mathematics + Skill=Attention to Detail + Environment=Office. Accounting requires meticulous numerical accuracy.').
why(graphic_designer,
    'Interest=Arts + Skills=Creativity & Visual Arts + Environment=Remote. Graphic design rewards artistic talent and visual communication skills.').
why(ux_ui_designer,
    'Interests=Arts & Technology + Skills=Creativity & Problem Solving. UX/UI design sits at the intersection of aesthetics and user research.').
why(teacher,
    'Skills=Communication, Helping People & Leadership + Environment=School. Teaching requires passion for sharing knowledge and guiding others.').
why(psychologist,
    'Interest=Health + Subject=Biology + Skills=Empathy & Communication. Psychology combines behavioral science with compassionate counseling.').
why(environmental_scientist,
    'Interest=Engineering + Subjects=Biology & Chemistry + Environment=Outdoor. Environmental science involves fieldwork and lab-based problem solving.').
why(architect,
    'Interests=Arts & Engineering + Subject=Mathematics + Skill=Creativity. Architecture merges artistic vision with structural engineering precision.').
why(lawyer,
    'Interest=Business + Skills=Analytical Thinking, Communication & Leadership. Law requires sharp reasoning, research, and persuasive argumentation.').
why(entrepreneur,
    'Interest=Business + Skills=Creativity, Leadership & Problem Solving. Entrepreneurship rewards innovative thinkers who take initiative.').


% ============================================================
% SECTION 4: INFERENCE PREDICATES
% Called by Python via pyswip to run the forward chaining logic.
% ============================================================

% score_career(+CareerID, +UserFacts, -Score)
% Counts how many conditions in a rule are satisfied by UserFacts.
score_career(CareerID, UserFacts, Score) :-
    career_rule(CareerID, Conditions),
    count_matches(Conditions, UserFacts, Score).

count_matches([], _, 0).
count_matches([H|T], Facts, Score) :-
    count_matches(T, Facts, Rest),
    (member(H, Facts) -> Score is Rest + 1 ; Score is Rest).

% get_recommendation(+UserFacts, -CareerID, -Score)
% Returns a career and its score if score >= 2 (minimum threshold).
get_recommendation(UserFacts, CareerID, Score) :-
    career(CareerID, _, _, _, _, _),
    score_career(CareerID, UserFacts, Score),
    Score >= 2.

% get_career_info(+CareerID, -Name, -Desc, -Degree, -Certs, -Outlook, -Why)
% Returns all details for a given career ID.
get_career_info(CareerID, Name, Desc, Degree, Certs, Outlook, Why) :-
    career(CareerID, Name, Desc, Degree, Certs, Outlook),
    why(CareerID, Why).

% get_rule_size(+CareerID, -Size)
% Returns total number of conditions in a career's rule.
get_rule_size(CareerID, Size) :-
    career_rule(CareerID, Conditions),
    length(Conditions, Size).
