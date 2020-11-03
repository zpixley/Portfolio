from search import Problem, Node
import helpers
import mdp


def odd(num):
    return True if num % 2 == 1 else False


def even(num):
    return not odd(num)


class CustomerServiceCall(Problem):
    def __init__(self, max_turns=None):
        super(CustomerServiceCall, self).__init__('D1')
        self.max_turns = max_turns

    def actions(self, state):
        state = str(state)
        #print('CustomerServiceCall.actions(' + state + ')')
        if state[0] == 'D':
            return ['respond', 'redirect']
        elif state[0] == 'C' and odd(int(state.lstrip('C'))):
            return ['~resolved', 'resolved']
        elif state[0] == 'C' and even(int(state.lstrip('C'))):
            return ['frustrated', '~frustrated']
        elif state[0] == 'U':
            return []

    def result(self, state, action):
        state = str(state)
        action = str(action)
        #print('CustomerServiceCall.result(' + state + ', ' + action + ')')
        if state[0] == 'D':
            num = int(state.lstrip('D'))
            turn = num
            if action is 'respond':
                return 'C' + str(turn*2-1)
            elif action is 'redirect':
                return 'C' + str(turn*2)
        elif state[0] == 'C':
            num = int(state.lstrip('C'))
            turn = int((num + (num % 2)) / 2)
            if action == '~resolved':
                return 'U' + str(3 * (turn - 1) + 1) if turn >= self.max_turns else 'D' + str(turn + 1)
            elif action == 'resolved':
                return 'U' + str(3 * (turn - 1) + 2) if turn >= self.max_turns else 'U' + str(3 * turn - 2)
            elif action == 'frustrated':
                return 'U' + str(3 * (turn - 1) + 3) if turn >= self.max_turns else 'U' + str(3 * turn - 1)
            elif action == '~frustrated':
                return 'U' + str(3 * (turn - 1) + 4) if turn >= self.max_turns else 'U' + str(3 * turn)
        elif state[0] == 'U':
            return state

    def value(self, state):
        # value of a state is resolved using expectimax algorithm
        state = str(state)
        #print('CustomerServiceCall.value(' + state + ')')
        if state[0] == 'D':
            return max([self.value(child) for child in [self.result(state, a) for a in self.actions(state)]])
        elif state[0] == 'C':
            num = int(state.lstrip('C'))
            if odd(num):
                return round(0.9 * self.value(self.result(state, '~resolved')) + 0.1 * self.value(self.result(state, 'resolved')), 2)
            elif even(num):
                return round(0.3 * self.value(self.result(state, 'frustrated')) + 0.7 * self.value(self.result(state, '~frustrated')), 2)
        elif state[0] == 'U':
            num = int(state.lstrip('U'))
            k = int((num - 1) / 3) + 1
            if k >= self.max_turns:
                num = num - ((self.max_turns-1)*3)
                rem = num % 4
                if rem == 1:
                    return 0
                if rem == 2:
                    return 100
                if rem == 3:
                    return 5*self.max_turns
                if rem == 0:
                    return -100 + 5*self.max_turns
            else:
                rem = num % 3
                if rem == 1:
                    return 100
                if rem == 2:
                    return 5*k
                if rem == 0:
                    return -100 + 5*k


def q2(max_turns=2):
    p = CustomerServiceCall(max_turns)
    choices = []
    print('Turn\tRespond\tRedirect\tChoice')
    print('----------------------------------------------')
    for i in range(1, max_turns+1):
        respond = p.value(p.result('D' + str(i), 'respond'))
        redirect = p.value(p.result('D' + str(i), 'redirect'))
        choices.append('respond') if respond >= redirect else choices.append('redirect')
        print(str(i) + ':\t' + str(respond) + '\t' + str(redirect) + '\t\t' + choices[i-1])
    return choices


"""parents = ['D1']
p = CustomerServiceCall(30)


def child_nodes(problem, parents):
    children = []
    for p in parents:
        for a in problem.actions(p):
            children.append(problem.result(p, a))
    return children


children = child_nodes(p, parents)
while children:
    costs = []
    for c in children:
        costs.append(p.value(c))
    print(str(children) + '==>' + str(costs))
    children = child_nodes(p, children)"""


def q3():
    q2(30)


T, F = True, False

chatbot_net = (helpers.BayesNet()
               .add('Accurate', [], 0.9)
               .add('ProblemSize', [], helpers.ProbDist(small=0.9, big=0.1))
               .add('ConversationLength', ['ProblemSize'], {'small': helpers.ProbDist(short=0.4, medium=0.4, long=0.2),
                                                            'big': helpers.ProbDist(short=0.2, medium=0.3, long=0.5)})
               .add('Resolved', ['ConversationLength', 'Accurate'], {('short', T): 0.3, ('short', F): 0.2, ('medium', T): 0.5, ('medium', F): 0.3, ('long', T): 0.7, ('long', F): 0.4})
               .add('Frustrated', ['ProblemSize', 'ConversationLength', 'Accurate'], {('small', 'short', T): 0.2, ('small', 'short', F): 0.4, ('small', 'medium', T): 0.3, ('small', 'medium', F): 0.5, ('small', 'long', T): 0.6, ('small', 'long', F): 0.8, ('big', 'short', T): 0.3, ('big', 'short', F): 0.5, ('big', 'medium', T): 0.6, ('big', 'medium', F): 0.8, ('big', 'long', T): 0.7, ('big', 'long', F): 0.9}))


