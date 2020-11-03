# -*- coding: utf-8 -*-

import logic
import planning
import search
import utils

""" A2 Part A

    giveFeedback is a function that reads in a student state and returns a feedback message using propositional logic and proof by resolution. The rules
    for how to decide which message to return are given in the assignment description.
    
    studentState:   a String representing a conjunction of five possible symbols: CorrectAnswer, NewSkill, MasteredSkill, CorrectStreak, IncorrectStreak
                    For example, you could call giveFeedback('CorrectAnswer') or giveFeedback('MasteredSkill & CorrectStreak')
    
    feedbackMessage:a String representing one of eight feedback messages (M1 through M8 below). 
    
    Feel free to refactor the code to move M1 through M8 into a class, but the function call and return values should remain as specified below for our grading.
"""

M1 = 'Correct. Keep up the good work!'
M2 = 'Correct. I think you’re getting it!'
M3 = 'Correct. After this problem we can switch to a new activity.'
M4 = 'Incorrect. Keep trying and I’m sure you’ll get it!'
M5 = 'Incorrect. After this problem, we can switch to a new activity.'
M6 = 'Incorrect. The following is the correct answer to the problem.'
M7 = 'Correct.'
M8 = 'Incorrect.'


def giveFeedback(studentState):
    message_kb = logic.PropKB()

    message_kb.tell('CorrectAnswer ==> (M1 | M2 | M3 | M7)')
    message_kb.tell('~CorrectAnswer ==> (M4 | M5 | M6 | M8)')
    message_kb.tell('((MasteredSkill & ~CorrectAnswer) | (MasteredSkill & CorrectStreak)) ==> IsBored')
    message_kb.tell('(NewSkill | IncorrectStreak) ==> M6')
    message_kb.tell('((IncorrectStreak & CorrectAnswer) | (NewSkill & CorrectStreak)) ==> NeedsEncouragement')
    message_kb.tell('NeedsEncouragement ==> (M2 | M4)')
    message_kb.tell('IsBored ==> (M3 | M5)')
    message_kb.tell('((NewSkill & CorrectAnswer) | CorrectStreak) ==> M1')
    message_kb.tell(studentState)

    feedbackMessage = 'no answer'

    if message_kb.ask_if_true(logic.expr('CorrectAnswer')):
        feedbackMessage = M7
        if logic.pl_resolution(message_kb, logic.expr('M1')):
            feedbackMessage = M1
        elif logic.pl_resolution(message_kb, logic.expr('M2')):
            feedbackMessage = M2
        elif logic.pl_resolution(message_kb, logic.expr('M3')):
            feedbackMessage = M3
    elif message_kb.ask_if_true(logic.expr('~CorrectAnswer')):
        feedbackMessage = M8
        if logic.pl_resolution(message_kb, logic.expr('M4')):
            feedbackMessage = M4
        elif logic.pl_resolution(message_kb, logic.expr('M5')):
            feedbackMessage = M5
        elif logic.pl_resolution(message_kb, logic.expr('M6')):
            feedbackMessage = M6

    """print('M1: ' + str(logic.pl_resolution(message_kb, logic.expr('M1'))))
    print('M2: ' + str(logic.pl_resolution(message_kb, logic.expr('M2'))))
    print('M3: ' + str(logic.pl_resolution(message_kb, logic.expr('M3'))))
    print('M4: ' + str(logic.pl_resolution(message_kb, logic.expr('M4'))))
    print('M5: ' + str(logic.pl_resolution(message_kb, logic.expr('M5'))))
    print('M6: ' + str(logic.pl_resolution(message_kb, logic.expr('M6'))))
    print('M7: ' + str(logic.pl_resolution(message_kb, logic.expr('M7'))))
    print('M8: ' + str(logic.pl_resolution(message_kb, logic.expr('M8'))))
    print('NeedsEncouragement: ' + str(message_kb.ask_if_true(logic.expr('NeedsEncouragement'))))
    print('IsBored: ' + str(message_kb.ask_if_true(logic.expr('IsBored'))))"""

    return feedbackMessage


""" A2 Part B

    solveEquation is a function that converts a string representation of an equation to a first-order logic representation, and then
    uses a forward planning algorithm to solve the equation. 
    
    equation:   a String representing the equation to be solved. "x=3", "-3x=6", "3x-2=6", "4+3x=6x-7" are all possible Strings.
                For example, you could call solveEquation('x=6') or solveEquation('-3x=6')
    
    plan:   return a list of Strings, where each String is a step in the plan. The Strings should reference the core actions described in the
            Task Domain part of the assignment description.
    
"""


class AlgebraPlan(planning.ForwardPlan):
    """def __init__(self, terms, planning_problem):
        #print('AlgebraPlan.__init__(' + str(terms) + ', planning_problem)')
        self.terms = terms
        super(AlgebraPlan, self).__init__(planning_problem)"""

    def convert(self, clauses):
        """Converts strings into Exprs"""
        if isinstance(clauses, logic.Expr):
            clauses = logic.conjuncts(clauses)
            for i in range(len(clauses)):
                if clauses[i].op == '~':
                    clauses[i] = logic.expr('Not' + str(clauses[i].args[0]))

        elif isinstance(clauses, str):
            clauses = clauses.replace('~', 'Not')
            if len(clauses) > 0:
                clauses = logic.expr(clauses)

            try:
                clauses = logic.conjuncts(clauses)
            except AttributeError:
                pass

        return clauses

    def result(self, state, action):
        terms = parse_state(state)
        sentence = str(logic.associate('&', action(logic.conjuncts(state), action.args).clauses))
        #print('------------\nBefore: ' + sentence)
        while sentence.find('divide(x)') != -1:
            quotient = terms[4] / terms[0]
            terms[0] = 1
            terms[4] = quotient
            sentence = sentence.replace('divide(x)', '{:.2f}'.format(quotient))
        while sentence.find('combine') != -1:
            start = sentence.find('combine')
            term = int(sentence[start + 9]) - 1
            if term < 3:
                new_val = terms[term] + terms[2]
                terms[term] = new_val
                terms[2] = 0
            else:
                new_val = terms[term] + terms[5]
                terms[term] = new_val
                terms[5] = 0
            sentence = sentence.replace(sentence[start:(start+11)], str(new_val))
        while sentence.find('inverse') != -1:
            start = sentence.find('inverse')
            term = int(sentence[start + 9]) - 1
            new_val = -1 * terms[term]
            if term == 3:
                terms[2] = new_val
            elif term == 1:
                terms[5] = new_val
            terms[term] = 0
            sentence = sentence.replace(sentence[start:(start+11)], str(new_val))
        #print('After: ' + sentence + '\n------------')
        return logic.associate('&', self.convert(sentence.replace('Not', '~')))


MATH_ACTIONS = [
            planning.Action(
                action='AddVar(x)',
                precond='Zero(T3) & Val(T4, x)',
                effect='~Val(T4, x) & Zero(T4) & ~Zero(T3) & Var(T3) & Val(T3, inverse(T4))'
            ),
            planning.Action(
                action='AddConst(x)',
                precond='Zero(T6) & Val(T2, x)',
                effect='~Val(T2, x) & Zero(T2) & ~Zero(T6) & Const(T6) & Val(T6, inverse(T2))'
            ),
            planning.Action(
                action='Divide(x)',
                precond='Val(T1, x) & Zero(T2) & Zero(T3) & Zero(T4) & Zero(T6)',
                effect='~Val(T1, x) & Val(T1, 1) & Val(T5, divide(x))'
            ),
            planning.Action(
                action='CombVarLeft(x, y)',
                precond='Var(T3) & Val(T1, x) & Val(T3, y)',
                effect='Zero(T3) & ~Var(T3) & ~Val(T3, y) & ~Val(T1, x) & Val(T1, combine(T1))'
            ),
            planning.Action(
                action='CombVarRight(x, y)',
                precond='Var(T6) & Val(T4, x) & Val(T6, y)',
                effect='Zero(T6) & ~Var(T6) & ~Val(T6, y) & ~Val(T4, x) & Val(T4, combine(T4))'
            ),
            planning.Action(
                action='CombConstLeft(x, y)',
                precond='Const(T3) & Val(T2, x) & Val(T3, y)',
                effect='Zero(T3) & ~Const(T3) & ~Val(T3, y) & ~Val(T2, x) & Val(T2, combine(T2))'
            ),
            planning.Action(
                action='CombConstRight(x, y)',
                precond='Const(T6) & Val(T5, x) & Val(T6, y)',
                effect='Zero(T6) & ~Const(T6) & ~Val(T6, y) & ~Val(T5, x) & Val(T5, combine(T5))'
            )
        ]