def ask(var, val, e):
    evidence = dict()
    for key in e.keys():
        evidence[chatbot_net.lookup[key]] = e[key]
    dist = helpers.enumeration_ask(chatbot_net.lookup[var], evidence, chatbot_net)
    return dist[val]

def q8(var='Resolved', val=True, e={'ConversationLength': 'long', 'ProblemSize': 'big', 'Accurate': T}):
    result = ask(var, val, e)
    print('{:.2f}'.format(result))



def q9():
    x = helpers.joint_distribution(chatbot_net)
    vars = chatbot_net.variables
    #print(vars)
    print(vars[0].__name__ + '  ' + vars[1].__name__ + '\t' + vars[2].__name__ + '  ' + vars[3].__name__ + '\t' + vars[4].__name__ + ' | Probability')
    print('----------------------------------------------------------------------------------')
    for row in x:
        #print(str(row) + ' = {:.3f}'.format(x[row]))
        print(str(row[0]) + '\t  ' + str(row[1]) + '\t\t' + str(row[2]) + '\t\t    ' + str(row[3]) + '\t' + str(row[4]) + '\t   | {:.3f}'.format(x[row]))


class CustomerServiceCallPlus(Problem):
    def __init__(self, max_turns=None):
        super(CustomerServiceCallPlus, self).__init__('D1')
        self.max_turns = max_turns

    def actions(self, state):
        state = str(state)
        #print('CustomerServiceCall.actions(' + state + ')')
        if state[0] == 'D':
            return ['respond', 'redirect']
        elif state[0] == 'C' and odd(int(state.lstrip('C'))):
            return ['~resolved', 'resolved']
        elif state[0] == 'C' and even(int(state.lstrip('C'))):
            return ['frustrated', '~frustrated']
        elif state[0] == 'U':
            return []

    def result(self, state, action):
        state = str(state)
        action = str(action)
        #print('CustomerServiceCall.result(' + state + ', ' + action + ')')
        if state[0] == 'D':
            num = int(state.lstrip('D'))
            turn = num
            if action is 'respond':
                return 'C' + str(turn*2-1)
            elif action is 'redirect':
                return 'C' + str(turn*2)
        elif state[0] == 'C':
            num = int(state.lstrip('C'))
            turn = int((num + (num % 2)) / 2)
            if action == '~resolved':
                return 'U' + str(3 * (turn - 1) + 1) if turn >= self.max_turns else 'D' + str(turn + 1)
            elif action == 'resolved':
                return 'U' + str(3 * (turn - 1) + 2) if turn >= self.max_turns else 'U' + str(3 * turn - 2)
            elif action == 'frustrated':
                return 'U' + str(3 * (turn - 1) + 3) if turn >= self.max_turns else 'U' + str(3 * turn - 1)
            elif action == '~frustrated':
                return 'U' + str(3 * (turn - 1) + 4) if turn >= self.max_turns else 'U' + str(3 * turn)
        elif state[0] == 'U':
            return state

    def value(self, state):
        # value of a state is resolved using expectimax algorithm
        state = str(state)
        #print('CustomerServiceCall.value(' + state + ')')
        if state[0] == 'D':
            return max([self.value(child) for child in [self.result(state, a) for a in self.actions(state)]])
        elif state[0] == 'C':
            num = int(state.lstrip('C'))
            turn = int((num + (num % 2)) / 2)
            cl = 'long'
            r_true = 0.67
            r_false = 0.33
            f_true = 0.62
            f_false = 0.38
            if turn <= 5:
                cl = 'short'
                r_true = 0.29
                r_false = 0.71
                f_true = 0.22
                f_false = 0.78
            elif turn <= 10:
                cl = 'medium'
                r_true = 0.52
                r_false = 0.48
                f_true = 0.32
                f_false = 0.68

            e = {'ConversationLength': cl, 'ProblemSize': 'small'}

            r_false = ask('Resolved', False, e)
            r_true = ask('Resolved', True, e)
            f_true = ask('Frustrated', True, e)
            f_false = ask('Frustrated', False, e)

            if odd(num):
                return round(r_false * self.value(self.result(state, '~resolved')) + r_true * self.value(self.result(state, 'resolved')), 2)
            elif even(num):
                return round(f_true * self.value(self.result(state, 'frustrated')) + f_false * self.value(self.result(state, '~frustrated')), 2)
        elif state[0] == 'U':
            num = int(state.lstrip('U'))
            k = int((num - 1) / 3) + 1
            if k >= self.max_turns:
                num = num - ((self.max_turns-1)*3)
                rem = num % 4
                if rem == 1:
                    return 0
                if rem == 2:
                    return 100
                if rem == 3:
                    return 5*self.max_turns
                if rem == 0:
                    return -100 + 5*self.max_turns
            else:
                rem = num % 3
                if rem == 1:
                    return 100
                if rem == 2:
                    return 5*k
                if rem == 0:
                    return -100 + 5*k