def convertEquation(equation):
    sentence = '('
    left_var = False
    left_const = False
    right_var = False
    right_const = False

    equation = equation.replace('-', '+-')
    left, right = equation.split('=')
    left = left.strip('+')
    right = right.strip('+')
    left = left.split('+')
    right = right.split('+')
    a = left[0]
    b = '0'
    c = right[0]
    d = '0'
    if len(left) > 1:
        b = left[1]
    if len(right) > 1:
        d = right[1]

    if 'x' in a:
        a = a.strip('x')
        if (a is '-') or (a is ''):
            a += '1'
        sentence += 'Val(T1, ' + a + ') & '
        left_var = True
    else:
        sentence += 'Val(T2, ' + a + ') & '
        left_const = True

    if b is '0':
        if left_var:
            sentence += 'Zero(T2) & Zero(T3) & '
        elif left_const:
            sentence += 'Zero(T1) & Zero(T3) & '
    elif 'x' in b:
        b = b.strip('x')
        if (b is '-') or (b is ''):
            b += '1'
        if left_var:
            sentence += 'Zero(T2) & Var(T3) & Val(T3, ' + b + ') & '
        else:
            sentence += 'Zero(T3) & Val(T1, ' + b + ') & '
    else:
        if left_const:
            sentence += 'Zero(T1) & Const(T3) & Val(T3, ' + b + ') & '
        else:
            sentence += 'Zero(T3) & Val(T2, ' + b + ') & '

    if 'x' in c:
        c = c.strip('x')
        if (c is '-') or (c is ''):
            c += '1'
        sentence += 'Val(T4, ' + c + ') & '
        right_var = True
    else:
        sentence += 'Val(T5, ' + a + ') & '
        right_const = True

    if d is '0':
        if right_var:
            sentence += 'Zero(T5) & Zero(T6)'
        elif right_const:
            sentence += 'Zero(T4) & Zero(T6)'
    elif 'x' in d:
        d = d.strip('x')
        if (d is '-') or (d is ''):
            d += '1'
        if right_var:
            sentence += 'Zero(T5) & Var(T6) & Val(T6, ' + d + ')'
        else:
            sentence += 'Zero(T6) & Val(T4, ' + d + ')'
    else:
        if right_const:
            sentence += 'Zero(T4) & Const(T6) & Val(T6, ' + d + ')'
        else:
            sentence += 'Zero(T6) & Val(T5, ' + d + ')'

    sentence += ')'

    return sentence


def parse_state(state):
    terms = [0, 0, 0, 0, 0, 0]
    state = str(state)
    clauses = state[1:len(state)-1].split(' & ')
    for c in clauses:
        if c.find('Not') == -1 and c.find('Val') != -1:
            term = int(c[5]) - 1
            try:
                value = int(c.split(', ')[1].rstrip(')'))
            except:
                value = float(c.split(', ')[1].rstrip(')'))
            terms[term] = value
    return terms


def mathProblem(equation):
    initial = convertEquation(equation)
    terms = parse_state(initial)
    possible_terms = [1, terms[0] - terms[3], terms[4] - terms[1], -1 * terms[1], -1 * terms[3], terms[0] + terms[2],
                      terms[1] + terms[2], terms[3] + terms[5], terms[4] + terms[5]]
    domain = 'Var(T1) & Const(T2) & Var(T4) & Const(T5)'
    for p in possible_terms:
        domain += ' & Possible(' + str(p) + ')'

    return planning.PlanningProblem(
        initial=initial,
        goals='Val(T1, 1) & Zero(T2) & Zero(T3) & Zero(T4) & Zero(T6)',
        actions=MATH_ACTIONS,
        domain=domain)


def solveEquation(equation):
    mp = mathProblem(equation)
    ap = AlgebraPlan(mp)
    bfs = search.breadth_first_graph_search(ap)
    plan = []
    for s in bfs.solution():
        step = str(s)
        if 'AddVar' in step:
            value = step.split('(')[1].rstrip(')')
            if '-' in value:
                plan.append('add ' + value.lstrip('-') + 'x')
            else:
                plan.append('add -' + value + 'x')
        elif 'AddConst' in step:
            value = step.split('(')[1].rstrip(')')
            if '-' in value:
                plan.append('add ' + value.lstrip('-'))
            else:
                plan.append('add -' + value)
        elif 'CombVarLeft' in step:
            terms = s.split('(')[1].rstrip(')').split(', ')
            result = int(terms[0]) + int(terms[1])
            if result >= 0:
                plan.append('combine LHS variable terms and get positive')
            elif result < 0:
                plan.append('combine LHS variable terms and get negative')
        elif 'CombVarRight' in step:
            terms = s.split('(')[1].rstrip(')').split(', ')
            result = int(terms[0]) + int(terms[1])
            if result >= 0:
                plan.append('combine RHS variable terms and get positive')
            elif result < 0:
                plan.append('combine RHS variable terms and get negative')
        elif 'CombConstLeft' in step:
            plan.append('combine LHS constant terms')
        elif 'CombConstRight' in step:
            plan.append('combine RHS constant terms')
        elif 'Divide' in step:
            value = step.split('(')[1].rstrip(')')
            plan.append('divide ' + value)
    return plan


""" A2 Part C

    predictSuccess is a function that takes in a list of skills students have and an equation to be solved, and returns the skills
    students need but do not currently have in order to solve the skill. For example, if students are solving the problem 3x+2=8, and have S7 and S8, 
    they would still need S4 and S5 to solve the problem.
    
    current_skills: A list of skills students currently have, represented by S1 through S9 (described in the assignment description)
    
    equation:   a String representing the equation to be solved. "x=3", "-3x=6", "3x-2=6", "4+3x=6x-7" are all possible Strings.
                For example, you could call solveEquation('x=6') or solveEquation('-3x=6')
    
    missing_skills: A list of skills students need to solve the problem, represented by S1 through S9.
    
"""