def q10(max_turns=40):
    p = CustomerServiceCallPlus(max_turns)
    choices = []
    print('Turn\tRespond\tRedirect\tChoice')
    print('----------------------------------------------')
    for i in range(1, max_turns + 1):
        respond = p.value(p.result('D' + str(i), 'respond'))
        redirect = p.value(p.result('D' + str(i), 'redirect'))
        choices.append('respond') if respond >= redirect else choices.append('redirect')
        print(str(i) + ':\t' + str(respond) + '\t' + str(redirect) + '\t\t' + choices[i - 1])
    return choices


class CustomMDP(mdp.MDP):
    def __init__(self, transition_matrix, rewards, terminals, init, gamma=.9):
        # All possible actions.
        actlist = []
        for state in transition_matrix.keys():
            actlist.extend(transition_matrix[state])
        actlist = list(set(actlist))

        mdp.MDP.__init__(self, init, actlist, terminals, gamma=gamma)
        self.t = transition_matrix
        self.reward = rewards
        for state in self.t:
            self.states.add(state)

    def T(self, state, action):
        if action is None:
            return [(0.0, state)]
        else:
            return [(prob, new_state) for new_state, prob in self.t[state][action].items()]


t = {
    'D_short1': {
        'respond': {
            'Resolved': 0.29,
            'D_short2': 0.71
        },
        'redirect': {
            'Frustrated_s': 0.22,
            'NotFrustrated_s': 0.78
        }
    },
    'D_short2': {
        'respond': {
            'Resolved': 0.29,
            'D_short3': 0.71
        },
        'redirect': {
            'Frustrated_s': 0.22,
            'NotFrustrated_s': 0.78
        }
    },
    'D_short3': {
        'respond': {
            'Resolved': 0.29,
            'D_short4': 0.71
        },
        'redirect': {
            'Frustrated_s': 0.22,
            'NotFrustrated_s': 0.78
        }
    },
    'D_short4': {
        'respond': {
            'Resolved': 0.29,
            'D_short5': 0.71
        },
        'redirect': {
            'Frustrated_s': 0.22,
            'NotFrustrated_s': 0.78
        }
    },
    'D_short5': {
        'respond': {
            'Resolved': 0.29,
            'D_medium1': 0.71
        },
        'redirect': {
            'Frustrated_s': 0.22,
            'NotFrustrated_s': 0.78
        }
    },
    'D_medium1': {
        'respond': {
            'Resolved': 0.52,
            'D_medium2': 0.48
        },
        "redirect": {
            'Frustrated_m': 0.32,
            'NotFrustrated_m': 0.68
        }
    },
    'D_medium2': {
        'respond': {
            'Resolved': 0.52,
            'D_medium3': 0.48
        },
        "redirect": {
            'Frustrated_m': 0.32,
            'NotFrustrated_m': 0.68
        }
    },
    'D_medium3': {
        'respond': {
            'Resolved': 0.52,
            'D_medium4': 0.48
        },
        "redirect": {
            'Frustrated_m': 0.32,
            'NotFrustrated_m': 0.68
        }
    },
    'D_medium4': {
        'respond': {
            'Resolved': 0.52,
            'D_medium5': 0.48
        },
        "redirect": {
            'Frustrated_m': 0.32,
            'NotFrustrated_m': 0.68
        }
    },
    'D_medium5': {
        'respond': {
            'Resolved': 0.52,
            'D_long': 0.48
        },
        "redirect": {
            'Frustrated_m': 0.32,
            'NotFrustrated_m': 0.68
        }
    },
    'D_long': {
        'respond': {
            'Resolved': 0.67,
            'D_long': 0.33
        },
        'redirect': {
            'Frustrated_l': 0.62,
            'NotFrustrated_l': 0.38
        }
    },
    'Resolved': {},
    'Frustrated_s': {},
    'NotFrustrated_s': {},
    'Frustrated_m': {},
    'NotFrustrated_m': {},
    'Frustrated_l': {},
    'NotFrustrated_l': {}
}

init = 'D_short1'

terminals = ['Resolved', 'Frustrated_s', 'NotFrustrated_s', 'Frustrated_m', 'NotFrustrated_m', 'Frustrated_l', 'NotFrustrated_l']

rewards = {
    'Resolved': 100,
    'Frustrated_s': 15,
    'NotFrustrated_s': -85,
    'Frustrated_m': 40,
    'NotFrustrated_m': -60,
    'Frustrated_l': 75,
    'NotFrustrated_l': -25
}


chatbot_mdp = CustomMDP(t, rewards, terminals, init)


def partD():
    policy = mdp.best_policy(chatbot_mdp, mdp.value_iteration(chatbot_mdp))
    print('State: choice\n-----------------------')
    for s in chatbot_mdp.states:
        print(str(s) + ': ' + str(policy[s]))