def predictSuccess(current_skills, equation):
    steps = solveEquation(equation)

    skills_kb = logic.FolKB([
        logic.expr('Knows(S1) ==> Can(AddVarPos)'),
        logic.expr('Knows(S2) ==> Can(AddVarNeg)'),
        logic.expr('Knows(S3) ==> Can(AddConstPos)'),
        logic.expr('Knows(S4) ==> Can(AddConstNeg)'),
        logic.expr('Knows(S5) ==> Can(DivPos)'),
        logic.expr('Knows(S6) ==> Can(DivNeg)'),
        logic.expr('Knows(S7) ==> Can(CombVarPos)'),
        logic.expr('Knows(S8) ==> Can(CombVarNeg)'),
        logic.expr('Knows(S9) ==> Can(CombConst)')])

    for skill in current_skills:
        skills_kb.tell(logic.expr('Knows(' + skill + ')'))

    missing_skills = []

    for s in steps:
        if 'add' in s:
            if 'x' in s:
                if '-' not in s and skills_kb.ask(logic.expr('Can(AddVarPos)')) is False:
                    missing_skills.append('S1')
                elif '-' in s and skills_kb.ask(logic.expr('Can(AddVarNeg)')) is False:
                    missing_skills.append('S2')
            elif 'x' not in s:
                if '-' not in s and skills_kb.ask(logic.expr('Can(AddConstPos)')) is False:
                    missing_skills.append('S3')
                elif '-' in s and skills_kb.ask(logic.expr('Can(AddConstNeg)')) is False:
                    missing_skills.append('S4')
        elif 'divide' in s:
            if '-' not in s and skills_kb.ask(logic.expr('Can(DivPos)')) is False:
                missing_skills.append('S5')
            elif '-' in s and skills_kb.ask(logic.expr('Can(DivNeg)')) is False:
                missing_skills.append('S6')
        elif 'combine' in s:
            if 'variable' in s:
                if 'positive' in s and skills_kb.ask(logic.expr('Can(CombVarPos)')) is False:
                    missing_skills.append('S7')
                elif 'negative' in s and skills_kb.ask(logic.expr('Can(CombVarNeg')) is False:
                    missing_skills.append('S8')
            elif 'constant' in s and skills_kb.ask(logic.expr('Can(CombConst)')) is False:
                missing_skills.append('S9')

    """for s in steps:
        s = str(s)
        if 'AddVar' in s:
            if '-' in s and skills_kb.ask(logic.expr('Can(AddVarPos)')) is False:
                missing_skills.append('S1')
            elif '-' not in s and skills_kb.ask(logic.expr('Can(AddVarNeg)')) is False:
                missing_skills.append('S2')
        elif 'AddConst' in s:
            if '-' in s and skills_kb.ask(logic.expr('Can(AddConstPos)')) is False:
                missing_skills.append('S3')
            elif '-' not in s and skills_kb.ask(logic.expr('Can(AddConstNeg)')) is False:
                missing_skills.append('S4')
        elif 'Divide' in s:
            if '-' not in s and skills_kb.ask(logic.expr('Can(DivPos)')) is False:
                missing_skills.append('S5')
            elif '-' in s and skills_kb.ask(logic.expr('Can(DivNeg)')) is False:
                missing_skills.append('S6')
        elif 'CombVar' in s:
            terms = s.split('(')[1].rstrip(')').split(', ')
            result = int(terms[0]) + int(terms[1])
            if result >= 0 and skills_kb.ask(logic.expr('Can(CombVarPos)')) is False:
                missing_skills.append('S7')
            elif result < 0 and skills_kb.ask(logic.expr('Can(CombVarNeg')) is False:
                missing_skills.append('S8')
        elif 'CombConst' in s:
            if skills_kb.ask(logic.expr('Can(CombConst)')) is False:
                missing_skills.append('S9')"""

    return missing_skills


""" A2 Part D

    stepThroughProblem is a function that takes a problem, a student action, and a list of current student skills, and returns
    a list containing a feedback message to the student and their updated list of skills.
    
    equation: a String representing the equation to be solved. "x=3", "-3x=6", "3x-2=6", "4+3x=6x-7" are all possible Strings.
    
    action: an action in the task domain. For example: 'add 2', 'combine RHS constant terms', 'divide 3'
    
    current_skills: A list of skills students currently have, represented by S1 through S9 (described in the assignment description)
    
    feedback_message: A feedback message chosen correctly from M1-M9.
    
    updated_skills: A list of skills students have after executing the action.
    
"""
#CURRENT_SKILLS = ['S8', 'S9']
#EQUATION = '3x+2=8'
#ACTION = 'add -2'
#UPDATED_SKILLS = ['S8', 'S9', 'S4']


class Student:
    def __init__(self):
        self.streak = 0
        self.correct_streak = True

    def answer(self, correct):
        if correct:
            if self.correct_streak:
                self.streak += 1
            else:
                self.correct_streak = True
                self.streak = 1
        else:
            if self.correct_streak:
                self.correct_streak = False
                self.streak = 1
            else:
                self.streak += 1


student = Student()


def stepThroughProblem(equation, action, current_skills):
    state = ''
    relevant_skill = ''
    updated_skills = current_skills

    correct_step = solveEquation(equation)[0]

    if action == correct_step:
        correct_answer = True
        state += 'CorrectAnswer'
    else:
        state += '~CorrectAnswer'
        correct_answer = False

    if student.streak >= 3:
        if student.correct_streak:
            state += ' & CorrectStreak'
        else:
            state += ' & IncorrectStreak'

    student.answer(correct_answer)

    if 'add' in correct_step:
        if 'x' in correct_step:
            if '-' not in correct_step:
                relevant_skill = 'S1'
            elif '-' in correct_step:
                relevant_skill = 'S2'
        elif 'x' not in correct_step:
            if '-' not in correct_step:
                relevant_skill = 'S3'
            elif '-' in correct_step:
                relevant_skill = 'S4'
    elif 'divide' in correct_step:
        if '-' not in correct_step:
            relevant_skill = 'S5'
        elif '-' in correct_step:
            relevant_skill = 'S6'
    elif 'combine' in correct_step:
        if 'variable' in correct_step:
            if 'positive' in correct_step:
                relevant_skill = 'S7'
            elif 'negative' in correct_step:
                relevant_skill = 'S8'
        elif 'constant' in correct_step:
            relevant_skill = 'S9'

    if relevant_skill in current_skills:
        state += ' & MasteredSkill'
    else:
        state += ' & NewSkill'
        if correct_answer:
            updated_skills.append(relevant_skill)

    feedback_message = giveFeedback(state)
    return [feedback_message, updated_skills]


"""c_skills = ['S8', 'S9', 'S4']
eq = '3x+2=8'
act = 'add -2'
print('CURRENT_SKILLS = ' + str(c_skills) + '\nEQUATION = ' + eq + '\nACTION = ' + act)
[f_message, u_skills] = stepThroughProblem(eq, act, c_skills)
print('FEEDBACK_MESSAGE: ' + f_message + '\nUPDATED_SKILLS: ' + str(u_skills) + '\n-----------------')

c_skills = ['S8', 'S9', 'S4']
eq = '3x=8-2'
act = 'combine RHS constant terms'
print('CURRENT_SKILLS = ' + str(c_skills) + '\nEQUATION = ' + eq + '\nACTION = ' + act)
[f_message, u_skills] = stepThroughProblem(eq, act, c_skills)
print('FEEDBACK_MESSAGE: ' + f_message + '\nUPDATED_SKILLS: ' + str(u_skills) + '\n-----------------')

c_skills = ['S8', 'S9', 'S4']
eq = '-2x=10'
act = 'add -10'
print('CURRENT_SKILLS = ' + str(c_skills) + '\nEQUATION = ' + eq + '\nACTION = ' + act)
[f_message, u_skills] = stepThroughProblem(eq, act, c_skills)
print('FEEDBACK_MESSAGE: ' + f_message + '\nUPDATED_SKILLS: ' + str(u_skills) + '\n-----------------')

c_skills = ['S8', 'S9', 'S4']
eq = '-2x=10'
act = 'divide 2'
print('CURRENT_SKILLS = ' + str(c_skills) + '\nEQUATION = ' + eq + '\nACTION = ' + act)
[f_message, u_skills] = stepThroughProblem(eq, act, c_skills)
print('FEEDBACK_MESSAGE: ' + f_message + '\nUPDATED_SKILLS: ' + str(u_skills) + '\n-----------------')

c_skills = ['S8', 'S9', 'S4']
eq = '-2x=10'
act = 'divide -2'
print('CURRENT_SKILLS = ' + str(c_skills) + '\nEQUATION = ' + eq + '\nACTION = ' + act)
[f_message, u_skills] = stepThroughProblem(eq, act, c_skills)
print('FEEDBACK_MESSAGE: ' + f_message + '\nUPDATED_SKILLS: ' + str(u_skills) + '\n-----------------')"""
